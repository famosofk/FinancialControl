package com.example.agrogestao.view.fragments.navigation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.agrogestao.R
import com.example.agrogestao.models.AtividadesEconomicas
import com.example.agrogestao.models.BalancoPatrimonial
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import io.realm.Realm
import io.realm.kotlin.where

/**
 * A simple [Fragment] subclass.
 */
class ResultadosAtividadeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var id = ""
    private lateinit var root: View
    private lateinit var list: MutableList<AtividadesEconomicas>
    private lateinit var balancoPatrimonial: BalancoPatrimonial
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        id = arguments?.getString("id").toString()
        root = inflater.inflate(R.layout.resultados_atividades_fazenda, container, false)
        spinnerAdapter()
        load()
        return root

    }


    private fun spinnerAdapter() {
        val realm = Realm.getDefaultInstance()
        list = realm.where<AtividadesEconomicas>().contains("fazendaID", id).findAll()
        val strings = mutableListOf<String>()
        for (atividade in list) {
            strings.add(atividade.nome)
        }
        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                strings
            )
        }
        val spinner = root.findViewById<Spinner>(R.id.selecionarAtividadeSpinner)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    private fun load() {
        val realm = Realm.getDefaultInstance()
        balancoPatrimonial = realm.where<BalancoPatrimonial>().contains("farm", id).findFirst()!!

        populartela()
        criarGrafico(0)
    }

    private fun populartela() {
        val lucroText: TextView = root.findViewById(R.id.lucroResultadoAtivideFazenda)
        val lucroString = "Lucro:  ${String.format("%.2f", balancoPatrimonial.lucro)}"
        lucroText.text = lucroString
        val liquida: TextView = root.findViewById(R.id.margemLiquidaAtividadeFazenda)
        val liquidaString =
            "Margem líquida: ${String.format("%.2f", balancoPatrimonial.margemLiquida)}"
        liquida.text = liquidaString
        val bruta: TextView = root.findViewById(R.id.margemBrutaAtividadeFazenda)
        val brutaString = "Margem bruta: ${String.format("%.2f", balancoPatrimonial.margemBruta)}"
        bruta.text = brutaString
        val receitaBruta: TextView = root.findViewById(R.id.receitaBrutaAtividadeFazenda)
        val receitaString =
            "Receita bruta: ${String.format("%.2f", balancoPatrimonial.receitaBruta)}"
        receitaBruta.text = receitaString
        val custoTotal: TextView = root.findViewById(R.id.custoTotalAtividadeFazenda)
        val custotString =
            "Custo total: ${String.format("%.2f", balancoPatrimonial.calcularCustoTotal())}"
        custoTotal.text = custotString
        val custoOperacionalTotal: TextView =
            root.findViewById(R.id.custoOperacionalTotalAtividadeFazenda)
        val custooptString = "Custo operacional total: ${String.format(
            "%.2f",
            balancoPatrimonial.custoOperacionalTotal
        )}"
        custoOperacionalTotal.text = custooptString
        val custoOperacionalEfetivo: TextView =
            root.findViewById(R.id.custoOperacionalEfetivoAtividadeFazenda)
        val custoopeString =
            "Custo operacional efetivo: ${String.format(
                "%.2f",
                balancoPatrimonial.custoOperacionalEfetivo
            )}"
        custoOperacionalEfetivo.text = custoopeString
    }

    private fun criarGrafico(position: Int) {
        val entries = mutableListOf<PieEntry>()
        val item = list[position]
        val aux = list[0]

        if (item.custoSemente + aux.custoSemente > 1) {
            if (position == 0) {
                entries.add(PieEntry(item.custoSemente, "Sementes"))
            } else {
                entries.add(
                    PieEntry(
                        (aux.custoSemente * item.rateio).div(100f) + item.custoSemente,
                        "Sementes"
                    )
                )
            }
        }
        if (item.custoDefensivo + aux.custoDefensivo > 1) {
            if (position == 0) {
                entries.add(PieEntry(item.custoDefensivo, "Defensivo"))
            } else {
                entries.add(
                    PieEntry(
                        (aux.custoDefensivo * item.rateio).div(100f) + item.custoDefensivo,
                        "Defensivo"
                    )
                )
            }
        }
        if (item.custoFertilizante + aux.custoFertilizante > 1) {
            if (position == 0) {
                entries.add(PieEntry(item.custoFertilizante, "Fertilizante"))
            } else {
                entries.add(
                    PieEntry(
                        (aux.custoFertilizante * item.rateio).div(100f) + item.custoFertilizante,
                        "Fertilizante"
                    )
                )
            }
        }
        if (item.custoMaodeobra + aux.custoMaodeobra > 1) {
            if (position == 0) {
                entries.add(PieEntry(item.custoMaodeobra, "Mão de obra"))
            } else {
                entries.add(
                    PieEntry(
                        (aux.custoMaodeobra * item.rateio).div(100f) + item.custoMaodeobra,
                        "Mão de obra"
                    )
                )
            }
        }
        if (item.custoMaquina + aux.custoMaquina > 1) {
            if (position == 0) {
                entries.add(PieEntry(item.custoMaquina, "Máquinas"))
            } else {
                entries.add(
                    PieEntry(
                        (aux.custoMaquina * item.rateio).div(100f) + item.custoMaquina,
                        "Máquinas"
                    )
                )
            }
        }
        if (item.custoOutros + aux.custoOutros > 1) {
            if (position == 0) {
                entries.add(PieEntry(item.custoOutros, "Outros"))
            } else {
                entries.add(
                    PieEntry(
                        (aux.custoOutros * item.rateio).div(100f) + item.custoOutros,
                        "Outros"
                    )
                )
            }
        }

        val piechart = root.findViewById<PieChart>(R.id.distribuicao_despesas_chart)
        val set = PieDataSet(entries, "Distribuição de despesas.")
        val data = PieData(set)
        set.setColors(COLORFUL_COLORS, 255)
        piechart.data = data
        piechart.invalidate()


    }

    val COLORFUL_COLORS = intArrayOf(
        Color.rgb(193, 37, 82),
        Color.rgb(255, 102, 0),
        Color.rgb(245, 199, 0),
        Color.rgb(106, 150, 31),
        Color.rgb(179, 100, 53),
        Color.rgb(0, 128, 255)
    )

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        criarGrafico(position = p2)
    }

}
