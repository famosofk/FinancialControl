package com.example.agrogestao.view.fragments.navigation

import android.graphics.Color
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
import com.example.agrogestao.models.ItemBalancoPatrimonial
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import com.example.agrogestao.view.adapter.ItemPatrimonioAdapter
import com.example.agrogestao.viewmodel.BalancoPatrimonialViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class BalancoPatrimonialFragment : Fragment() {

    private lateinit var balancoPatrimonialViewModel: BalancoPatrimonialViewModel
    var id = ""
    private lateinit var root: View

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
        textPatrimonio.text = "Patrimônio líquido: " + String.format("%.2f", it.patrimonioLiquido)
        val textSolvencia = view.findViewById<TextView>(R.id.textSolvenciaBalanco)
        textSolvencia.text = "Solvência: " + String.format("%.2f", it.liquidezGeral)
        val textLiquidez = view.findViewById<TextView>(R.id.textLiquidezBalanco)
        textLiquidez.text = "Liquidez: " + String.format("%.2f", it.liquidezCorrente)
        val textRentabilidade = view.findViewById<TextView>(R.id.textRentabilidadeBalanco)
        textRentabilidade.text = "Rentabilidade: " + String.format("%.2f", it.rentabilidade)
        val textAtivo = view.findViewById<TextView>(R.id.textAtivoBalanco)
        textAtivo.text = "Ativo: " + String.format("%.2f", it.ativo)
        val textAtivoCirculante = view.findViewById<TextView>(R.id.textAtivoCirculanteBalanco)
        textAtivoCirculante.text =
            "Ativo circulante: " + String.format(
                "%.2f",
                (it.calcularValorAnimaisInsumosProdutos() + it.dinheiroBanco)
            )
        val textPassivo = view.findViewById<TextView>(R.id.textPassivoBalanco)
        textPassivo.text = "Passivo: " + String.format("%.2f", it.passivo)

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
        if (balanco.calcularValorInsumos() >= 1) {
            entries.add(
                PieEntry(
                    balanco.calcularValorInsumos().toFloat(),
                    ItemBalancoPatrimonial.ITEM_INSUMOS
                )
            )
        }
        if (balanco.calcularValorProdutos() >= 1) {
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
        val adapter = ItemPatrimonioAdapter()
        adapter.submitList(it.listaItens)
        recyclerView.adapter = adapter
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


