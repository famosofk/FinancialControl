package com.example.agrogestao.view.fragments.navigation

import android.app.AlertDialog
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
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import com.example.agrogestao.models.realmclasses.Farm
import com.example.agrogestao.models.realmclasses.FluxoCaixa
import com.example.agrogestao.viewmodel.navigation.ApresentacaoFazendaViewModel
import io.realm.Realm
import io.realm.kotlin.where

class ApresentacaoFazendaFragment : Fragment() {

    private lateinit var id: String
    private lateinit var apresentacaoFazendaViewModel: ApresentacaoFazendaViewModel
    private lateinit var root: View
    private var lucroAtual: String = ""
    private var lucroMeta: String = ""
    private var mBrutaAtual: String = ""
    private var mBrutaMeta: String = ""
    private var mLiquidaAtual: String = ""
    private var mLiquidaMeta: String = ""
    private var saldoAtual = ""
    private var saldoMeta = ""
    private var metaPatrimonio = ""
    private var atualPatrimonio = ""
    private var metaLGeral = ""
    private var atualLGeral = ""
    private var metaLCorrente = ""
    private var atualLCorrente = ""
    private var atualRentabilidade = ""
    private var metaRentabilidade = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        apresentacaoFazendaViewModel =
            ViewModelProvider(this).get(ApresentacaoFazendaViewModel::class.java)
        root = inflater.inflate(R.layout.apresentacao_fazenda, container, false)
        inicializarListeners(root)
        observer(root)

        if (arguments?.get("id") != null) {
            id = arguments?.getString("id")!!
            apresentacaoFazendaViewModel.verificarRegistro(id)
        } else id = apresentacaoFazendaViewModel.recuperacaoDrawer()


        return root
    }

    private fun observer(view: View) {

        apresentacaoFazendaViewModel.myFarm.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                lucroMeta = String.format("%.2f", it.metaLucro)
                mLiquidaMeta = String.format("%.2f", it.metaMargemLiquida)
                mBrutaMeta = String.format("%.2f", it.metaMargemBruta)

                saldoMeta = String.format("%.2f", it.metasaldo)
                metaPatrimonio = String.format("%.2f", it.metaPatrimonioLiquido)
                metaLCorrente = String.format("%.2f", it.metaLiquidezCorrente)
                metaLGeral = String.format("%.2f", it.metaLiquidezGeral)
                metaRentabilidade = String.format("%.2f", it.rentabilidade)

                val textNomeFazenda = view.findViewById<TextView>(R.id.resultadosFazendaText)
                textNomeFazenda.text = it.codigoFazenda
                atualizarTextos(view)
                if (!it.observacao.equals("")) {
                    createAlertDialog(it)
                }
            }
        })
        apresentacaoFazendaViewModel.myBalancoPatrimonial.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                atualizarTextosBalanco(it)
            }
        })

    }

    private fun createAlertDialog(farm: Farm) {
        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.observacao_dialog_layout, null)
        val dontshowButton: Button = mDialogView.findViewById(R.id.dontshow_observacao)
        val message: TextView = mDialogView.findViewById(R.id.message_farm)
        message.text = farm.observacao
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .create()
        mBuilder.show()

        dontshowButton.setOnClickListener {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            farm.observacao = ""
            realm.commitTransaction()
            mBuilder.dismiss()
        }
    }

    private fun atualizarTextos(view: View) {

        val textlucro = view.findViewById<TextView>(R.id.textLucroApresentacao)
        val textmBruta = view.findViewById<TextView>(R.id.textMargemBrutaApresentacao)
        val textmLiquida = view.findViewById<TextView>(R.id.textMargemLiquidaApresentacao)
        val textSaldo = view.findViewById<TextView>(R.id.textsSaldoApresentacao)
        val textPatrimonioLiquido = view.findViewById<TextView>(R.id.textsPatrimonioApresentacao)
        val textgeral = view.findViewById<TextView>(R.id.textsSolvenciaApresentacao)
        val textLiquidez = view.findViewById<TextView>(R.id.textsLiquidezApresentacao)
        val textRentabilidade = view.findViewById<TextView>(R.id.textsRentabilidadeApresentacao)
        var stringLucro = ""
        if (lucroAtual.equals("0.")) {
            stringLucro = "Lucro: 0.00 / $lucroMeta"
        } else {
            stringLucro = "Lucro: $lucroAtual / $lucroMeta"
        }
        val stringRentabilidade = "Rentabilidade: $atualRentabilidade / $metaRentabilidade %"
        val stringmBruta = "Margem Bruta: $mBrutaAtual / $mBrutaMeta"
        val stringmLiquida = "Margem Liquida: $mLiquidaAtual / $mLiquidaMeta"
        val stringsaldo = "Saldo: $saldoAtual / $saldoMeta"
        val stringPatrimonio = "$atualPatrimonio / $metaPatrimonio";
        val stringlcorrente = "$atualLCorrente / $metaLCorrente"
        val stringlgeral = "$atualLGeral / $metaLGeral";
        textlucro.text = stringLucro
        textmBruta.text = stringmBruta
        textmLiquida.text = stringmLiquida
        textSaldo.text = stringsaldo
        textgeral.text = stringlgeral
        textLiquidez.text = stringlcorrente
        textPatrimonioLiquido.text = stringPatrimonio
        textRentabilidade.text = stringRentabilidade
    }

    private fun inicializarListeners(root: View) {

        val indicadoresFinanceirosAtividade =
            root.findViewById<Button>(R.id.indicadoresFinanceirosAtividadeButton)
        val indicadoresFinanceirosDetalhes =
            root.findViewById<Button>(R.id.indicadoresFinanceirosDetalhes)
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


    fun atualizarTextosBalanco(balanco: BalancoPatrimonial) {
        if (lucroAtual != "0.00") {
            lucroAtual = balanco.lucro.substring(0, balanco.lucro.length - 2)
        }
        mLiquidaAtual = balanco.margemLiquida
        mBrutaAtual = balanco.margemBruta
        saldoAtual = balanco.dinheiroBanco

        val textPagar = root.findViewById<TextView>(R.id.textsPagarApresentacao)
        val textReceber = root.findViewById<TextView>(R.id.textReceberApresentacao)

        //colocar aqui o realm e recuperar as contas novas
        var pendenciasRecebimento = 0f
        var pendenciasPagamento = 0f
        val realm = Realm.getDefaultInstance()
        val fluxoCaixa = realm.where<FluxoCaixa>().contains("farmID", balanco.farmID).findFirst()!!
        fluxoCaixa.list.forEach {
            if (!it.currentYear) {
                if (it.tipo)
                    pendenciasPagamento += it.valorAtual.toFloat()
                else pendenciasRecebimento += it.valorAtual.toFloat()
            }
        }

        textPagar.text =
            "Contas a pagar: ${balanco.totalContasPagar.toBigDecimal() + pendenciasPagamento.toBigDecimal()}"
        textReceber.text =
            "Contas a receber: ${balanco.totalContasReceber.toBigDecimal() + pendenciasRecebimento.toBigDecimal()}"
        atualPatrimonio = "Patrimônio Líquido: ${balanco.patrimonioLiquido}"
        atualLGeral = "Liquidez geral:  ${balanco.liquidezGeral}"
        atualLCorrente = "Liquidez corrente: ${balanco.liquidezCorrente}"
        atualRentabilidade = balanco.rentabilidade

        atualizarTextos(root)
    }


}
