package com.example.agrogestao.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.databinding.ItemListBalancoPatrimonialBinding
import com.example.agrogestao.models.ItemBalancoPatrimonial
import com.example.agrogestao.view.listener.FarmListener

class ItemPatrimonioAdapter :
    ListAdapter<ItemBalancoPatrimonial, ItemPatrimonioAdapter.ViewHolder>(ItemPatrimonioDiff()) {

    private lateinit var clickListener: FarmListener

    class ViewHolder private constructor(
        val binding: ItemListBalancoPatrimonialBinding,
        val listener: FarmListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemBalancoPatrimonial, position: Int) {
            binding.itemBalanco = item
            binding.textView4.text = "${item.quantidadeFinal} un"
            Log.e("valor atual", item.valorAtual)
            Log.e(
                "quantidade:",
                "${item.valorUnitario.toFloat()} ${item.quantidadeFinal} ${item.reforma.toFloat()} ${item.depreciacao.toFloat()}"
            )
            binding.precoItemListagem.text = (item.valorUnitario.toFloat()
                .times(item.quantidadeFinal) + item.reforma.toFloat() - item.depreciacao.toFloat()).toString()
            binding.linearItemListener.setOnClickListener {
                listener.onClick(position)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, listener: FarmListener): ViewHolder {
                val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemListBalancoPatrimonialBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, listener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    fun attachListener(listener: FarmListener) {
        clickListener = listener

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