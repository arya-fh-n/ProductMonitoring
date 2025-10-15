package com.arfdevs.productmonitoring.presentation.view.ui.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.FragmentProductListBinding
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.goneIf
import com.arfdevs.productmonitoring.helper.isError
import com.arfdevs.productmonitoring.helper.visible
import com.arfdevs.productmonitoring.presentation.view.adapter.ProductsAdapter
import com.arfdevs.productmonitoring.presentation.view.base.BaseFragment
import com.arfdevs.productmonitoring.presentation.viewmodel.ProdukViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProductListFragment : BaseFragment<FragmentProductListBinding>(
    FragmentProductListBinding::inflate
) {

    private val adapter by lazy {
        ProductsAdapter()
    }

    private val produkVM: ProdukViewModel by activityViewModel()

    override fun initView() = with(binding) {
        tvProductPageTitle.text = getString(R.string.tv_product_page_title)

        context?.let { ctx ->
            rvProducts.layoutManager = LinearLayoutManager(ctx)
        }

        rvProducts.adapter = adapter

        produkVM.getProduk()
        initObservers()
    }

    private fun initObservers() {
        produkVM.listProduk.observe(viewLifecycleOwner) { state ->
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
        tvProductPageTitle.goneIf(isError)
        rvProducts.goneIf(isError)
    }

    private fun showError(message: String) {
        binding.evProduct.apply {
            visible(true)
            setMessage(
                "Error",
                message,
                "Coba Lagi",
            ) {
                produkVM.getProduk()
            }
        }
    }

}