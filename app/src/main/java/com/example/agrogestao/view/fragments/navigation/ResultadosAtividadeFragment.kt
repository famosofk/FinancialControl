package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agrogestao.R
import com.example.agrogestao.models.BalancoPatrimonial
import io.realm.Realm
import io.realm.kotlin.where

/**
 * A simple [Fragment] subclass.
 */
class ResultadosAtividadeFragment : Fragment() {

    private var id = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        id = arguments?.getString("id").toString()
        val root: View = inflater.inflate(R.layout.resultados_atividades_fazenda, container, false)
        load(root)
        return root

    }

    private fun load(root: View) {
        val realm = Realm.getDefaultInstance()
        realm.where<BalancoPatrimonial>().contains("farm", id).findFirst()


    }

}
