package com.arfdevs.productmonitoring.presentation.view.ui.splash

import coil.load
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.FragmentSplashBinding
import com.arfdevs.productmonitoring.presentation.view.base.BaseFragment

class SplashFragment : BaseFragment<FragmentSplashBinding>(
    FragmentSplashBinding::inflate
) {

    override fun initView() {
        binding.ivSplash.load(R.drawable.splash_icon)
    }

}
