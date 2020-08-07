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
import com.example.agrogestao.view.listener.FarmListener


class ProgramaAdapter : ListAdapter<FarmProgram, ProgramaAdapter.Viewholder>(ProgramDiff()) {

    private lateinit var listener: FarmListener

    class Viewholder private constructor(
        val binding: ItemlistProgramaFazendaBinding,
        val mlistener: FarmListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FarmProgram, position: Int) {

            binding.textProgramaFazenda.text = item.name
            binding.programaLinearLayout.setOnClickListener {
                mlistener.onClick(position)
            }


        }

        companion object {
            fun from(context: Context, listener: FarmListener): Viewholder {
                val layoutInflater = LayoutInflater.from(context)
                val binding: ItemlistProgramaFazendaBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.itemlist_programa_fazenda,
                    null,
                    false
                )
                return Viewholder(binding, listener)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder.from(parent.context, listener)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun attachToAdapter(lis: FarmListener) {
        listener = lis
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