package com.example.bluromatic.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.KEY_BLUR_LEVEL
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import androidx.core.net.toUri
import androidx.work.workDataOf

private const val TAG = "BlurWorker"

/**
 * WHAT: Worker chạy nền để làm mờ ảnh.
 * WHY: WorkManager điều phối job dài, sống sót qua process death; CoroutineWorker cho phép suspend an toàn.
 */
class BlurWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    /**
     * WHAT: Điểm vào chính của job blur ảnh.
     * WHY: WorkManager gọi hàm này; phải trả Result để báo trạng thái cuối cùng (success/failure/cancelled).
     */
    override suspend fun doWork(): Result {

        // WHAT: Lấy input do bên enqueue truyền vào.
        // WHY: Worker là tiến trình nền độc lập, không dựa vào state UI; mọi tham số nên đi qua inputData.
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)

        // WHAT: Thông báo “đang blur”.
        // WHY: Nâng UX, giúp dev xác nhận job đã thực sự bắt đầu chạy ở nền.
        makeStatusNotification(
            applicationContext.resources.getString(R.string.blurring_image),
            applicationContext
        )

        // WHAT: Chuyển toàn bộ pipeline I/O sang IO dispatcher.
        // WHY: Decode/ghi file là blocking I/O; IO pool chịu được nhiều block hơn, tránh nghẽn Default/Main.
        return withContext(Dispatchers.IO) {

            // WHAT: Tạo suspend point nhẹ.
            // WHY: Giả lập tác vụ dài, tạo cơ hội cancellation; delay không block thread như Thread.sleep().
            delay(DELAY_TIME_MILLIS)

            // WHAT: try/catch bao toàn bộ decode → blur → write → notify.
            // WHY: Lỗi ở bất kỳ bước nào sẽ được log và trả failure có kiểm soát cho WorkManager (có thể retry).
            return@withContext try {
                // WHAT: Validate input bắt buộc (URI).
                // WHY: Không có URI thì không đọc được ảnh; fail sớm với thông điệp rõ.
                require(!resourceUri.isNullOrBlank()) {
                    val errorMessage =
                        applicationContext.resources.getString(R.string.invalid_input_uri)
                    Log.e(TAG, errorMessage) // log trước khi ném exception để có trace
                    errorMessage
                }

                val resolver = applicationContext.contentResolver

                // WHAT: Decode bitmap từ ContentResolver qua URI người dùng.
                // WHY: Nhiều dữ liệu Android chỉ truy cập qua content:// thay vì file path.
                // FIXME: openInputStream phải được đóng (use {}) và decodeStream có thể trả null → cần check null.
                val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(resourceUri.toUri())
                )

                // WHAT: Áp dụng blur với mức do input cung cấp.
                // WHY: Tác vụ CPU-bound; nếu nặng, cân nhắc chuyển khối này sang Dispatchers.Default.
                // FIXME: picture có thể null → cần guard để tránh NPE.
                val output = blurBitmap(picture, blurLevel)

                // WHAT: Ghi bitmap kết quả ra file và nhận về URI.
                // WHY: Cho phép bước sau/UI truy cập kết quả.
                val outputUri = writeBitmapToFile(applicationContext, output)

                // WHAT: Đóng gói output để chuyển cho chain/UI.
                // WHY: Cần một key riêng cho output; không nên reuse KEY_IMAGE_URI (dễ nhầm input vs output).
                // FIXME: dùng key OUTPUT_URI riêng thay vì KEY_IMAGE_URI.
                val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())


                // WHAT: Báo thành công kèm outputData.
                // WHY: Cho chain hoặc UI đọc kết quả.
                Result.success(outputData)

            } catch (throwable: Throwable) {
                // WHAT: Log lỗi có ngữ cảnh.
                // WHY: Dễ truy vết và quyết định retry/alert.
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_applying_blur),
                    throwable,
                )

                // WHAT: Trả failure.
                // WHY: WorkManager căn cứ để đánh dấu thất bại (hoặc retry theo policy).
                // FIXME: Phân biệt CancellationException để không gộp “cancelled” thành “failure”.
                Result.failure()
            }
        }
    }
}
