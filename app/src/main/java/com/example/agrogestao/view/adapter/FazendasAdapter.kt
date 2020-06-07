package com.example.agrogestao.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.Farm
import com.example.agrogestao.view.viewholder.FarmsViewHolder

class FazendasAdapter : RecyclerView.Adapter<FarmsViewHolder>() {

    private var myFarmList: List<Farm> = arrayListOf()


    fun updateFarms(list: List<Farm>) {
        myFarmList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmsViewHolder {

        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fazenda_listagem, parent, false)
        return FarmsViewHolder(item)
    }

    override fun getItemCount(): Int {
        return myFarmList.count()
    }

    override fun onBindViewHolder(holder: FarmsViewHolder, position: Int) {
        holder.bind(myFarmList[position])
    }

}