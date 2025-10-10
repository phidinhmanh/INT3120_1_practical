package com.example.bluromatic.data

import android.content.Context
import android.net.Uri
import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.bluromatic.IMAGE_MANIPULATION_WORK_NAME
import com.example.bluromatic.KEY_BLUR_LEVEL
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.TAG_OUTPUT
import com.example.bluromatic.getImageUri
import com.example.bluromatic.workers.BlurWorker
import com.example.bluromatic.workers.CleanupWorker
import com.example.bluromatic.workers.SaveImageToFileWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull

class WorkManagerBluromaticRepository(context: Context) : BluromaticRepository {

    // WHAT: URI ảnh nguồn sẽ được xử lý.
    // WHY: Repo quyết định "đầu vào" cho pipeline; ở codelab dùng helper lấy URI demo.
    private var imageUri: Uri = context.getImageUri()

    // WHAT: Lấy singleton WorkManager cho app.
    // WHY: Mọi thao tác enqueue/cancel/observe work đều đi qua đây.
    private val workManager = WorkManager.getInstance(context)

    // WHAT: Dòng trạng thái của "output work" (work gắn TAG_OUTPUT) dưới dạng Flow.
    // WHY: UI có thể observe để biết khi nào save xong (SUCCEEDED/FAILED/RUNNING...).
    //      Codelab chọn work đầu tiên (it.first()) nếu có nhiều work cùng tag.
    override val outputWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfosByTagLiveData(TAG_OUTPUT) // LiveData<List<WorkInfo>>
            .asFlow()                                      // chuyển sang Flow
            .mapNotNull { list -> if (list.isNotEmpty()) list.first() else null }
    // WHY: chỉ emit phần tử đầu tiên để đơn giản hóa UI; nếu có nhiều "save" song song,
    //      cân nhắc lọc theo ID request cụ thể thay vì "first()".

    /**
     * WHAT: Xây pipeline gồm 3 bước:
     *       1) Cleanup tệp tạm  →  2) Blur ảnh  →  3) Save ảnh đầu ra (tag = TAG_OUTPUT)
     * WHY: Sử dụng chuỗi Work để đảm bảo thứ tự, bền bỉ qua app kill/reboot, và có thể retry.
     * @param blurLevel Mức độ làm mờ
     */
    override fun applyBlur(blurLevel: Int) {

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        // WHAT: Bắt đầu một "Unique Work" tên IMAGE_MANIPULATION_WORK_NAME.
        // WHY: Đảm bảo không có nhiều pipeline cùng tên chạy trùng nhau. Policy.REPLACE sẽ hủy pipeline cũ (nếu có)
        //      và thay bằng pipeline mới (cách UX hay cho nút "Blur lại").
        var continuation = workManager.beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(CleanupWorker::class.java) // bước 1: dọn tệp tạm
        )

        // WHAT: Tạo WorkRequest cho bước blur.
        // WHY: Bước 2 thực hiện xử lý ảnh; cần có inputData (URI ảnh + blur level).
        val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
        blurBuilder.setInputData(createInputDataForWorkRequest(blurLevel, imageUri))
        blurBuilder.setConstraints(constraints)
        // WHAT: Nối bước blur vào sau cleanup.
        // WHY: Bảo đảm cleanup chạy xong mới blur (thứ tự xác định).
        continuation = continuation.then(blurBuilder.build())

        // WHAT: Tạo WorkRequest để lưu ảnh ra storage "ổn định" (MediaStore/Files).
        // WHY: Bước 3 là "đầu ra cuối", gắn tag TAG_OUTPUT để dễ quan sát từ UI.
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .addTag(TAG_OUTPUT) // UI sẽ observe theo tag này
            .build()
        continuation = continuation.then(save)

        // WHAT: Thực sự enqueue chuỗi công việc.
        // WHY: Cho WorkManager lên lịch & thực thi theo điều kiện hệ thống.
        continuation.enqueue()
    }

    /**
     * WHAT: Hủy các WorkRequest đang chạy (chưa implement).
     * WHY: Cần cho UX nút Cancel; tuỳ chiến lược, có thể cancel unique work hoặc theo tag.
     */
    override fun cancelWork() {
        // Ví dụ (đề xuất):
        // workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
        // hoặc: workManager.cancelAllWorkByTag(TAG_OUTPUT)
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    /**
     * WHAT: Đóng gói dữ liệu input cho BlurWorker: URI ảnh + mức blur.
     * WHY: Worker là tiến trình nền độc lập; mọi tham số nên truyền qua Data (chỉ hỗ trợ kiểu cơ bản).
     */
    private fun createInputDataForWorkRequest(blurLevel: Int, imageUri: Uri): Data {
        return Data.Builder()
            .putString(KEY_IMAGE_URI, imageUri.toString())
            .putInt(KEY_BLUR_LEVEL, blurLevel)
            .build()
    }
}
