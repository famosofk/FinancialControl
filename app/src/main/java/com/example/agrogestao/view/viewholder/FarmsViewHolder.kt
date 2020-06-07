package com.example.agrogestao.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.Farm

class FarmsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(farm: Farm) {
        val fazendaText: TextView = itemView.findViewById(R.id.farmTextViewListagem)
        val programaText: TextView = itemView.findViewById(R.id.programaTextViewListagem)

        fazendaText.setText(farm.codigoFazenda)
        fazendaText.setText(farm.programa)
    }


}