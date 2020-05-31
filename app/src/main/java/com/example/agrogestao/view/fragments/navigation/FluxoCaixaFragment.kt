package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agrogestao.R
import com.example.agrogestao.viewmodel.FluxoCaixaViewModel

class FluxoCaixaFragment : Fragment() {

    private lateinit var fluxoCaixaViewModel: FluxoCaixaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fluxoCaixaViewModel =
            ViewModelProvider(this).get(FluxoCaixaViewModel::class.java)
        val root = inflater.inflate(R.layout.fluxo_caixa_atividade, container, false)

        val tittle = root.findViewById<TextView>(R.id.fluxoCaixaText)
        //fazer a adequação do nome

        observe(root)
        return root
    }


    private fun observe(view: View) {

        //fazer como se fosse da fazenda
        fluxoCaixaViewModel.myBalancoPatrimonial.observe(viewLifecycleOwner, Observer {
            val saldo = view.findViewById<TextView>(R.id.fcaixaSaldoText)
            saldo.setText(it.saldo.toString())
            val receitas = view.findViewById<TextView>(R.id.fcaixaReceitasText)
            receitas.setText(it.totalReceitas.toString())
            val despesas = view.findViewById<TextView>(R.id.fcaixaDespesasText)
            despesas.setText(it.totalDespesas.toString())
            val pagar = view.findViewById<TextView>(R.id.fcaixaPagarText)
            pagar.setText(it.totalContasPagar.toString())
            val receber = view.findViewById<TextView>(R.id.fcaixaReceberText)
            receber.setText(it.totalContasReceber.toString())
            val lucro = view.findViewById<TextView>(R.id.fcaixaLucroText)
            lucro.setText(it.lucro.toString())

            //se for da atividade temos que filtrar só da atividade em questão
        })

    }

}
