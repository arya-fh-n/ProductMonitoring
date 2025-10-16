package com.arfdevs.productmonitoring.presentation.view.ui.storedetail

import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.ActivityStorePromoBinding
import com.arfdevs.productmonitoring.domain.model.ProdukTokoModel
import com.arfdevs.productmonitoring.helper.Promo
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.presentation.view.adapter.StoreProductsAdapter
import com.arfdevs.productmonitoring.presentation.view.base.BaseActivity
import com.arfdevs.productmonitoring.presentation.viewmodel.ReportViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.TokoViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class StorePromoActivity : BaseActivity<ActivityStorePromoBinding>(
    ActivityStorePromoBinding::inflate
), StoreProductsAdapter.StoreProductsListener {

    private val tokoVM: TokoViewModel by viewModel()
    private val reportVM: ReportViewModel by viewModel()

    private val adapter: StoreProductsAdapter by lazy {
        StoreProductsAdapter()
    }

    private val idToko: Int by lazy {
        intent.getIntExtra(Promo.ID_TOKO, 0)
    }

    private val namaToko: String by lazy {
        intent.getStringExtra(Promo.NAMA_TOKO) ?: ""
    }

    override fun setupView() = with(binding) {
        tvStorePromoTitle.text = getString(R.string.tv_store_promo_title, namaToko)

        rvStorePromo.adapter = adapter
        adapter.setListener(this@StorePromoActivity)

        if (idToko != 0) {
            tokoVM.getProdukToko(idToko)
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.snackbar_error_store_id_missing),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        initObserver()
    }

    private fun initObserver() {
        tokoVM.listProdukToko.observe(this) { uiState ->
            when (uiState) {
                is UiState.Success -> adapter.submitList(uiState.data)
                else -> {}
            }
        }
    }

    override fun onStoreProductClick(produk: ProdukTokoModel) {
        showPromoReportBottomSheet(produk)
    }

    private fun showPromoReportBottomSheet(produkTokoModel: ProdukTokoModel) {
        val isBottomSheetShown = supportFragmentManager.findFragmentByTag(
            ProductPromoBottomSheet.TAG
        )?.isAdded == true

        if (isBottomSheetShown) {
            return
        }

        ProductPromoBottomSheet.newInstance(
            productStoreModel = produkTokoModel,
            idToko = idToko
        ).show(supportFragmentManager, ProductPromoBottomSheet.TAG)
    }

    fun refreshStoreProductsList() {
        tokoVM.getProdukToko(idToko)
    }

}
