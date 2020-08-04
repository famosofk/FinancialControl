package com.example.agrogestao.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.databinding.ItemlistProgramaFazendaBinding
import com.example.agrogestao.models.realmclasses.Farm

class SelectFarmAdapter : ListAdapter<Farm, SelectFarmAdapter.ViewHolder>(SelectFarmDiff()) {
    class ViewHolder(val binding: ItemlistProgramaFazendaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(farm: Farm) {
            binding.textProgramaFazenda.text = farm.codigoFazenda
        }

        companion object {
            fun from(context: Context): ViewHolder {
                val inflater = LayoutInflater.from(context)
                val binding: ItemlistProgramaFazendaBinding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.itemlist_programa_fazenda,
                    null,
                    false
                )
                return ViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class SelectFarmDiff : DiffUtil.ItemCallback<Farm>() {

    override fun areItemsTheSame(oldItem: Farm, newItem: Farm): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Farm, newItem: Farm): Boolean {
        return oldItem.codigoFazenda == newItem.codigoFazenda
    }

}