package com.arfdevs.productmonitoring.presentation.view.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arfdevs.productmonitoring.databinding.ItemProductBinding
import com.arfdevs.productmonitoring.domain.model.ProdukTokoModel
import com.arfdevs.productmonitoring.helper.toRupiahFormat
import com.arfdevs.productmonitoring.helper.visible

class StoreProductsAdapter :
    ListAdapter<ProdukTokoModel, StoreProductsAdapter.ProdukViewHolder>(DIFF_CALLBACK) {

    var storeProductsListener: StoreProductsListener? = null

    inner class ProdukViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProdukTokoModel) = with(binding) {
            tvProductName.text = item.namaProduk
            tvProductPrice.text = item.harga.toLong().toRupiahFormat()
            tvProductBarcode.text = item.barcode

            tvProductPromo.visible(item.hargaPromo > 0)
            if (item.hargaPromo > 0) {
                tvProductPromo.text = item.hargaPromo.toLong().toRupiahFormat()
                tvProductPrice.paintFlags = tvProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }

            root.setOnClickListener {
                storeProductsListener?.onStoreProductClick(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProdukViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun setListener(listener: StoreProductsListener) {
        storeProductsListener = listener
    }

    interface StoreProductsListener {
        fun onStoreProductClick(produk: ProdukTokoModel)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProdukTokoModel>() {
            override fun areItemsTheSame(
                oldItem: ProdukTokoModel,
                newItem: ProdukTokoModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProdukTokoModel,
                newItem: ProdukTokoModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}
