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
            saldo.text = "Saldo: ${it.saldo}"
            val receitas = view.findViewById<TextView>(R.id.fcaixaReceitasText)
            receitas.text = "Receitas: ${it.totalReceitas}"
            val despesas = view.findViewById<TextView>(R.id.fcaixaDespesasText)
            despesas.text = "Despesas: ${it.totalDespesas}"
            val pagar = view.findViewById<TextView>(R.id.fcaixaPagarText)
            pagar.text = "Contas a pagar: ${it.totalContasReceber}"
            val receber = view.findViewById<TextView>(R.id.fcaixaReceberText)
            receber.text = "Contas a receber: ${it.totalContasReceber}"
            val lucro = view.findViewById<TextView>(R.id.fcaixaLucroText)
            lucro.text = "Lucro: ${it.lucro}"

            //se for da atividade temos que filtrar só da atividade em questão
        })

    }

}
