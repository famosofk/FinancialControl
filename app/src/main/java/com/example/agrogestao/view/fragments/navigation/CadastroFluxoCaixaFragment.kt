package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.agrogestao.R
import com.example.agrogestao.models.FluxoCaixa
import com.example.agrogestao.models.RegistradorFarm
import io.realm.Realm
import io.realm.kotlin.where

/**
 * A simple [Fragment] subclass.
 */
class CadastroFluxoCaixaFragment : Fragment() {
    private lateinit var root: View
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.cadastro_fluxo_caixa, container, false)
        realm = Realm.getDefaultInstance()
        listenersAndVisibility()

        return root
    }

    private fun recuperarFluxoCaixa() {
        val registradorFarm = realm.where<RegistradorFarm>().findFirst()!!
        var fluxoCaixa = realm.where<FluxoCaixa>().contains("farm", registradorFarm.id)

    }

    private fun listenersAndVisibility() {

        val switch: Switch = root.findViewById(R.id.switchPrazo);
        switch.setOnClickListener {
            val layoutVencimento: LinearLayout = root.findViewById(R.id.layoutVencimento)
            if (switch.isChecked) {
                layoutVencimento.visibility = View.VISIBLE
            } else {
                layoutVencimento.visibility = View.GONE
            }
        }
    }

}
