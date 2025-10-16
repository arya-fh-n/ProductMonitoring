package com.arfdevs.productmonitoring.presentation.view.ui.storedetail

import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.ActivityStoreProductsBinding
import com.arfdevs.productmonitoring.domain.model.ProdukModel
import com.arfdevs.productmonitoring.helper.ProdukToko
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.presentation.view.adapter.ProductsAdapter
import com.arfdevs.productmonitoring.presentation.view.base.BaseActivity
import com.arfdevs.productmonitoring.presentation.viewmodel.ProdukViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.ReportViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.TokoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StoreProductsActivity : BaseActivity<ActivityStoreProductsBinding>(
    ActivityStoreProductsBinding::inflate
), ProductsAdapter.ProductsListener {

    private val tokoVM: TokoViewModel by viewModel()
    private val produkVM: ProdukViewModel by viewModel()
    private val reportVM: ReportViewModel by viewModel()

    private var availableProdukIds: Set<Int> = emptySet()

    private val adapter: ProductsAdapter by lazy {
        ProductsAdapter()
    }

    private val idToko: Int by lazy {
        intent.getIntExtra(ProdukToko.ID_TOKO, 0)
    }

    private val namaToko: String by lazy {
        intent.getStringExtra(ProdukToko.NAMA_TOKO) ?: ""
    }

    override fun setupView() = with(binding) {
        tvStoreProductsTitle.text = getString(R.string.tv_store_products_title, namaToko)
        adapter.setListener(this@StoreProductsActivity)
        rvStoreProducts.adapter = adapter

        produkVM.getProduk()
        tokoVM.getProdukToko(idToko)

        initObserver()
    }

    private fun initObserver() {
        produkVM.listProduk.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    adapter.submitList(state.data)
                }

                else -> {}
            }
        }

        tokoVM.listProdukToko.observe(this) { state ->
            when (state) {
                is UiState.Success -> {
                    availableProdukIds = state.data.map { it.idProduk }.toSet()
                }

                else -> {}
            }
        }
    }

    override fun onStoreProductClick(produk: ProdukModel) {
        showReportBottomSheet(produk)
    }

    private fun showReportBottomSheet(produk: ProdukModel) {
        val isBottomSheetShown = supportFragmentManager.findFragmentByTag(
            ProductReportBottomSheet.TAG
        )?.isAdded == true

        if (isBottomSheetShown) {
            return
        }

        val isAvailable = availableProdukIds.contains(produk.id)

        ProductReportBottomSheet.newInstance(
            produk = produk,
            idToko = idToko,
            isAvailable = isAvailable
        ).show(supportFragmentManager, ProductReportBottomSheet.TAG)
    }

    fun refreshStoreProductsList() {
        tokoVM.getProdukToko(idToko)
        produkVM.getProduk()
    }

}
