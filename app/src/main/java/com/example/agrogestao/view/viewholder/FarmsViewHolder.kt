package com.example.agrogestao.view.viewholder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.Farm
import com.example.agrogestao.view.listener.FarmListener

class FarmsViewHolder(view: View, private val listener: FarmListener) :
    RecyclerView.ViewHolder(view) {

    fun bind(farm: Farm, position: Int) {

        val fazendaText: TextView = itemView.findViewById(R.id.farmTextViewListagem)
        val programaText: TextView = itemView.findViewById(R.id.programaTextViewListagem)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.layoutListagemFazenda)

        fazendaText.setText(farm.codigoFazenda)
        programaText.setText(farm.programa)

        linearLayout.setOnClickListener {
            listener.onClick(position)

        }


    }


}