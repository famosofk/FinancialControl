package com.example.agrogestao.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.databinding.ItemlistProgramaFazendaBinding
import com.example.agrogestao.models.realmclasses.Farm
import com.example.agrogestao.view.listener.FarmListener

class SelectFarmAdapter : ListAdapter<Farm, SelectFarmAdapter.ViewHolder>(SelectFarmDiff()) {
    private lateinit var mlistener: FarmListener

    class ViewHolder(val binding: ItemlistProgramaFazendaBinding, val listener: FarmListener) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(farm: Farm, position: Int) {
            binding.textProgramaFazenda.text = farm.codigoFazenda
            binding.programaLinearLayout.setOnClickListener {
                listener.onClick(position)
            }

        }

        companion object {
            fun from(parent: ViewGroup, listener: FarmListener): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemlistProgramaFazendaBinding.inflate(
                    inflater,
                    parent,
                    false
                )
                return ViewHolder(binding, listener)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mlistener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun attachListener(listener: FarmListener) {
        mlistener = listener

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