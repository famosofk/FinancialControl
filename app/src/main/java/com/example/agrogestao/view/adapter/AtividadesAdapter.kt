package com.example.agrogestao.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.databinding.AtividadesListagemBinding
import com.example.agrogestao.models.AtividadesEconomicas


class AtividadesAdapter :
    ListAdapter<AtividadesEconomicas, AtividadesAdapter.ViewHolder>(AtividadesDiff()) {
    class ViewHolder(val binding: AtividadesListagemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: AtividadesEconomicas) {
            binding.atividade = item
            binding.lucroListagem.text = "${item.rateio}%"
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = AtividadesListagemBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }


}


class AtividadesDiff : DiffUtil.ItemCallback<AtividadesEconomicas>() {
    override fun areItemsTheSame(
        oldItem: AtividadesEconomicas,
        newItem: AtividadesEconomicas
    ): Boolean {
        return oldItem.nome == newItem.nome
    }

    override fun areContentsTheSame(
        oldItem: AtividadesEconomicas,
        newItem: AtividadesEconomicas
    ): Boolean {
        return (oldItem.rateio == newItem.rateio && oldItem.nome == newItem.nome)
    }

}