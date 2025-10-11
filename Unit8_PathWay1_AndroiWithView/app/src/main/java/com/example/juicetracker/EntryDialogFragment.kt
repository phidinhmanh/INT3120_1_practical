package com.example.juicetracker

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.juicetracker.data.JuiceColor
import com.example.juicetracker.databinding.FragmentEntryDialogBinding
import com.example.juicetracker.ui.AppViewModelProvider
import com.example.juicetracker.ui.EntryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

/**
 * WHAT: Một [BottomSheetDialogFragment] dùng để tạo mới hoặc chỉnh sửa một mục nước ép.
 * WHY: Sử dụng BottomSheetDialogFragment cung cấp một giao diện người dùng linh hoạt, hiện đại,
 * cho phép người dùng nhập liệu mà không cần rời khỏi màn hình chính, tạo ra trải nghiệm liền mạch.
 */
class EntryDialogFragment : BottomSheetDialogFragment() {

    /**
     * WHAT: Một instance của [EntryViewModel] được ủy quyền (delegated).
     * WHY: Để tách biệt logic nghiệp vụ (lưu, cập nhật, lấy dữ liệu) ra khỏi UI controller (Fragment).
     * Việc sử dụng `by viewModels` đảm bảo ViewModel tồn tại qua các thay đổi cấu hình (như xoay màn hình)
     * và được liên kết với vòng đời của Fragment. `AppViewModelProvider.Factory` được dùng để "tiêm"
     * (inject) `JuiceRepository` vào ViewModel.
     */
    private val entryViewModel by viewModels<EntryViewModel> { AppViewModelProvider.Factory }

    /**
     * WHAT: Biến để lưu trữ màu sắc nước ép được chọn từ Spinner. Mặc định là 'Red'.
     * WHY: Để giữ trạng thái lựa chọn của người dùng từ Spinner. Biến này sẽ được sử dụng
     * khi lưu thông tin nước ép, đảm bảo đúng màu sắc được ghi vào cơ sở dữ liệu.
     */
    private var selectedColor: JuiceColor = JuiceColor.Red

    /**
     * WHAT: Ghi đè phương thức vòng đời `onCreateView` để tạo và trả về layout cho fragment.
     * WHY: Phương thức này "thổi phồng" (inflate) layout XML (`FragmentEntryDialogBinding`) thành một đối tượng View
     * bằng cách sử dụng View Binding. View Binding tạo ra một lớp binding an toàn về kiểu, giúp tránh lỗi
     * NullPointerException khi truy cập các view và làm cho mã nguồn gọn gàng hơn.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEntryDialogBinding.inflate(inflater, container, false).root
    }

    /**
     * WHAT: Ghi đè phương thức `onViewCreated`, được gọi ngay sau khi `onCreateView` hoàn tất.
     * Đây là nơi để thực hiện logic khởi tạo UI, thiết lập listeners và quan sát dữ liệu.
     * WHY: Tách biệt logic khỏi việc tạo view. Tại thời điểm này, view hierarchy đã được tạo đầy đủ
     * và an toàn để tương tác với các thành phần UI (như Button, Spinner).
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentEntryDialogBinding.bind(view)
        val args: EntryDialogFragmentArgs by navArgs()
        val juiceId = args.itemId

        // --- Thiết lập Spinner cho việc chọn màu ---
        // WHAT: Tạo một ArrayAdapter để cung cấp dữ liệu (tên các màu) cho Spinner.
        // WHY: ArrayAdapter là một cách tiêu chuẩn và hiệu quả để kết nối một nguồn dữ liệu (ở đây là danh sách tên màu)
        // với một UI widget có khả năng hiển thị danh sách như Spinner.
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item, // Sử dụng layout spinner mặc định của Android
            JuiceColor.values().map { it.name }
        )
        binding.juiOption.adapter = adapter

        // WHAT: Gán một OnItemSelectedListener để lắng nghe sự kiện khi người dùng chọn một mục trong Spinner.
        // WHY: Để cập nhật biến `selectedColor` theo lựa chọn của người dùng trong thời gian thực.
        // Điều này đảm bảo rằng khi người dùng nhấn "Lưu", màu sắc được chọn chính xác sẽ được ghi lại.
        binding.juiOption.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedColor = JuiceColor.valueOf(parent.getItemAtPosition(position) as String)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Không cần xử lý trong trường hợp này
            }
        }

        // --- Tải dữ liệu nếu là chế độ chỉnh sửa ---
        // WHAT: Kiểm tra nếu `juiceId` > 0. Nếu đúng, đây là chế độ chỉnh sửa một mục đã có.
        // WHY: Để phân biệt giữa việc tạo mới (juiceId <= 0) và chỉnh sửa. Nếu là chỉnh sửa,
        // chúng ta cần lấy dữ liệu hiện có của mục đó và điền vào các trường UI.
        if (juiceId > 0) {
            // WHAT: Bắt đầu một coroutine an toàn với vòng đời để quan sát dòng dữ liệu từ ViewModel.
            // `repeatOnLifecycle(Lifecycle.State.STARTED)` đảm bảo coroutine chỉ chạy khi Fragment ở trạng thái STARTED hoặc RESUMED.
            // WHY: Để cập nhật UI một cách hiệu quả và an toàn. Khi dữ liệu trong database thay đổi,
            // Flow sẽ phát ra giá trị mới và UI sẽ tự động được cập nhật. Việc sử dụng coroutine
            // và lifecycle-aware scope ngăn chặn rò rỉ bộ nhớ và các sự cố liên quan đến vòng đời.
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    entryViewModel.getJuiceStream(juiceId).filterNotNull().collect { juice ->
                        binding.juiceName.setText(juice.name)
                        binding.juiceDescription.setText(juice.description)
                        binding.ratingBar.rating = juice.rating.toFloat()
                        // WHAT: Đặt lựa chọn hiện tại của Spinner khớp với màu của nước ép.
                        // WHY: Để người dùng thấy được màu sắc đã lưu trước đó khi họ mở màn hình chỉnh sửa.
                        binding.juiOption.setSelection(
                            JuiceColor.entries.indexOf(JuiceColor.valueOf(juice.color))
                        )
                    }
                }
            }
        }

        // --- Thiết lập nút Lưu ---
        // WHAT: Gán một OnClickListener cho nút "Lưu".
        // WHY: Để thực hiện hành động lưu dữ liệu khi người dùng nhấn nút.
        binding.btnSave.setOnClickListener {
            // WHAT: Gọi phương thức `saveJuice` trên ViewModel, truyền vào dữ liệu được thu thập từ các trường UI.
            // WHY: Tuân thủ kiến trúc MVVM, Fragment chỉ có nhiệm vụ thu thập đầu vào và ra lệnh cho ViewModel.
            // ViewModel sẽ xử lý logic lưu trữ dữ liệu thực tế.
            entryViewModel.saveJuice(
                id = juiceId,
                name = binding.juiceName.text.toString(),
                description = binding.juiceDescription.text.toString(),
                color = selectedColor.name,
                rating = binding.ratingBar.rating.toInt()
            )
            // WHAT: Đóng dialog sau khi lưu.
            // WHY: Để quay trở lại màn hình trước đó, cung cấp phản hồi cho người dùng rằng hành động đã hoàn tất.
            dismiss()
        }
    }
}
