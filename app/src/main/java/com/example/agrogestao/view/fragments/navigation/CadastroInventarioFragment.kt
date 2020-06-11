package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.agrogestao.R
import com.example.agrogestao.models.AtividadesEconomicas
import io.realm.Realm
import io.realm.kotlin.where

/**
 * A simple [Fragment] subclass.
 */
class CadastroInventarioFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.cadastro_inventario, container, false)
        val realm = Realm.getDefaultInstance()
        val resultados = realm.where<AtividadesEconomicas>().findAll()
        val list = mutableListOf<String>()
        for (atividade in resultados) {
            list.add(atividade.nome)
        }
        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )
        }

        val spinner: Spinner = root.findViewById(R.id.spinnerAtividadeItem)
        spinner.adapter = adapter

        return root
    }




}
