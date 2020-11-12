package com.example.agrogestao

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.agrogestao.models.ItemFluxoCaixa
import com.example.agrogestao.models.RegistradorFarm
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import com.example.agrogestao.models.realmclasses.FluxoCaixa
import io.realm.Realm
import io.realm.kotlin.where

class PendenciasFragment : Fragment() {

    var selectedDay = ""
    var selectedMonth = ""
    var selectedYear = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pendencias, container, false)

        val createButton : Button = view.findViewById(R.id.cadastrarPendencia)
        val nomePendencia : EditText = view.findViewById(R.id.nomeItemPendencia)
        val valorPendencia : EditText = view.findViewById(R.id.valorUnitarioPendencia)
        val entrada : ToggleButton = view.findViewById(R.id.entradaSaidaPendencia)

        val calendar: CalendarView = view.findViewById(R.id.dataVencimentoPendencia)
        calendar.setOnDateChangeListener { _, year, month, day ->
            selectedDay = day.toString()
            selectedMonth = (month + 1).toString()
            selectedYear = year.toString()
        }

        createButton.setOnClickListener {
            val item = ItemFluxoCaixa()
            item.nome = nomePendencia.text.toString()
            item.valorAtual = valorPendencia.text.toString()
            item.currentYear = false
            item.quantidadeInicial = 1
            item.valorInicial = item.valorAtual
            item.tipo = entrada.isChecked
            item.dataPagamentoPrazo = "$selectedDay / $selectedMonth / $selectedYear"
            val realm = Realm.getDefaultInstance()
            val registradorFarm = realm.where<RegistradorFarm>().findFirst()!!
            val fluxoCaixa =
                realm.where<FluxoCaixa>().contains("farmID", registradorFarm.id).findFirst()!!
            val balanco = realm.where<BalancoPatrimonial>().contains("farmID", registradorFarm.id)
                .findFirst()!!
            realm.beginTransaction()
            fluxoCaixa.list.add(item)
            if (item.tipo)
                balanco.pendenciasPagamento =
                    (balanco.pendenciasPagamento.toDouble() + item.valorAtual.toDouble()).toString()
            else balanco.pendenciasRecebimento =
                (balanco.pendenciasRecebimento.toDouble() + item.valorAtual.toDouble()).toString()
            balanco.atualizarBalanco()
            realm.commitTransaction()
            view.findNavController().navigate(R.id.action_pendenciasFragment_to_nav_fazenda)


        }

        return view
    }

}