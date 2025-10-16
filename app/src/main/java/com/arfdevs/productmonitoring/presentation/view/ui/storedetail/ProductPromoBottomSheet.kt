package com.arfdevs.productmonitoring.presentation.view.ui.storedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.core.widget.addTextChangedListener
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.BottomSheetProductPromoBinding
import com.arfdevs.productmonitoring.domain.model.ProdukTokoModel
import com.arfdevs.productmonitoring.helper.BS
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.enabled
import com.arfdevs.productmonitoring.helper.orZero
import com.arfdevs.productmonitoring.presentation.viewmodel.ReportViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductPromoBottomSheet : BottomSheetDialogFragment() {

    private val reportVM: ReportViewModel by viewModel()

    private var produkToko: ProdukTokoModel? = null
    private var idToko: Int = -1

    private var originalPromoPrice: Int = 0

    private lateinit var binding: BottomSheetProductPromoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetProductPromoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            produkToko =
                BundleCompat.getParcelable(it, BS.BS_PRODUK_TOKO, ProdukTokoModel::class.java)
            idToko = it.getInt(BS.BS_PRODUK_TOKO_ID_TOKO)

            originalPromoPrice = produkToko?.hargaPromo.orZero()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvBsPromoTitle.text = getString(R.string.tv_store_promo_bs_title)
            tiPromoPrice.hint = getString(R.string.tv_promo_price_hint)
            btnBsConfirmPromo.text = getString(R.string.btn_konfirmasi)

            tvBsPromoProductName.text = produkToko?.namaProduk.orEmpty()
            etPromoPrice.setText(
                if (originalPromoPrice > 0) originalPromoPrice.toString() else ""
            )

            btnBsConfirmPromo.enabled(false)
        }

        initObserver()
        initListener()
    }

    private fun initObserver() {
        reportVM.reportPromo.observe(viewLifecycleOwner) {
            handleReportResult(it, false)
        }

        reportVM.removePromo.observe(viewLifecycleOwner) {
            handleReportResult(it, true)
        }
    }

    private fun initListener() = with(binding) {
        etPromoPrice.addTextChangedListener {
            checkPromoPriceDifference(it.toString())
        }

        btnBsConfirmPromo.setOnClickListener {
            handlePromo()
        }
    }

    private fun checkPromoPriceDifference(promoPrice: String) {
        val newPromoPrice = promoPrice.trim().toIntOrNull() ?: 0
        val isPriceChanged = newPromoPrice != originalPromoPrice

        binding.btnBsConfirmPromo.enabled(isPriceChanged)
    }

    private fun handlePromo() {
        val idProduk = produkToko?.idProduk.orZero()
        if (idToko == -1 || idProduk == 0) return

        val inputPriceText = binding.etPromoPrice.text.toString().trim()
        val inputPromoPrice = inputPriceText.toIntOrNull() ?: 0

        if (inputPromoPrice == 0 || inputPriceText.isBlank()) {
            // check if there was a promo to remove in the first place
            if (originalPromoPrice > 0) {
                reportVM.removePromo(idToko, idProduk)
            } else {
                // no promo to remove and no new promo set
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_no_promo_changes), Toast.LENGTH_SHORT
                ).show()
                dismiss()
            }
        } else {
            reportVM.reportPromo(idToko, idProduk, inputPromoPrice)
        }
    }

    private fun handleReportResult(
        uiState: UiState<Unit>,
        isRemove: Boolean
    ) {
        when (uiState) {
            is UiState.Loading -> binding.btnBsConfirmPromo.enabled(false)
            is UiState.Success -> {
                val message =
                    if (isRemove) getString(R.string.toast_promo_remove_success) else getString(R.string.toast_promo_added_success)
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                if (isRemove) reportVM.clearRemovePromoState() else reportVM.clearReportPromoState()

                (activity as? StorePromoActivity)?.refreshStoreProductsList()
                dismiss()
            }

            is UiState.Error -> {
                Toast.makeText(requireContext(), "Error: ${uiState.message}", Toast.LENGTH_LONG)
                    .show()
                binding.btnBsConfirmPromo.enabled(true)
            }

            else -> {}
        }
    }

    companion object {
        const val TAG = "PromoReportBottomSheet"

        fun newInstance(
            productStoreModel: ProdukTokoModel,
            idToko: Int
        ) = ProductPromoBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(BS.BS_PRODUK_TOKO, productStoreModel)
                putInt(BS.BS_PRODUK_TOKO_ID_TOKO, idToko)
            }
        }
    }

}
