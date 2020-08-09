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
import com.example.agrogestao.models.realmclasses.Farm
import com.example.agrogestao.viewmodel.navigation.ApresentacaoFazendaViewModel
import io.realm.Realm

class ApresentacaoFazendaFragment : Fragment() {

    private lateinit var id: String
    private lateinit var apresentacaoFazendaViewModel: ApresentacaoFazendaViewModel
    private lateinit var root: View
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
        root = inflater.inflate(R.layout.apresentacao_fazenda, container, false)
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
                lucroMeta = String.format("%.2f", it.metaLucro)
                mLiquidaMeta = String.format("%.2f", it.metaMargemLiquida)
                mBrutaMeta = String.format("%.2f", it.metaMargemBruta)
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
                lucroAtual = String.format("%.2f", it.lucro)
                mLiquidaAtual = String.format("%.2f", it.margemLiquida)
                mBrutaAtual = String.format("%.2f", it.margemBruta)
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
                textSaldo.text = "Saldo: ${String.format("%.2f", it.dinheiroBanco)}"
                textPagar.text = "Contas a pagar: ${String.format(
                    "%.2f",
                    it.totalContasPagar + it.pendenciasPagamento
                )}"
                textReceber.text =
                    "Contas a receber: ${String.format(
                        "%.2f",
                        it.totalContasReceber + it.pendenciasRecebimento
                    )}"
                textPatrimonioLiquido.text =
                    "Patrimônio Líquido: ${String.format("%.2f", it.patrimonioLiquido)}"
                textSolvencia.text = "Liquidez geral: ${String.format("%.2f", it.liquidezGeral)}"
                textLiquidez.text =
                    "Liquidez corrente: ${String.format("%.2f", it.liquidezCorrente)}"
                textRentabilidade.text = "Rentabilidade: ${String.format("%.2f", it.rentabilidade)}"

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
            farm.saveToDb()
            mBuilder.dismiss()
        }
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


}
