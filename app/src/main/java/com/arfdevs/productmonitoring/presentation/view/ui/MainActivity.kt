package com.arfdevs.productmonitoring.presentation.view.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.ActivityMainBinding
import com.arfdevs.productmonitoring.helper.Constants
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.presentation.view.base.BaseActivity
import com.arfdevs.productmonitoring.presentation.viewmodel.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val authVM: AuthViewModel by viewModel()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    override fun setupView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        initObserver()
        authVM.getCurrentUser()
    }

    private fun initObserver() {
        authVM.session.observe(this) { state ->
            if (navController.currentDestination?.id != R.id.splashFragment) return@observe
            when (state) {
                is UiState.Success -> navigateToDashboard()
                UiState.Loading -> {}
                is UiState.Error -> {
                    if (state.errorCode != Constants.SILENT_NAV_CODE) {
                        Snackbar.make(
                            binding.root,
                            state.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    navigateToLogin()
                }

                else -> navigateToLogin()
            }
        }
    }

    private fun navigateToDashboard() {
        lifecycleScope.launch {
            delay(3000L)
            navController.navigate(R.id.action_splashFragment_to_dashboardActivity)
            finish()
        }
    }

    private fun navigateToLogin() {
        lifecycleScope.launch {
            delay(3000L)
            navController.navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }

}
