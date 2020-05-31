package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.agrogestao.R
import com.example.agrogestao.viewmodel.ApresentacaoFazendaViewModel

class ApresentacaoFazendaFragment : Fragment() {

    private lateinit var apresentacaoFazendaViewModel: ApresentacaoFazendaViewModel
    var lucroAtual: String = "";
    var lucroMeta: String = "";
    var mBrutaAtual: String = "";
    var mBrutaMeta: String = "";
    var mLiquidaAtual: String = "";
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
        apresentacaoFazendaViewModel.load()

        return root
    }

    private fun observer(view: View) {

        apresentacaoFazendaViewModel.myFarm.observe(viewLifecycleOwner, Observer {
            lucroMeta = it.metaLucro.toString()
            mLiquidaMeta = it.metaMargemLiquida.toString()
            mBrutaMeta = it.metaMargemBruta.toString()
            atualizarTextos(view)
        })
        apresentacaoFazendaViewModel.myBalancoPatrimonial.observe(viewLifecycleOwner, Observer {
            lucroAtual = it.lucro.toString()
            mLiquidaAtual = it.margemLiquida.toString()
            mBrutaMeta = it.margemBruta.toString()
            atualizarTextos(view)
            val textSaldo = view.findViewById<TextView>(R.id.textsSaldoApresentacao)
            val textPagar = view.findViewById<TextView>(R.id.textsSaldoApresentacao)
            val textReceber = view.findViewById<TextView>(R.id.textsSaldoApresentacao)
            val textPatrimonioLiquido =
                view.findViewById<TextView>(R.id.textsPatrimonioApresentacao)
            val textSolvencia = view.findViewById<TextView>(R.id.textsSolvenciaApresentacao)
            val textLiquidez = view.findViewById<TextView>(R.id.textsLiquidezApresentacao)
            val textRentabilidade = view.findViewById<TextView>(R.id.textsRentabilidadeApresentacao)
            textSaldo.setText(it.saldo.toString())
            textPagar.setText(it.totalContasPagar.toString())
            textReceber.setText(it.totalContasReceber.toString())
            textPatrimonioLiquido.setText(it.patrimonioLiquido.toString())
            textSolvencia.setText(it.solvencia.toString())
            textLiquidez.setText(it.liquidez.toString())
            textRentabilidade.setText(it.rentabilidade.toString())

        })

    }

    private fun atualizarTextos(view: View) {


        val textlucro = view.findViewById<TextView>(R.id.textLucroApresentacao)
        val textmBruta = view.findViewById<TextView>(R.id.textMargemLiquidaApresentacao)
        val textmLiquida = view.findViewById<TextView>(R.id.textMargemLiquidaApresentacao)
        val stringLucro = "Lucro: $lucroAtual/$lucroMeta"
        val stringmBruta = "Margem Bruta: $mBrutaAtual / $mBrutaMeta"
        val stringmLiquida = "Margem Liquida: $mLiquidaAtual/$mLiquidaMeta"
        textlucro.setText(stringLucro)
        textmBruta.setText(stringmBruta)
        textmLiquida.setText(stringmLiquida)

    }

    private fun inicializarListeners(root: View) {

        val indicadoresFinanceirosAtividade =
            root.findViewById<Button>(R.id.indicadoresFinanceirosAtividadeButton)
        val indicadoresFinanceirosDetalhes =
            root.findViewById<Button>(R.id.indicadoresFinanceirosDetalhes)
        val fluxoCaixaAtividade = root.findViewById<Button>(R.id.fluxoCaixaAtividade)
        val fluxoCaixaDetalhes = root.findViewById<Button>(R.id.fluxoCaixaDetalhes)
        val fluxoCaixaCadastrar = root.findViewById<Button>(R.id.fluxoCaixaCadastrar)
        val balancoPatrimonialDetalhes = root.findViewById<Button>(R.id.balancoPatrimonialDetalhes)
        val balancoPatrimonialInventario =
            root.findViewById<Button>(R.id.balancoPatrimonialInventario)

        indicadoresFinanceirosAtividade.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.toResultadosAtividadeFragment
            )
        )
        indicadoresFinanceirosDetalhes.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toResultadosFazendaFragment))
        fluxoCaixaAtividade.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toFluxoCaixa))
        fluxoCaixaDetalhes.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toFluxoCaixa))
        fluxoCaixaCadastrar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCadastroFluxoCaixaFragment))
        balancoPatrimonialDetalhes.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toBalancoPatrimonial))
        balancoPatrimonialInventario.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCadastroInventarioFragment))


    }
}
