package com.example.agrogestao.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.Farm
import com.example.agrogestao.view.listener.FarmListener
import com.example.agrogestao.view.viewholder.FarmsViewHolder

class FazendasAdapter : RecyclerView.Adapter<FarmsViewHolder>() {

    private var myFarmList: List<Farm> = arrayListOf()
    private lateinit var mListener: FarmListener


    fun updateFarms(list: List<Farm>) {
        myFarmList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmsViewHolder {

        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fazenda_listagem, parent, false)
        return FarmsViewHolder(item, mListener)
    }

    override fun getItemCount(): Int {
        return myFarmList.count()
    }

    override fun onBindViewHolder(holder: FarmsViewHolder, position: Int) {
        holder.bind(myFarmList[position], position)
    }

    fun attachListener(listener: FarmListener) {
        mListener = listener
    }

    fun get(position: Int): Farm {
        return myFarmList.get(position)
    }

}