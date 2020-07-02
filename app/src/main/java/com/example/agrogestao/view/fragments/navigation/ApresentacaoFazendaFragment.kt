package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.agrogestao.R
import com.example.agrogestao.models.AtividadesEconomicas
import com.example.agrogestao.viewmodel.ApresentacaoFazendaViewModel
import io.realm.Realm
import io.realm.kotlin.where

class ApresentacaoFazendaFragment : Fragment() {

    private lateinit var id: String
    private lateinit var apresentacaoFazendaViewModel: ApresentacaoFazendaViewModel
    var lucroAtual: String = ""
    var lucroMeta: String = ""
    var mBrutaAtual: String = ""
    var mBrutaMeta: String = ""
    var mLiquidaAtual: String = ""
    var mLiquidaMeta: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        apresentacaoFazendaViewModel =
            ViewModelProvider(this).get(ApresentacaoFazendaViewModel::class.java)
        val root = inflater.inflate(R.layout.apresentacao_fazenda, container, false)
        inicializarListeners(root)
        observer(root)

        if (arguments?.get("id") != null) {
            id = arguments?.getString("id")!!
            apresentacaoFazendaViewModel.verificarRegistro(id)
        } else {
            id = apresentacaoFazendaViewModel.recuperacaoDrawer()
        }



        return root
    }

    private fun observer(view: View) {

        apresentacaoFazendaViewModel.myFarm.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                lucroMeta = it.metaLucro.toString()
                mLiquidaMeta = it.metaMargemLiquida.toString()
                mBrutaMeta = it.metaMargemBruta.toString()
                val textNomeFazenda = view.findViewById<TextView>(R.id.resultadosFazendaText)
                textNomeFazenda.text = it.codigoFazenda
                atualizarTextos(view)
            }
        })
        apresentacaoFazendaViewModel.myBalancoPatrimonial.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                lucroAtual = it.lucro.toString()
                mLiquidaAtual = it.margemLiquida.toString()
                mBrutaAtual = it.margemBruta.toString()
                atualizarTextos(view)
                val textSaldo = view.findViewById<TextView>(R.id.textsSaldoApresentacao)
                val textPagar = view.findViewById<TextView>(R.id.textsPagarApresentacao)
                val textReceber = view.findViewById<TextView>(R.id.textReceberApresentacao)
                val textPatrimonioLiquido =
                    view.findViewById<TextView>(R.id.textsPatrimonioApresentacao)
                val textSolvencia = view.findViewById<TextView>(R.id.textsSolvenciaApresentacao)
                val textLiquidez = view.findViewById<TextView>(R.id.textsLiquidezApresentacao)
                val textRentabilidade =
                    view.findViewById<TextView>(R.id.textsRentabilidadeApresentacao)
                textSaldo.text = "Saldo: ${it.saldo}"
                textPagar.text = "Contas a pagar: ${it.totalContasPagar}"
                textReceber.text = "Contas a receber: ${it.totalContasReceber}"
                textPatrimonioLiquido.text = "Patrimônio Líquido: ${it.patrimonioLiquido}"
                textSolvencia.text = "Liquidez geral: ${it.liquidezGeral}"
                textLiquidez.text = "Liquidez corrente: ${it.liquidezCorrente}"
                textRentabilidade.text = "Rentabilidade: ${it.rentabilidade}"
                Toast.makeText(context, "${it.ativo}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun atualizarTextos(view: View) {


        val textlucro = view.findViewById<TextView>(R.id.textLucroApresentacao)
        val textmBruta = view.findViewById<TextView>(R.id.textMargemBrutaApresentacao)
        val textmLiquida = view.findViewById<TextView>(R.id.textMargemLiquidaApresentacao)
        val stringLucro = "Lucro: $lucroAtual / $lucroMeta"
        val stringmBruta = "Margem Bruta: $mBrutaAtual / $mBrutaMeta"
        val stringmLiquida = "Margem Liquida: $mLiquidaAtual / $mLiquidaMeta"
        textlucro.text = stringLucro
        textmBruta.text = stringmBruta
        textmLiquida.text = stringmLiquida

    }

    private fun inicializarListeners(root: View) {

        val indicadoresFinanceirosAtividade =
            root.findViewById<Button>(R.id.indicadoresFinanceirosAtividadeButton)
        val indicadoresFinanceirosDetalhes =
            root.findViewById<Button>(R.id.indicadoresFinanceirosDetalhes)
        val fluxoCaixaAtividade = root.findViewById<Button>(R.id.fluxoCaixaAtividade)
        val fluxoCaixaDetalhes = root.findViewById<Button>(R.id.fluxoCaixaDetalhes)
        val fluxoCaixaCadastrar = root.findViewById<Button>(R.id.fluxoCaixaCadastrar)
        val metasFazenda = root.findViewById<Button>(R.id.metasFazendaButton)
        val balancoPatrimonialDetalhes = root.findViewById<Button>(R.id.balancoPatrimonialDetalhes)
        val balancoPatrimonialInventario =
            root.findViewById<Button>(R.id.balancoPatrimonialInventario)


        metasFazenda.setOnClickListener {
            val bundle = bundleOf("id" to id)
            root.findNavController().navigate(R.id.toAtualizarFazendaFragment, bundle)
        }

        indicadoresFinanceirosAtividade.setOnClickListener {
            val bundle = bundleOf("id" to id)
            root.findNavController().navigate(R.id.toResultadosAtividadeFragment, bundle)
        }
        indicadoresFinanceirosDetalhes.setOnClickListener {
            val bundle = bundleOf("id" to id)
            root.findNavController().navigate(R.id.toResultadosFazendaFragment, bundle)
        }
        fluxoCaixaAtividade.setOnClickListener {
            val bundle = bundleOf("id" to id)
            root.findNavController().navigate(R.id.toFluxoCaixa, bundle)
        }
        fluxoCaixaDetalhes.setOnClickListener {
            val bundle = bundleOf("id" to id)
            root.findNavController().navigate(R.id.toFluxoCaixa, bundle)
        }
        fluxoCaixaCadastrar.setOnClickListener {
            val bundle = bundleOf("id" to id)
            root.findNavController().navigate(R.id.toCadastroFluxoCaixaFragment, bundle)
        }
        balancoPatrimonialDetalhes.setOnClickListener {
            val bundle = bundleOf("id" to id)
            root.findNavController().navigate(R.id.toBalancoPatrimonial, bundle)
        }
        balancoPatrimonialInventario.setOnClickListener {
            val realm = Realm.getDefaultInstance()
            val query = realm.where<AtividadesEconomicas>().findAll()
            if (query.size > 0) {
                val bundle = bundleOf("id" to id)
                root.findNavController().navigate(R.id.toCadastroInventarioFragment, bundle)
            } else {
                Toast.makeText(
                    context,
                    "Para cadastrar itens, primeiro crie as atividades.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        apresentacaoFazendaViewModel.recuperacaoDrawer()
    }

}
