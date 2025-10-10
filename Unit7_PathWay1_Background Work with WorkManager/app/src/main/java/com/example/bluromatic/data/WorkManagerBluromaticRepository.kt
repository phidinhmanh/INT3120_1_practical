/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// 💬 Đây là phần license chuẩn của Google (Apache License 2.0).
// Nó cho phép bạn sử dụng, chỉnh sửa, phân phối code này
// miễn là bạn giữ lại phần ghi chú bản quyền này.
// Không liên quan đến logic chương trình.

package com.example.bluromatic.data

import android.content.Context
import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.bluromatic.KEY_BLUR_LEVEL
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.getImageUri
import com.example.bluromatic.workers.BlurWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Repository dùng WorkManager để chạy các tác vụ nền liên quan đến "blur" ảnh.
 *
 * Mục tiêu của lớp này:
 * - Chuẩn bị dữ liệu (inputData) cho Worker
 * - Khởi chạy (enqueue) công việc làm mờ ảnh
 * - Cung cấp Flow để quan sát trạng thái công việc (outputWorkInfo)
 */
class WorkManagerBluromaticRepository(context: Context) : BluromaticRepository {

    // 🔹 Flow chứa thông tin về trạng thái công việc đang chạy (hoặc đã xong)
    // MutableStateFlow(null) nghĩa là ban đầu chưa có WorkInfo nào.
    // Sau này có thể dùng để emit WorkInfo mới khi Worker thay đổi trạng thái.
    override val outputWorkInfo: Flow<WorkInfo?> = MutableStateFlow(null)

    // 🔹 Lấy URI ảnh mẫu (được định nghĩa sẵn trong hàm extension getImageUri()).
    // Đây là ảnh nguồn mà Worker sẽ làm mờ.
    private var imageUri: Uri = context.getImageUri()

    // 🔹 Lấy instance WorkManager dùng chung toàn app.
    // WorkManager chịu trách nhiệm lên lịch, lưu, chạy và quản lý các tác vụ nền.
    private val workManager = WorkManager.getInstance(context)

    /**
     * Hàm khởi chạy công việc làm mờ ảnh.
     * @param blurLevel Mức độ làm mờ (số nguyên, càng lớn càng mờ).
     */
    override fun applyBlur(blurLevel: Int) {
        // ⚠️ VẤN ĐỀ: Đoạn này trong codelab gốc bị lỗi thứ tự logic.
        // Builder phải set inputData TRƯỚC khi build() và enqueue().

        val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()

        blurBuilder.setInputData(createInputDataForWorkRequest(blurLevel, imageUri))

        workManager.enqueue(blurBuilder.build())


    }

    /**
     * Hàm hủy mọi công việc đang chạy (chưa được implement).
     * WorkManager cho phép cancel theo tag, id, hoặc toàn bộ hàng đợi.
     * Ví dụ:
     *     workManager.cancelAllWork()
     * hoặc:
     *     workManager.cancelAllWorkByTag("blur")
     */
    override fun cancelWork() {}

    /**
     * Tạo dữ liệu đầu vào (inputData) cho Worker.
     * @param blurLevel: mức độ làm mờ (int)
     * @param imageUri: đường dẫn ảnh cần xử lý
     * @return đối tượng Data chứa các cặp key-value mà Worker sẽ nhận trong doWork()
     */
    private fun createInputDataForWorkRequest(blurLevel: Int, imageUri: Uri): Data {
        // Data.Builder giúp gói dữ liệu truyền vào Worker.
        // Chỉ hỗ trợ kiểu dữ liệu cơ bản: String, Int, Boolean, Double, Array...
        val builder = Data.Builder()

        // Ghi hai thông tin cần thiết:
        // 1️⃣ URI ảnh (chuyển thành chuỗi để truyền qua Data)
        // 2️⃣ Mức độ làm mờ
        builder.putString(KEY_IMAGE_URI, imageUri.toString())
            .putInt(KEY_BLUR_LEVEL, blurLevel)

        // Trả về Data hoàn chỉnh.
        return builder.build()
    }
}
