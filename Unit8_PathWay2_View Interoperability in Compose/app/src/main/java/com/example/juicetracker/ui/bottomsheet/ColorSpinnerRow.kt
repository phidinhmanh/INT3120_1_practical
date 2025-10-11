/**
 * Package: com.example.juicetracker.ui.bottomsheet
 *
 * 🧃 MỤC ĐÍCH:
 * Tạo giao diện chọn màu nước ép (JuiceColor) bằng Spinner (thành phần View gốc của Android)
 * nhưng được tích hợp mượt mà vào Jetpack Compose UI.
 *
 * ⚙️ THÀNH PHẦN CHÍNH:
 * - [SpinnerAdapter]: Bộ lắng nghe xử lý sự kiện khi người dùng chọn màu trong Spinner.
 * - [findColorIndex]: Hàm tiện ích chuyển đổi tên màu thành chỉ số trong danh sách JuiceColor.
 * - [ColorSpinnerRow]: Thành phần Composable hiển thị giao diện chọn màu.
 *
 * 💡 ỨNG DỤNG:
 * Dùng trong màn hình “Thêm / Chỉnh sửa nước ép” để cho phép người dùng chọn màu tương ứng
 * với loại nước ép họ đang thêm vào ứng dụng.
 */

package com.example.juicetracker.ui.bottomsheet

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.juicetracker.R
import com.example.juicetracker.data.JuiceColor


/**
 * 🌀 SpinnerAdapter
 *
 * Adapter lắng nghe các sự kiện chọn item trong Spinner.
 *
 * @property onColorChange Callback được gọi mỗi khi người dùng thay đổi lựa chọn màu.
 *
 * **WHAT:**
 * - Cài đặt `AdapterView.OnItemSelectedListener` để nhận sự kiện chọn màu trong Spinner.
 *
 * **WHY:**
 * - Giúp truyền thông tin về màu được chọn từ thành phần View (Spinner)
 *   sang Compose thông qua callback `onColorChange`.
 *
 * **CÁCH HOẠT ĐỘNG:**
 * - Khi người dùng chọn một item: gọi `onColorChange(position)`.
 * - Nếu không chọn gì: mặc định chọn vị trí 0.
 */
class SpinnerAdapter(
    val onColorChange: (Int) -> Unit
) : AdapterView.OnItemSelectedListener {

    /**
     * Gọi khi người dùng chọn một phần tử trong Spinner.
     *
     * @param parent Spinner chứa danh sách màu.
     * @param view View con được chọn (item hiển thị).
     * @param position Vị trí của item được chọn.
     * @param id ID của item (không dùng ở đây).
     */
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onColorChange(position)
    }

    /**
     * Gọi khi không có item nào được chọn trong Spinner.
     * Ở đây ta chọn mặc định là màu đầu tiên (index 0).
     */
    override fun onNothingSelected(p0: AdapterView<*>?) {
        onColorChange(0)
    }
}




/**
 * 🧩 ColorSpinner
 *
 * Thành phần giao diện (Composable) hiển thị Spinner chọn màu nước ép.
 *
 * @param onColorSpinnerPosition Vị trí màu hiện tại đang được chọn trong Spinner.
 * @param onColorChange Callback được gọi khi người dùng chọn một màu khác.
 * @param modifier Modifier Compose dùng để tùy chỉnh bố cục, kích thước, hoặc padding.
 *
 * **WHAT:**
 * - Tạo giao diện chọn màu trong Compose bằng cách nhúng một Spinner gốc (Android View).
 * - Hiển thị danh sách tên màu từ enum [JuiceColor].
 * - Cập nhật màu hiện tại và xử lý khi người dùng chọn màu khác.
 *
 * **WHY:**
 * - Dùng Spinner gốc để tận dụng UI quen thuộc và logic xử lý có sẵn của Android.
 * - Giúp dễ dàng tái sử dụng trong các thành phần Compose khác mà không cần viết DropdownMenu thủ công.
 *
 * **CÁCH HOẠT ĐỘNG:**
 * 1. Lấy danh sách tên hiển thị của các màu từ enum [JuiceColor].
 * 2. Tạo Spinner và gắn [ArrayAdapter] để hiển thị danh sách.
 * 3. Dùng [AndroidView] để nhúng Spinner vào Compose UI.
 * 4. Gắn [SpinnerAdapter] để xử lý khi người dùng chọn màu.
 *
 * **VÍ DỤ SỬ DỤNG:**
 * ```kotlin
 * ColorSpinner(
 *     onColorSpinnerPosition = 2,
 *     onColorChange = { newIndex -> viewModel.updateColor(newIndex) }
 * )
 * ```
 */
@Composable
fun ColorSpinnerRow(
    onColorSpinnerPosition: Int,
    onColorChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Tạo danh sách tên màu từ enum JuiceColor (ví dụ: "Red", "Green", "Purple", ...)
    val juiceArray = JuiceColor.entries.map { juiceColor ->
        stringResource(juiceColor.label)
    }

    // InputRow là thành phần UI bọc Spinner, hiển thị nhãn "Color"
    InputRow(inputLabel = stringResource(R.string.color), modifier = modifier) {

        // AndroidView cho phép nhúng View truyền thống vào trong Compose
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                Spinner(context).apply {
                    adapter = ArrayAdapter(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        juiceArray
                    )
                }
            },
            update = { spinner ->
                spinner.setSelection(onColorSpinnerPosition)
                spinner.onItemSelectedListener = SpinnerAdapter(onColorChange)
            }
        )
    }
}
