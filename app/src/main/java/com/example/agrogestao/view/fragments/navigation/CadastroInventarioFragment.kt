package com.example.agrogestao.view.fragments.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.agrogestao.R

/**
 * A simple [Fragment] subclass.
 */
class CadastroInventarioFragment : Fragment() {

    private lateinit var list: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.cadastro_inventario, container, false)

        inicializarSpinners(root, context)

        return root
    }

    private fun inicializarSpinners(root: View, con: Context?) {
        list = mutableListOf()

        val atividadeAdapter =
            ArrayAdapter(con!!, android.R.layout.simple_spinner_dropdown_item, list)
        val atividadeSpinner = root.findViewById<Spinner>(R.id.spinnerAtividadeItem)
        atividadeSpinner.adapter = atividadeAdapter


    }


}
