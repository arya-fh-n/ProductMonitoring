package com.arfdevs.productmonitoring.presentation.view.ui.home

import android.content.Intent
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.FragmentStoreListBinding
import com.arfdevs.productmonitoring.domain.model.TokoModel
import com.arfdevs.productmonitoring.helper.Constants
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.goneIf
import com.arfdevs.productmonitoring.helper.isError
import com.arfdevs.productmonitoring.helper.visible
import com.arfdevs.productmonitoring.presentation.view.adapter.StoresAdapter
import com.arfdevs.productmonitoring.presentation.view.base.BaseFragment
import com.arfdevs.productmonitoring.presentation.view.ui.storedetail.StoreDetailActivity
import com.arfdevs.productmonitoring.presentation.viewmodel.TokoViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class StoreListFragment : BaseFragment<FragmentStoreListBinding>(
    FragmentStoreListBinding::inflate
), StoresAdapter.StoreListener {

    private val adapter by lazy {
        StoresAdapter()
    }

    private val tokoVM: TokoViewModel by activityViewModel()

    override fun initView() = with(binding) {
        tvStorePageTitle.text = getString(R.string.tv_store_page_title)
        adapter.setListener(this@StoreListFragment)
        rvStores.adapter = adapter

        tokoVM.getToko()
        initObservers()
    }

    private fun initObservers() {
        tokoVM.listToko.observe(viewLifecycleOwner) { state ->
            hideContent(state.isError(true))
            when (state) {
                is UiState.Success -> adapter.submitList(state.data)
                is UiState.Error -> showError(state.message)
                UiState.ErrorConnection -> showError("Error koneksi internet")
                UiState.Empty -> showError("Tidak ada data toko")
                else -> {}
            }
        }
    }

    private fun hideContent(isError: Boolean) = with(binding) {
        tvStorePageTitle.goneIf(isError)
        rvStores.goneIf(isError)
    }

    private fun showError(message: String) {
        binding.evStore.apply {
            visible(true)
            setMessage(
                "Error",
                message,
                "Coba Lagi",
            ) {
                tokoVM.getToko()
            }
        }
    }

    override fun onStoreClick(store: TokoModel) {
        context?.let { ctx ->
            Intent(ctx, StoreDetailActivity::class.java).also {
                it.putExtra(Constants.DATA_TOKO, store)
                startActivity(it)
            }
        }
    }

}
