package com.arfdevs.productmonitoring.presentation.view.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.ActivityDashboardBinding
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.visible
import com.arfdevs.productmonitoring.presentation.view.base.BaseActivity
import com.arfdevs.productmonitoring.presentation.view.ui.MainActivity
import com.arfdevs.productmonitoring.presentation.viewmodel.AuthViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.ProdukViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.ReportViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.TokoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity<ActivityDashboardBinding>(
    ActivityDashboardBinding::inflate
) {
    private val authVM: AuthViewModel by viewModel()
    private val reportVM: ReportViewModel by viewModel()
    private val produkVM: ProdukViewModel by viewModel()
    private val tokoVM: TokoViewModel by viewModel()

    private lateinit var navController: NavController

    private var backPressedTime: Long = 0
    private lateinit var toast: Toast

    private val backPressCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val currentTime = System.currentTimeMillis()

            if (currentTime - backPressedTime < 2000) {
                toast.cancel()
                finishAffinity()
            } else {
                backPressedTime = currentTime
                toast = Toast.makeText(
                    this@DashboardActivity,
                    getString(R.string.toast_tap_back_twice),
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }

    }

    override fun setupView() {
        binding.toolbarDashboard.title = getString(R.string.toolbar_dashboard_title)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_dashboard) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        reportVM.getLatestAttendance()
        initObserver()

        initListener()
    }

    private fun initObserver() {
        reportVM.attendance.observe(this) { state ->
            showLoading(state is UiState.Loading)
            when (state) {
                is UiState.Error -> showError(state.message)
                UiState.ErrorConnection -> showError("Error koneksi internet")
                is UiState.Success -> {
                    val bottomNav = binding.bottomNavigation

                    val storeMenu = bottomNav.menu.findItem(R.id.nav_store)
                    val productMenu = bottomNav.menu.findItem(R.id.nav_products)

                    storeMenu.isVisible = state.data
                    productMenu.isVisible = state.data
                }

                else -> {}
            }
        }

        tokoVM.listToko.observe(this) { state ->
            showLoading(state is UiState.Loading)
        }

        produkVM.listProduk.observe(this) { state ->
            showLoading(state is UiState.Loading)
        }

        authVM.logoutState.observe(this) {
            if (it is UiState.Success) {
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    )
                )
                finishAffinity()
            }
        }
    }

    private fun initListener() {
        onBackPressedDispatcher.addCallback(this, backPressCallback)
        binding.toolbarDashboard.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_sign_out -> {
                    logOut()
                    true
                }

                else -> false
            }
        }
    }

    private fun logOut() {
        MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.dialog_logout_message))
            .setNegativeButton(getString(R.string.dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.dialog_yes)) { _, _ ->
                authVM.logOut()
            }
            .show()
            .getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(ContextCompat.getColor(this, R.color.red))
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
