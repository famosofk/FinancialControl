package com.example.agrogestao.view.fragments.navigation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agrogestao.R
import com.example.agrogestao.databinding.ResultadosFazendaBinding
import com.example.agrogestao.models.realmclasses.AtividadesEconomicas
import com.example.agrogestao.view.adapter.AtividadesAdapter
import com.example.agrogestao.viewmodel.navigation.ResultadosFazendaViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.realm.Realm
import kotlinx.android.synthetic.main.cadastro_atividade_dialog.view.*

/**
 * A simple [Fragment] subclass.
 */
class ResultadosFazendaFragment : Fragment() {

    private lateinit var resultadosFazendaViewModel: ResultadosFazendaViewModel
    val adapter = AtividadesAdapter()
    private var farmID = ""
    private lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (arguments?.get("id") != null) {
            val id = arguments?.getString("id")!!
            farmID = id
        }

        resultadosFazendaViewModel =
            ViewModelProvider(this).get(ResultadosFazendaViewModel::class.java)
        val binding: ResultadosFazendaBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.resultados_fazenda, container, false)
        root = binding.root
        binding.lifecycleOwner = this
        binding.viewModel = resultadosFazendaViewModel
        binding.recyclerAtividadesDetalhesFazenda.adapter = adapter
        observe(binding)

        resultadosFazendaViewModel.load(farmID)

        return root
    }

    private fun observe(binding: ResultadosFazendaBinding) {

        resultadosFazendaViewModel.myBalancoPatrimonial.observe(viewLifecycleOwner, Observer {
            val resultadoLucro = binding.resultadosLucroText
            val resultadoLiquida = binding.resultadosmLiquidaText
            val resultadoBruta = binding.resultadosmBrutaText
            resultadoLucro.text = "Lucro: ${String.format("%.2f", it.lucro)}"
            resultadoLiquida.text = "Margem líquida: ${String.format("%.2f", it.margemLiquida)}"
            resultadoBruta.text = "Margem bruta: ${String.format("%.2f", it.margemBruta)}"

        })

        resultadosFazendaViewModel.cadastrarAtividade.observe(viewLifecycleOwner, Observer {

            if (resultadosFazendaViewModel.inicio == 0) {
                resultadosFazendaViewModel.inicio = 1
            } else {
                criarAtividadeDialog()
            }
        })

        resultadosFazendaViewModel.carregarAtividade.observe(viewLifecycleOwner, Observer {
            adapter.submitList(resultadosFazendaViewModel.list)
            createBarChart()

        })

    }

    private fun createBarChart() {
        val chart: BarChart = root.findViewById(R.id.resultadoAtividadeChart)
        val entries = ArrayList<BarEntry>()
        val list = resultadosFazendaViewModel.list
        var contador = 0f
        for (item in list) {
            entries.add(BarEntry(contador, (item.vendasAtividade - item.custoDeProducao).toFloat()))
            contador += 1f
        }
        val set = BarDataSet(entries, "Resultado atividade.")
        val data = BarData(set)
        data.barWidth = 0.5f
        chart.data = data
        chart.setFitBars(true)
        chart.invalidate()

    }


    private fun criarAtividadeDialog() {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.cadastro_atividade_dialog, null)
        val cadastrarButton: Button = mDialogView.findViewById(R.id.cadastrarAtividadeButton)
        val cancelButton: Button = mDialogView.findViewById(R.id.cancelAtividadeButton)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("Cadastro de Atividade")
            .create()

        mBuilder.show()

        cadastrarButton.setOnClickListener {
            mBuilder.dismiss()
            val name = mDialogView.atividade_nome_cadastro.text.toString()
            val id = farmID
            val rateio = mDialogView.atividade_rateio_cadastro.text.toString().toDouble()
            val atividadesEconomicas =
                AtividadesEconomicas(
                    name
                )
            atividadesEconomicas.fazendaID = farmID
            atividadesEconomicas.rateio = rateio
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.copyToRealm(atividadesEconomicas)
            realm.commitTransaction()
            mBuilder.dismiss()
            resultadosFazendaViewModel.load(farmID)

        }
        cancelButton.setOnClickListener { mBuilder.dismiss() }


    }


}
