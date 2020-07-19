package com.example.agrogestao.view.fragments.navigation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

    private fun spinnerAdapter() {}

    private fun load() {
        val realm = Realm.getDefaultInstance()
        realm.where<BalancoPatrimonial>().contains("farm", id).findFirst()
        list = realm.where<AtividadesEconomicas>().contains("fazendaID", id).findAll()
        criarGrafico(0)
    }

    private fun criarGrafico(position: Int) {
        val entries = mutableListOf<PieEntry>()
        val item = list[position]
        if (item.custoSemente > 1) {
            entries.add(PieEntry(item.custoSemente, "Sementes"))
        }
        if (item.custoDefensivo > 1) {
            entries.add(PieEntry(item.custoDefensivo, "Defensivo"))
        }
        if (item.custoFertilizante > 1) {
            entries.add(PieEntry(item.custoFertilizante, "Fetilizante"))
        }
        if (item.custoMaodeobra > 1) {
            entries.add(PieEntry(item.custoMaodeobra, "Mão de obra"))
        }
        if (item.custoMaquina > 1) {
            entries.add(PieEntry(item.custoMaquina, "Máquina"))
        }
        if (item.custoOutros > 1) {
            entries.add(PieEntry(item.custoOutros, "Outros"))
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
