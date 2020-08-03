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
import com.example.agrogestao.models.realmclasses.FarmProgram


class ProgramaAdapter : ListAdapter<FarmProgram, ProgramaAdapter.Viewholder>(ProgramDiff()) {

    class Viewholder private constructor(val binding: ItemlistProgramaFazendaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FarmProgram) {
            binding.textProgramaFazenda.text = item.name
        }

        companion object {
            fun from(context: Context): Viewholder {
                val layoutInflater = LayoutInflater.from(context)
                val binding: ItemlistProgramaFazendaBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.itemlist_programa_fazenda,
                    null,
                    false
                )
                return Viewholder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder.from(parent.context)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ProgramDiff : DiffUtil.ItemCallback<FarmProgram>() {

    override fun areItemsTheSame(oldItem: FarmProgram, newItem: FarmProgram): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: FarmProgram, newItem: FarmProgram): Boolean {
        return oldItem.name == newItem.name
    }

}