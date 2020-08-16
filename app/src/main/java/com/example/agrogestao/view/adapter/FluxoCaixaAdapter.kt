package com.example.agrogestao.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.databinding.ItemListFluxoCaixaBinding
import com.example.agrogestao.models.ItemFluxoCaixa

class FluxoCaixaAdapter(val onClickListener: ItemFluxoCaixaListener) :
    ListAdapter<ItemFluxoCaixa, FluxoCaixaAdapter.ViewHolder>(FluxoCaixaDiff()) {

    class ViewHolder private constructor(val binding: ItemListFluxoCaixaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemFluxoCaixa, onClickListener: ItemFluxoCaixaListener) {
            binding.fluxoCaixa = item
            binding.clickListener = onClickListener
            if (!item.pagamentoPrazo) {
                if (!item.tipo) {
                    binding.tipoMovimentacao.text = "Venda"
                    binding.tipoMovimentacao.setTextColor(Color.GREEN)
                } else {
                    binding.tipoMovimentacao.text = "Compra"
                    binding.tipoMovimentacao.setTextColor(Color.parseColor("#ff6f00"))
                }
            } else {
                if (!item.tipo) {
                    binding.tipoMovimentacao.text = item.dataPagamentoPrazo
                    binding.tipoMovimentacao.setTextColor(Color.GREEN)
                } else {
                    binding.tipoMovimentacao.text = item.dataPagamentoPrazo
                    binding.tipoMovimentacao.setTextColor(Color.parseColor("#ff6f00"))
                }
            }
            binding.valorMovimentacao.text =
                String.format("%.2f", (item.quantidadeInicial * item.valorInicial.toFloat()))
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding: ItemListFluxoCaixaBinding =
                    DataBindingUtil.inflate(inflater, R.layout.item_list_fluxo_caixa, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }


}


class FluxoCaixaDiff : DiffUtil.ItemCallback<ItemFluxoCaixa>() {
    override fun areItemsTheSame(oldItem: ItemFluxoCaixa, newItem: ItemFluxoCaixa): Boolean {
        return oldItem.itemID == newItem.itemID
    }

    override fun areContentsTheSame(oldItem: ItemFluxoCaixa, newItem: ItemFluxoCaixa): Boolean {
        return true
    }

}

class ItemFluxoCaixaListener(val clickListener: (itemId: String) -> Unit) {
    fun onClick(item: ItemFluxoCaixa) = clickListener(item.itemID)
}