package com.arfdevs.productmonitoring.presentation.view.ui.attendance

import com.arfdevs.productmonitoring.databinding.FragmentAttendanceBinding
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.presentation.view.base.BaseFragment
import com.arfdevs.productmonitoring.presentation.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AttendanceFragment : BaseFragment<FragmentAttendanceBinding>(
    FragmentAttendanceBinding::inflate
) {

    private val authVM: AuthViewModel by activityViewModel()

    override fun initView() {
        initObserver()
        initListener()
    }

    private fun initListener() = with(binding) {
        btnFetch.setOnClickListener {
            Snackbar.make(it, "Fetching products...", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun initObserver() = with(binding) {
        authVM.session.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Success -> {
                    text2.text = "Username: ${it.data.username}"
                    text3.text = "Username: ${it.data.token}"
                }
                else -> {}
            }
        }
    }


}
