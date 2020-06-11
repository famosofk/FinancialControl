package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.agrogestao.R
import com.example.agrogestao.viewmodel.ApresentacaoFazendaViewModel

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
                val textPagar = view.findViewById<TextView>(R.id.textsSaldoApresentacao)
                val textReceber = view.findViewById<TextView>(R.id.textsSaldoApresentacao)
                val textPatrimonioLiquido =
                    view.findViewById<TextView>(R.id.textsPatrimonioApresentacao)
                val textSolvencia = view.findViewById<TextView>(R.id.textsSolvenciaApresentacao)
                val textLiquidez = view.findViewById<TextView>(R.id.textsLiquidezApresentacao)
                val textRentabilidade =
                    view.findViewById<TextView>(R.id.textsRentabilidadeApresentacao)
                textSaldo.text = it.saldo.toString()
                textPagar.text = it.totalContasPagar.toString()
                textReceber.text = it.totalContasReceber.toString()
                textPatrimonioLiquido.text = it.patrimonioLiquido.toString()
                textSolvencia.text = it.liquidezGeral.toString()
                textLiquidez.text = it.liquidezCorrente.toString()
                textRentabilidade.text = it.rentabilidade.toString()
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
            val bundle = bundleOf("id" to id)
            root.findNavController().navigate(R.id.toCadastroInventarioFragment, bundle)
        }

    }

    override fun onResume() {
        super.onResume()
        apresentacaoFazendaViewModel.recuperacaoDrawer()
    }

}
