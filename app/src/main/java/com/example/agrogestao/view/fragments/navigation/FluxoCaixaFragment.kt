package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.ItemFluxoCaixa
import com.example.agrogestao.view.adapter.FluxoCaixaAdapter
import com.example.agrogestao.viewmodel.FluxoCaixaViewModel
import io.realm.Realm
import io.realm.kotlin.where

class FluxoCaixaFragment : Fragment() {

    private lateinit var fluxoCaixaViewModel: FluxoCaixaViewModel
    val adapter = FluxoCaixaAdapter()
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
        val recycler = root.findViewById<RecyclerView>(R.id.recyclerFluxoCaixa)
        recycler.adapter = adapter
        adapter.submitList(getFluxoCaixaList())


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

    private fun getFluxoCaixaList(): List<ItemFluxoCaixa> {
        val realm = Realm.getDefaultInstance()
        val results = realm.where<ItemFluxoCaixa>().findAll()
        return results
    }

}
