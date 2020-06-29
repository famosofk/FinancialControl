package com.example.agrogestao.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.databinding.ItemListBalancoPatrimonialBinding
import com.example.agrogestao.models.ItemBalancoPatrimonial

class ItemPatrimonioAdapter :
    ListAdapter<ItemBalancoPatrimonial, ItemPatrimonioAdapter.ViewHolder>(ItemPatrimonioDiff()) {

    class ViewHolder private constructor(val binding: ItemListBalancoPatrimonialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemBalancoPatrimonial) {
            binding.itemBalanco = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemListBalancoPatrimonialBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}


class ItemPatrimonioDiff : DiffUtil.ItemCallback<ItemBalancoPatrimonial>() {
    override fun areItemsTheSame(
        oldItem: ItemBalancoPatrimonial,
        newItem: ItemBalancoPatrimonial
    ): Boolean {
        return oldItem.idItem == newItem.idItem
    }

    override fun areContentsTheSame(
        oldItem: ItemBalancoPatrimonial,
        newItem: ItemBalancoPatrimonial
    ): Boolean {
        return oldItem.nome == newItem.nome
    }


}