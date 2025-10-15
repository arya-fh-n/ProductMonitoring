package com.arfdevs.productmonitoring.presentation.view.ui.home

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.ActivityDashboardBinding
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.visible
import com.arfdevs.productmonitoring.presentation.view.base.BaseActivity
import com.arfdevs.productmonitoring.presentation.viewmodel.ProdukViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.ReportViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.TokoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity<ActivityDashboardBinding>(
    ActivityDashboardBinding::inflate
) {

    private val reportVM: ReportViewModel by viewModel()
    private val produkVM: ProdukViewModel by viewModel()
    private val tokoVM: TokoViewModel by viewModel()

    private lateinit var navController: NavController

    override fun setupView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_dashboard) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        reportVM.getLatestAttendance()
        initObserver()
    }

    private fun initObserver() {
        reportVM.attendance.observe(this) { state ->
            showLoading(state is UiState.Loading)
            binding.fcvDashboard.visible(state is UiState.Success)
            when (state) {
                is UiState.Error -> showError(state.message)
                UiState.ErrorConnection -> showError("Error koneksi internet")
                else -> {}
            }
        }

        tokoVM.listToko.observe(this) { state ->
            showLoading(state is UiState.Loading)
        }

        produkVM.listProduk.observe(this) { state ->
            showLoading(state is UiState.Loading)
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        loadingOverlay.visible(isLoading)
        loadingAnim.visible(isLoading)
    }

    private fun showError(message: String) {
        binding.evHome.apply {
            visible(true)
            setMessage(
                "Error",
                message,
                "Coba Lagi"
            ) {
                reportVM.getLatestAttendance()
            }
        }
    }

}
