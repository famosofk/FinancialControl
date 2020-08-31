package com.example.agrogestao.view.fragments.navigation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.ItemFluxoCaixa
import com.example.agrogestao.models.realmclasses.AtividadesEconomicas
import com.example.agrogestao.view.adapter.FluxoCaixaAdapter
import com.example.agrogestao.view.adapter.ItemFluxoCaixaListener
import com.example.agrogestao.viewmodel.navigation.FluxoCaixaViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.realm.Realm
import io.realm.kotlin.where


class FluxoCaixaFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var fluxoCaixaViewModel: FluxoCaixaViewModel
    private val adapterVista = FluxoCaixaAdapter(ItemFluxoCaixaListener { })
    private val adapterPrazo =
        FluxoCaixaAdapter(ItemFluxoCaixaListener { criarAtividadeDialog(it) })
    private val arrayAtividades = ArrayList<String>()
    private lateinit var geral: AtividadesEconomicas
    private lateinit var atividadeSelecionada: AtividadesEconomicas
    var id: String = ""
    private lateinit var root: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fluxoCaixaViewModel =
            ViewModelProvider(this).get(FluxoCaixaViewModel::class.java)
        root = inflater.inflate(R.layout.fluxo_caixa_atividade, container, false)

        id = arguments?.getString("id")!!
        atualizarListas()

        val recyclerVista = root.findViewById<RecyclerView>(R.id.recyclerFluxoCaixaVista)
        recyclerVista.adapter = adapterVista
        val recyclerPrazo = root.findViewById<RecyclerView>(R.id.recyclerFluxoCaixaPrazo)
        recyclerPrazo.adapter = adapterPrazo

        observe(root)
        return root
    }

    private fun createBarChart(position: Int = 0) {
        val entries = ArrayList<BarEntry>()
        var contador = 0f
        val chart: BarChart = root.findViewById(R.id.fluxoCaixaChart)
        if (position == 0) {
            for (item in geral.arrayCustos)
                if (contador < 6) {
                    entries.add(BarEntry(contador + 7f, item.toFloat()))
                    contador += 1f
                } else {
                    entries.add(BarEntry(contador - 5f, item.toFloat()))
                    contador += 1f
                }
            atividadeSelecionada = geral
        } else {
            for (item in atividadeSelecionada.arrayCustos) {
                if (contador < 6) {
                    entries.add(BarEntry(
                        contador + 7f,
                        (item + geral.arrayCustos[contador.toInt()]!!.times(atividadeSelecionada.rateio)
                            .div(100)).toFloat()
                    )
                    )
                    contador += 1f
                } else {
                    entries.add(BarEntry(
                        contador - 5f,
                        (item + geral.arrayCustos[contador.toInt()]!!.times(atividadeSelecionada.rateio)
                            .div(100)).toFloat()
                    )
                    )
                    contador += 1f
                }
            }
        }
        val set = BarDataSet(entries, "Resultado ${atividadeSelecionada.nome}")
        val data = BarData(set)
        chart.data = data
        chart.invalidate()


    }

    private fun atualizarListas() {
        fluxoCaixaViewModel.load(id)
    }

    private fun observe(view: View) {

        fluxoCaixaViewModel.myAtividades.observe(viewLifecycleOwner, Observer {

            geral = fluxoCaixaViewModel.myAtividades.value!![0]
            for (item in fluxoCaixaViewModel.myAtividades.value!!) {
                arrayAtividades.add(item.nome)
            }
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayAtividades
                )
            }
            val spinner: Spinner = root.findViewById(R.id.spinnerResultadoFluxoCaixa)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this

        })

        fluxoCaixaViewModel.myBalancoPatrimonial.observe(viewLifecycleOwner, Observer {


            val saldo = view.findViewById<TextView>(R.id.fcaixaSaldoText)
            saldo.text = "Saldo: ${it.saldo}"
            val receitas = view.findViewById<TextView>(R.id.fcaixaReceitasText)
            receitas.text = "Receitas: ${it.totalReceitas}"
            val despesas = view.findViewById<TextView>(R.id.fcaixaDespesasText)
            despesas.text = "Despesas: ${it.totalDespesas}"
            val pagar = view.findViewById<TextView>(R.id.fcaixaPagarText)
            pagar.text = "Contas a pagar: ${it.totalContasPagar}"
            val receber = view.findViewById<TextView>(R.id.fcaixaReceberText)
            receber.text = "Contas a receber: ${it.totalContasReceber}"
            val lucro = view.findViewById<TextView>(R.id.fcaixaLucroText)
            lucro.text = "Lucro: ${it.lucro}"

        })
        fluxoCaixaViewModel.myFluxoCaixa.observe(viewLifecycleOwner, Observer {
            fluxoCaixaViewModel.loadLists("Geral")
        })
        fluxoCaixaViewModel.listaVista.observe(viewLifecycleOwner, Observer {
            adapterVista.submitList(it)
        })
        fluxoCaixaViewModel.listaPrazo.observe(viewLifecycleOwner, Observer {
            adapterPrazo.submitList(it)
        })
    }


    private fun criarAtividadeDialog(id: String) {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.pagar_pendencia_dialog, null)
        val confirmarButton: Button = mDialogView.findViewById(R.id.pagarPendenciaButton)
        val cancelButton: Button = mDialogView.findViewById(R.id.cancelarPagarPendencia)
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .setTitle("Pagar/Receber pendência?")
            .create()

        mBuilder.show()

        confirmarButton.setOnClickListener {
            mBuilder.dismiss()
            val realm = Realm.getDefaultInstance()
            val item = realm.where<ItemFluxoCaixa>().contains("itemID", id).findFirst()
            realm.beginTransaction()
            item?.pagamentoPrazo = false
            val balanco = fluxoCaixaViewModel.myBalancoPatrimonial.value!!
            if (item?.tipo!!) {
                balanco.totalContasPagar =
                    (balanco.totalContasPagar.toDouble() - item.quantidadeInicial * item.valorInicial.toDouble()).toString()
                balanco.dinheiroBanco =
                    (balanco.dinheiroBanco.toDouble() - item.quantidadeInicial * item.valorInicial.toDouble()).toString()
                balanco.totalDespesas =
                    (balanco.totalDespesas.toBigDecimal() + item.quantidadeInicial.toBigDecimal() * item.valorInicial.toBigDecimal()).toString()
            } else {
                balanco.totalContasReceber =
                    (balanco.totalContasReceber.toDouble() - item.quantidadeInicial * item.valorInicial.toDouble()).toString()
                balanco.dinheiroBanco =
                    (balanco.dinheiroBanco.toDouble() + item.quantidadeInicial * item.valorInicial.toDouble()).toString()
                balanco.totalReceitas =
                    (balanco.totalReceitas.toBigDecimal() + item.quantidadeInicial.toBigDecimal() * item.valorInicial.toBigDecimal()).toString()
            }
            balanco.atualizarBalanco()
            realm.commitTransaction()

            fluxoCaixaViewModel.loadLists(atividadeSelecionada.nome)
            mBuilder.dismiss()


        }
        cancelButton.setOnClickListener { mBuilder.dismiss() }


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p2 > 0) {
            atividadeSelecionada = fluxoCaixaViewModel.myAtividades.value!![p2]
        }
        createBarChart(p2)
    }


}
