package com.arfdevs.productmonitoring.presentation.view.ui.storedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import com.arfdevs.productmonitoring.R
import com.arfdevs.productmonitoring.databinding.BottomSheetProductReportBinding
import com.arfdevs.productmonitoring.domain.model.ProdukModel
import com.arfdevs.productmonitoring.helper.BS
import com.arfdevs.productmonitoring.helper.UiState
import com.arfdevs.productmonitoring.helper.enabled
import com.arfdevs.productmonitoring.helper.orZero
import com.arfdevs.productmonitoring.presentation.viewmodel.ReportViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProductReportBottomSheet : BottomSheetDialogFragment() {

    private val reportVM: ReportViewModel by activityViewModel()

    private var produk: ProdukModel? = null
    private var idToko: Int = -1

    // init checkbox state
    private var isCurrentlyAvailable: Boolean = false

    // product availability state (checked/unchecked)
    private var isNewAvailability: Boolean = false

    private lateinit var binding: BottomSheetProductReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetProductReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            produk =
                BundleCompat.getParcelable(it, BS.BS_PRODUK, ProdukModel::class.java)
                    ?: ProdukModel()
            idToko = it.getInt(BS.BS_ID_TOKO)
            isCurrentlyAvailable = it.getBoolean(BS.BS_IS_AVAILABLE)
            isNewAvailability = isCurrentlyAvailable
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvBsProductTitle.text = getString(R.string.tv_store_products_title)
            tvBsProductName.text = produk?.namaProduk.orEmpty()

            cbProductAvailability.text = getString(R.string.cb_store_products_bs)
            cbProductAvailability.isChecked = isCurrentlyAvailable

            btnBsConfirmReport.text = getString(R.string.btn_konfirmasi)
            btnBsConfirmReport.enabled(false)
        }

        initObserver()
        initListener()
    }

    private fun initObserver() {
        reportVM.reportProduct.observe(viewLifecycleOwner) {
            handleReportResult(it, false)
        }

        reportVM.removeProductFromStore.observe(viewLifecycleOwner) {
            handleReportResult(it, true)
        }
    }

    private fun initListener() {
        binding.cbProductAvailability.setOnCheckedChangeListener { _, isChecked ->
            isNewAvailability = isChecked
            // only enable confirmation button if state has changed
            binding.btnBsConfirmReport.enabled(isNewAvailability != isCurrentlyAvailable)
        }

        binding.btnBsConfirmReport.setOnClickListener {
            if (idToko != -1 && produk != null) {
                if (isNewAvailability) {
                    // user add product to store (was false, now true)
                    reportVM.reportProduct(idToko, produk?.id.orZero())
                } else {
                    // user remove product from store (was true, now false)
                    reportVM.removeProductFromStore(idToko, produk?.id.orZero())
                }
            }
        }
    }

    private fun handleReportResult(uiState: UiState<Unit>, isRemove: Boolean) {
        when (uiState) {
            is UiState.Loading -> binding.btnBsConfirmReport.enabled(false)
            is UiState.Success -> {
                val message = if (isRemove) getString(R.string.toast_remove_product)
                else getString(R.string.toast_add_product)
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_LONG
                ).show()

                if (isRemove) {
                    reportVM.clearRemoveProductState()
                } else {
                    reportVM.clearReportProductState()
                }
                (activity as? StoreProductsActivity)?.refreshStoreProductsList()
                dismiss()
            }

            is UiState.Error -> {
                Toast.makeText(requireContext(), "Error: ${uiState.message}", Toast.LENGTH_LONG)
                    .show()
                binding.btnBsConfirmReport.enabled(true)
            }

            else -> {}
        }
    }

    companion object {
        const val TAG = "ProductReportBottomSheet"

        fun newInstance(
            produk: ProdukModel,
            idToko: Int,
            isAvailable: Boolean
        ) = ProductReportBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(BS.BS_PRODUK, produk)
                putInt(BS.BS_ID_TOKO, idToko)
                putBoolean(BS.BS_IS_AVAILABLE, isAvailable)
            }
        }
    }
}