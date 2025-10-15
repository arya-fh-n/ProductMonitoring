package com.arfdevs.productmonitoring.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arfdevs.productmonitoring.databinding.ItemStoreBinding
import com.arfdevs.productmonitoring.domain.model.TokoModel

class StoresAdapter : ListAdapter<TokoModel, StoresAdapter.StoreViewHolder>(DIFF_CALLBACK) {

    var listener: StoreListener? = null

    inner class StoreViewHolder(private val binding: ItemStoreBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TokoModel) {
            binding.tvStoreName.text = item.namaToko
            binding.tvStoreCode.text = item.kodeToko
            binding.tvStoreAddress.text = item.alamat

            binding.root.setOnClickListener {
                listener?.onStoreClick(item)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ItemStoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun setListener(listener: StoreListener) {
        this.listener = listener
    }

    interface StoreListener {
        fun onStoreClick(store: TokoModel)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TokoModel>() {
            override fun areItemsTheSame(oldItem: TokoModel, newItem: TokoModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TokoModel, newItem: TokoModel): Boolean {
                return oldItem == newItem
            }

        }
    }

}
