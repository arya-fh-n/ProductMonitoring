package com.arfdevs.productmonitoring.presentation.view.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.arfdevs.productmonitoring.databinding.ActivityMainBinding
import com.arfdevs.productmonitoring.presentation.view.base.BaseActivity
import com.arfdevs.productmonitoring.presentation.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val authVM: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    override fun setupView() {}

}
