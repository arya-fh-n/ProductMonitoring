package com.arfdevs.productmonitoring.presentation.view.ui.storedetail

import android.content.Intent
import androidx.core.content.IntentCompat
import coil.load
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.ActivityStoreDetailBinding
import com.arfdevs.productmonitoring.domain.model.TokoModel
import com.arfdevs.productmonitoring.helper.Constants
import com.arfdevs.productmonitoring.presentation.view.base.BaseActivity

class StoreDetailActivity : BaseActivity<ActivityStoreDetailBinding>(
    ActivityStoreDetailBinding::inflate
) {

    private val dataToko: TokoModel by lazy {
        IntentCompat.getParcelableExtra(intent, Constants.EXTRA_TOKO, TokoModel::class.java)
            ?: TokoModel()
    }

    override fun setupView() = with(binding) {
        tvStoreDetailCodeTitle.text = getString(R.string.tv_store_detail_code_title)
        tvStoreDetailAlamatTitle.text = getString(R.string.tv_store_detail_alamat_title)

        ivProdukToko.load(R.drawable.ic_store_product)
        ivPromoToko.load(R.drawable.ic_promo)

        tvOpenProdukToko.text = getString(R.string.tv_open_produk_toko)
        tvOpenPromoToko.text = getString(R.string.tv_open_promo_toko)

        initTokoDetail(dataToko)

        initListener()
    }

    private fun initTokoDetail(dataToko: TokoModel) = with(binding) {
        tvStoreDetailName.text = dataToko.namaToko
        tvStoreDetailCode.text = dataToko.kodeToko
        tvStoreDetailAlamat.text = dataToko.alamat
    }

    private fun initListener() {
        binding.mcvProdukToko.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    StoreProductsActivity::class.java
                ).putExtra(Constants.EXTRA_ID_TOKO, dataToko.id)
                    .putExtra(Constants.EXTRA_NAMA_TOKO, dataToko.namaToko)
            )
        }

        binding.mcvPromoToko.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    StorePromoActivity::class.java
                ).putExtra(Constants.EXTRA_ID_TOKO, dataToko.id)
            )
        }
    }

}
