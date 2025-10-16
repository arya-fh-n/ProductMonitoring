package com.arfdevs.productmonitoring.presentation.view.ui.storedetail

import com.arfdevs.productmonitoring.databinding.ActivityStorePromoBinding
import com.arfdevs.productmonitoring.helper.Promo
import com.arfdevs.productmonitoring.presentation.view.base.BaseActivity
import com.arfdevs.productmonitoring.presentation.viewmodel.ReportViewModel
import com.arfdevs.productmonitoring.presentation.viewmodel.TokoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StorePromoActivity : BaseActivity<ActivityStorePromoBinding>(
    ActivityStorePromoBinding::inflate
) {

    private val tokoVM: TokoViewModel by viewModel()
    private val reportVM: ReportViewModel by viewModel()

    private val idToko: Int by lazy {
        intent.getIntExtra(Promo.ID_TOKO, 0)
    }

    private val namaToko: String by lazy {
        intent.getStringExtra(Promo.NAMA_TOKO) ?: ""
    }

    override fun setupView() {

    }

}
