package com.arfdevs.productmonitoring.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arfdevs.productmonitoring.databinding.ItemProductBinding
import com.arfdevs.productmonitoring.domain.model.ProdukModel
import com.arfdevs.productmonitoring.helper.toRupiahFormat

class ProductsAdapter : ListAdapter<ProdukModel, ProductsAdapter.ProdukViewHolder>(DIFF_CALLBACK) {

    var productsListener: ProductsListener? = null

    inner class ProdukViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProdukModel) {
            binding.tvProductName.text = item.namaProduk
            binding.tvProductPrice.text = item.harga.toLong().toRupiahFormat()
            binding.tvProductBarcode.text = item.barcode

            binding.root.setOnClickListener {
                productsListener?.onStoreProductClick(item)
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

    fun setListener(listener: ProductsListener) {
        productsListener = listener
    }

    interface ProductsListener {
        fun onStoreProductClick(produk: ProdukModel)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProdukModel>() {
            override fun areItemsTheSame(oldItem: ProdukModel, newItem: ProdukModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProdukModel, newItem: ProdukModel): Boolean {
                return oldItem == newItem
            }

        }
    }

}
