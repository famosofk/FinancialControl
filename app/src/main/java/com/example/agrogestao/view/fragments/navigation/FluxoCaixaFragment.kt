package com.example.agrogestao.view.fragments.navigation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.ItemFluxoCaixa
import com.example.agrogestao.view.adapter.FluxoCaixaAdapter
import com.example.agrogestao.view.adapter.ItemFluxoCaixaListener
import com.example.agrogestao.viewmodel.FluxoCaixaViewModel
import io.realm.Realm
import io.realm.kotlin.where

class FluxoCaixaFragment : Fragment() {

    private lateinit var fluxoCaixaViewModel: FluxoCaixaViewModel
    val adapterVista = FluxoCaixaAdapter(ItemFluxoCaixaListener { })
    val adapterPrazo = FluxoCaixaAdapter(ItemFluxoCaixaListener { criarAtividadeDialog(it) })
    var id: String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fluxoCaixaViewModel =
            ViewModelProvider(this).get(FluxoCaixaViewModel::class.java)
        val root = inflater.inflate(R.layout.fluxo_caixa_atividade, container, false)

        id = arguments?.getString("id")!!
        atualizarListas()

        val recyclerVista = root.findViewById<RecyclerView>(R.id.recyclerFluxoCaixaVista)
        recyclerVista.adapter = adapterVista
        val recyclerPrazo = root.findViewById<RecyclerView>(R.id.recyclerFluxoCaixaPrazo)
        recyclerPrazo.adapter = adapterPrazo


        observe(root)
        return root
    }


    private fun atualizarListas() {
        fluxoCaixaViewModel.load(id)
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
            pagar.text = "Contas a pagar: ${it.totalContasPagar}"
            val receber = view.findViewById<TextView>(R.id.fcaixaReceberText)
            receber.text = "Contas a receber: ${it.totalContasReceber}"
            val lucro = view.findViewById<TextView>(R.id.fcaixaLucroText)
            lucro.text = "Lucro: ${it.lucro}"

            //se for da atividade temos que filtrar só da atividade em questão
        })
        fluxoCaixaViewModel.myFluxoCaixa.observe(viewLifecycleOwner, Observer {
            fluxoCaixaViewModel.loadLists("Geral")
        })
        fluxoCaixaViewModel.listaVista.observe(viewLifecycleOwner, Observer {
            adapterVista.submitList(it)
        })
        fluxoCaixaViewModel.listaPrazo.observe(viewLifecycleOwner, Observer {
            adapterPrazo.submitList(it)
        })
    }


    private fun criarAtividadeDialog(id: String) {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.pagar_pendencia_dialog, null)
        val confirmarButton: Button = mDialogView.findViewById(R.id.pagarPendenciaButton)
        val cancelButton: Button = mDialogView.findViewById(R.id.cancelarPagarPendencia)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("Pagar/Receber pendência?")
            .create()

        mBuilder.show()

        confirmarButton.setOnClickListener {
            mBuilder.dismiss()
            val realm = Realm.getDefaultInstance()
            val item = realm.where<ItemFluxoCaixa>().contains("itemID", id).findFirst()
            realm.beginTransaction()
            item?.pagamentoPrazo = false
            if (item?.tipo!!) {
                val balanco = fluxoCaixaViewModel.myBalancoPatrimonial.value!!
                balanco.totalContasPagar -= item.quantidadeInicial * item.valorInicial
                balanco.dinheiroBanco -= item.quantidadeInicial * item.valorInicial
            } else {
                val balanco = fluxoCaixaViewModel.myBalancoPatrimonial.value!!
                balanco.totalContasReceber -= item.quantidadeInicial * item.valorInicial
                balanco.dinheiroBanco += item.quantidadeInicial * item.valorInicial
            }
            realm.commitTransaction()
            mBuilder.dismiss()


        }
        cancelButton.setOnClickListener { mBuilder.dismiss() }


    }


}
