package com.arfdevs.productmonitoring.presentation.view.ui.home

import coil.load
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.FragmentHomeBinding
import com.arfdevs.productmonitoring.helper.Constants
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.enabled
import com.arfdevs.productmonitoring.helper.goneIf
import com.arfdevs.productmonitoring.helper.isError
import com.arfdevs.productmonitoring.presentation.view.base.BaseFragment
import com.arfdevs.productmonitoring.presentation.viewmodel.ReportViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val reportVM: ReportViewModel by activityViewModel()

    override fun initView() = with(binding) {
        tvAttendanceTitle.text = getString(R.string.tv_attendance_title)
        ivAttendanceStatus.load(R.drawable.ic_attendance_pending)
        tvAttendanceStatus.text = getString(R.string.attendance_status_not)
        btnSetPresent.text = getString(R.string.btn_set_present)
        btnSetAbsent.text = getString(R.string.btn_set_absent)

        initObserver()
        initListener()
    }

    private fun initObserver() {
        reportVM.attendance.observe(viewLifecycleOwner) { state ->
            hideContent(state.isError(false))
            when (state) {
                is UiState.Success -> {
                    updateAttendanceStatusUI(state.data)
                }

                UiState.Empty -> {
                    updateAttendanceStatusUINoAttendance()
                }

                else -> {}
            }
        }
    }


    private fun updateAttendanceStatusUI(isPresent: Boolean) = with(binding) {
        btnSetPresent.enabled(!isPresent)
        btnSetAbsent.enabled(isPresent)

        val statusIcon =
            if (isPresent) R.drawable.ic_check_positive else R.drawable.ic_check_negative
        val statusText =
            if (isPresent) R.string.attendance_status_present else R.string.attendance_status_absent

        ivAttendanceStatus.load(statusIcon)
        tvAttendanceStatus.text = getString(statusText)
    }

    private fun updateAttendanceStatusUINoAttendance() = with(binding) {
        btnSetPresent.enabled(true)
        btnSetAbsent.enabled(true)

        ivAttendanceStatus.load(R.drawable.ic_attendance_pending)
        tvAttendanceStatus.text = getString(R.string.attendance_status_not)
    }

    private fun hideContent(isError: Boolean) = with(binding) {
        tvTitleWelcome.goneIf(isError)
        tvAttendanceTitle.goneIf(isError)
        ivAttendanceStatus.goneIf(isError)
        tvAttendanceStatus.goneIf(isError)
        btnSetPresent.goneIf(isError)
        btnSetAbsent.goneIf(isError)
    }

    private fun initListener() {
        binding.btnSetAbsent.setOnClickListener {
            reportVM.setAttendance(Constants.ABSEN)
        }

        binding.btnSetPresent.setOnClickListener {
            reportVM.setAttendance(Constants.HADIR)
        }
    }

}
