package com.example.agrogestao.view.fragments.navigation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.ItemBalancoPatrimonial
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import com.example.agrogestao.view.adapter.ItemPatrimonioAdapter
import com.example.agrogestao.view.listener.FarmListener
import com.example.agrogestao.viewmodel.navigation.BalancoPatrimonialViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import io.realm.Realm
import io.realm.kotlin.where

class BalancoPatrimonialFragment : Fragment() {

    private lateinit var balancoPatrimonialViewModel: BalancoPatrimonialViewModel
    var id = ""
    private lateinit var root: View
    val adapter = ItemPatrimonioAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        balancoPatrimonialViewModel =
            ViewModelProvider(this).get(BalancoPatrimonialViewModel::class.java)
        root = inflater.inflate(R.layout.apresentacao_balanco_patrimonial, container, false)
        if (arguments?.get("id") != null) {
            id = arguments?.getString("id")!!
        }
        balancoPatrimonialViewModel.load(id)

        observe(root)


        return root
    }


    private fun observe(view: View) {
        balancoPatrimonialViewModel.myBalancoPatrimonial.observe(viewLifecycleOwner, Observer {
            popularTextos(it, view)
            preencherRecycler(it, view)
        })
    }

    private fun popularTextos(it: BalancoPatrimonial, view: View) {

        val textPatrimonio = view.findViewById<TextView>(R.id.textPatrimonioBalanco)
        textPatrimonio.text = "Patrimônio líquido: " + it.patrimonioLiquido
        val textSolvencia = view.findViewById<TextView>(R.id.textSolvenciaBalanco)
        textSolvencia.text = "Liquidez Geral: " + it.liquidezGeral
        val textLiquidez = view.findViewById<TextView>(R.id.textLiquidezBalanco)
        textLiquidez.text = "Liquidez Corrente: " + it.liquidezCorrente
        val textRentabilidade = view.findViewById<TextView>(R.id.textRentabilidadeBalanco)
        textRentabilidade.text = "Rentabilidade: " + it.rentabilidade
        val textAtivo = view.findViewById<TextView>(R.id.textAtivoBalanco)
        textAtivo.text = "Ativo: " + it.ativo
        val textAtivoCirculante = view.findViewById<TextView>(R.id.textAtivoCirculanteBalanco)
        val ativocirculante = it.calcularValorAnimaisInsumosProdutos().toFloat() +
                it.dinheiroBanco.toFloat() + it.pendenciasRecebimento.toFloat() + it.totalContasReceber.toFloat()
        textAtivoCirculante.text =
            "Ativo circulante: " + String.format(
                "%.2f",
                (ativocirculante)
            )
        val textAtivoNaoCirculante = view.findViewById<TextView>(R.id.textAtivoNaoCirculanteBalanco)
        textAtivoNaoCirculante.text =
            "Ativo não circulante: " + (it.ativo.toFloat() - ativocirculante)
        val textPassivo = view.findViewById<TextView>(R.id.textPassivoBalanco)
        textPassivo.text = "Passivo: ${it.passivo}"
        val textPassivoCirculante = view.findViewById<TextView>(R.id.textPassivoCirculanteBalanco)
        textPassivoCirculante.text =
            "Passivo circulante: ${it.totalContasPagar + it.pendenciasPagamento}"
        val textPassivoNCirculante = view.findViewById<TextView>(R.id.textPassivoNaoCirculanteBalanco)
        textPassivoNCirculante.text = "Passivo não circulante: ${it.dividasLongoPrazo}"

        criarGrafico(balanco = it)

    }


    private fun criarGrafico(balanco: BalancoPatrimonial) {
        val entries = ArrayList<PieEntry>()

        if (balanco.calcularValorAnimais() >= 1) {
            entries.add(
                PieEntry(
                    balanco.calcularValorAnimais().toFloat(),
                    ItemBalancoPatrimonial.ITEM_ANIMAIS
                )
            )
        }

        if (balanco.dinheiroBanco.toFloat() >= 1) {
            entries.add(
                PieEntry(
                    balanco.dinheiroBanco.toFloat(),
                    "Caixa"
                )
            )
        }

        if (balanco.calcularValorInsumos() >= 1) {
            entries.add(
                PieEntry(
                    balanco.calcularValorInsumos().toFloat(),
                    ItemBalancoPatrimonial.ITEM_INSUMOS
                )
            )
        }
        if (balanco.calcularValorProdutos() >= 1.toBigDecimal()) {
            entries.add(
                PieEntry(
                    balanco.calcularValorProdutos().toFloat(),
                    ItemBalancoPatrimonial.ITEM_PRODUTOS
                )
            )
        }
        if (balanco.calcularValorBenfeitorias() >= 1) {
            entries.add(
                PieEntry(
                    balanco.calcularValorBenfeitorias().toFloat(),
                    ItemBalancoPatrimonial.ITEM_BENFEITORIA
                )
            )
        }
        if (balanco.calcularValorMaquinas() >= 1) {
            entries.add(
                PieEntry(
                    balanco.calcularValorMaquinas().toFloat(), ItemBalancoPatrimonial.ITEM_MAQUINAS
                )
            )
        }
        if (balanco.calcularValorTerras() > 1) {
            entries.add(
                PieEntry(
                    balanco.calcularValorTerras().toFloat(),
                    ItemBalancoPatrimonial.ITEM_TERRA
                )
            )
        }

        val set = PieDataSet(entries, "Distribuição de patrimônio.")
        val data = PieData(set)
        set.setColors(COLORFUL_COLORS, 255)
        val piechart = root.findViewById<PieChart>(R.id.distribuicao_patrimonio_chart)
        piechart.data = data
        piechart.invalidate()


    }

    private fun preencherRecycler(it: BalancoPatrimonial, root: View) {
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerItensBalanco)
        val clickListener = object : FarmListener {
            override fun onClick(id: Int) {
                alertDialog(it.listaItens[id]!!, it)
            }
        }

        adapter.attachListener(clickListener)
        adapter.submitList(it.listaItens)
        recyclerView.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun alertDialog(item: ItemBalancoPatrimonial, balanco: BalancoPatrimonial) {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.observacao_dialog_layout, null)
        val dontshowButton: Button = mDialogView.findViewById(R.id.dontshow_observacao)
        val message: TextView = mDialogView.findViewById(R.id.message_farm)
        val title: TextView = mDialogView.findViewById(R.id.title_dialog_view)
        title.text = "Excluir item inventário?"
        message.text =
            "Nome: ${item.nome}\n" + "Preço: ${item.valorAtual}\n" + "Atividade: ${item.atividade}\n" + "Essa operação não pode ser desfeita. \n  " +
                    "Para cancelar, clique fora deste popup."
        dontshowButton.text = "Excluir item!"
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .create()

        mBuilder.show()

        dontshowButton.setOnClickListener {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.where<ItemBalancoPatrimonial>().contains("idItem", item.idItem).findAll()
                .deleteAllFromRealm()
            balanco.atualizarBalanco()
            realm.commitTransaction()
            mBuilder.dismiss()
            val bundle = Bundle()
            bundle.putString("id", balanco.farmID)
            root.findNavController()
                .navigate(R.id.action_balanco_patrimonial_frag_to_nav_fazenda, bundle)
        }

    }

    val COLORFUL_COLORS = intArrayOf(
        Color.rgb(193, 37, 82),
        Color.rgb(255, 102, 0),
        Color.rgb(245, 199, 0),
        Color.rgb(106, 150, 31),
        Color.rgb(179, 100, 53),
        Color.rgb(0, 128, 255)
    )
}


