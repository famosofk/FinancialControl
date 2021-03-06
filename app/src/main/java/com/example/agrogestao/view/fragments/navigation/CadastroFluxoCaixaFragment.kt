package com.example.agrogestao.view.fragments.navigation

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.agrogestao.R
import com.example.agrogestao.models.ItemBalancoPatrimonial
import com.example.agrogestao.models.ItemFluxoCaixa
import com.example.agrogestao.models.RegistradorFarm
import com.example.agrogestao.models.realmclasses.AtividadesEconomicas
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import com.example.agrogestao.models.realmclasses.FluxoCaixa
import io.realm.Realm
import io.realm.kotlin.where
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class CadastroFluxoCaixaFragment : Fragment(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {


    private fun recuperarFluxoCaixa() {
        val registradorFarm = realm.where<RegistradorFarm>().findFirst()!!
        fluxoCaixa = realm.where<FluxoCaixa>().contains("farmID", registradorFarm.id).findFirst()!!
        balancoPatrimonial =
            realm.where<BalancoPatrimonial>().contains("farmID", registradorFarm.id).findFirst()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        if (arguments?.get("id") != null) {
            val id = arguments?.getString("id")!!
            farmID = id
        }

        root = inflater.inflate(R.layout.cadastro_fluxo_caixa, container, false)
        realm = Realm.getDefaultInstance()
        recuperarFluxoCaixa()

        val buttonCompraVenda: ToggleButton = root.findViewById(R.id.entradaSaidaButton)
        buttonCompraVenda.setOnClickListener(this)

        spinnerConfiguration()

        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val formatedDate = sdf.format(date)
        val editText: EditText = root.findViewById(R.id.dataCadastroItemFluxoCaixa)
        editText.setText(formatedDate)
        val calendar: CalendarView = root.findViewById(R.id.dataVencimentoPrazoCadastroFluxoCaixa)
        calendar.setOnDateChangeListener { _, year, month, day ->
            selectedDay = day
            selectedMonth = month + 1
            selectedYear = year
        }

        switchsAndVisibility()

        val button: Button = root.findViewById(R.id.cadastrarItemFluxoCaixa)
        button.setOnClickListener { confirmarItem() }

        return root
    }

    private fun confirmarItem() {

        val mDialogView =
            LayoutInflater.from(context).inflate(R.layout.observacao_dialog_layout, null)
        val dontshowButton: Button = mDialogView.findViewById(R.id.dontshow_observacao)
        val message: TextView = mDialogView.findViewById(R.id.message_farm)
        val title: TextView = mDialogView.findViewById(R.id.title_dialog_view)
        title.text = "Verificar movimentação?"
        message.text =
            "Você tem certeza que deseja cadastrar essa movimentação? Não será possível edita-la. Tenha certeza que todos os itens estão" +
                    "certos. Para conferir, clique fora deste popup e verifique os campos preenchidos. Alterações não são permitidas."
        dontshowButton.text = "Cadastrar movimentação!!"
        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
            .create()

        mBuilder.show()

        dontshowButton.setOnClickListener {
            salvarItem()
            mBuilder.dismiss()
        }


    }

    private fun salvarItem() {

        val tipo: ToggleButton = root.findViewById(R.id.entradaSaidaButton)
        val item = ItemFluxoCaixa(tipo.isChecked)
        item.atividade = atividade
        val nome: EditText = root.findViewById(R.id.nomeItemCadastrarFluxoCaixa)
        item.nome = nome.text.toString()
        val dataText: EditText = root.findViewById(R.id.dataCadastroItemFluxoCaixa)
        item.data = dataText.text.toString()
        val quantidadeInicial: EditText =
            root.findViewById(R.id.quantidadeInicialCadastroItemFluxoCaixa)
        if (quantidadeInicial.text.isNotEmpty()) {
            item.quantidadeInicial = quantidadeInicial.text.toString().toInt()
        } else {
            item.quantidadeInicial = 1
        }
        val valorInicial: EditText = root.findViewById(R.id.valorUnitarioCadastroItemFluxoCaixa)
        if (valorInicial.text.isNotEmpty()) {
            item.valorInicial = valorInicial.text.toString()
            item.valorAtual = item.valorInicial
        } else {
            item.valorInicial = "0"
            item.valorAtual = "0"
        }
        val switchPrazo: Switch = root.findViewById(R.id.switchPrazo)
        if (switchPrazo.isChecked) {
            item.dataPagamentoPrazo = " $selectedDay/${selectedMonth}/$selectedYear"
            item.pagamentoPrazo = true
        }

        val switchReforma: Switch = root.findViewById(R.id.switchReforma)
        if (switchReforma.isChecked) {
            item.reforma = true
            val itemInventario: ItemBalancoPatrimonial? =
                realm.where<ItemBalancoPatrimonial>().contains("idItem", idItemReforma!!)
                    .findFirst()
            if (itemInventario != null) {
                realm.beginTransaction()
                itemInventario.reforma =
                    (itemInventario.reforma.toDouble() + item.valorInicial.toDouble()).toString()
                realm.commitTransaction()
            }
        }

        val switchConsumo: Switch = root.findViewById(R.id.switchConsumo)
        if (switchConsumo.isChecked) {
            item.valorAtual = "0.00"
        }

        val switchInventario: Switch = root.findViewById(R.id.switchPropriedade)

        realizarTransacao(item, switchInventario.isChecked)
        Log.e("aqui", item.toString())


    }

    private fun realizarTransacao(item: ItemFluxoCaixa, existente: Boolean) {

        if (verificarViabilidadeTransacao(item, existente)) {
            processarMovimentacao(item)
        } else {
            Toast.makeText(context, "Impossível movimentar.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun verificarViabilidadeTransacao(item: ItemFluxoCaixa, existente: Boolean): Boolean {
        var bool = false
        val atividade = listaAtividades[positionAtividades]
        var position by Delegates.notNull<Int>()
        if (item.pagamentoPrazo) {
            position = selectedMonth - 1
        } else {
            val array = item.data.trim().split("/")
            position = array[1].toInt() - 1
        }

        if (existente) {
            val itemInventario: ItemBalancoPatrimonial? =
                realm.where<ItemBalancoPatrimonial>().contains("idItem", idItemTransacao!!)
                    .findFirst()
            val custoOperacao =
                itemInventario!!.valorAtual.toBigDecimal() * item.quantidadeInicial.toBigDecimal()
            realm.beginTransaction()
            if (exitingMoney) {
                Log.e("aqui:", "" + Integer.parseInt(item.valorAtual))
                if (Integer.parseInt(item.valorAtual) != 0) {
                    Toast.makeText(context, "Entrou", Toast.LENGTH_SHORT).show()

                    itemInventario.valorUnitario = itemInventario.quantidadeFinal.toDouble()
                        .times(itemInventario.valorAtual.toDouble())
                        .plus(item.quantidadeInicial.times(item.valorInicial.toDouble()))
                        .div(itemInventario.quantidadeFinal + item.quantidadeInicial)
                        .toString()
                    itemInventario.quantidadeInicial = itemInventario.quantidadeFinal
                    itemInventario.valorInicial = itemInventario.valorUnitario
                    itemInventario.valorAtual = itemInventario.valorInicial
                }

                itemInventario.quantidadeFinal += item.quantidadeInicial
                item.anoProducao = itemInventario.anoProducao
                atividade.custoDeProducao += custoOperacao.toDouble()
                atividade.arrayCustos[position] =
                    atividade.arrayCustos[position]!! - custoOperacao.toDouble()
                atividade.attModificacao()
                atividade.atualizado = true

                bool = true
            } else {
                if (item.quantidadeInicial <= itemInventario.quantidadeFinal) {
                    val switchConsumo: Switch = root.findViewById(R.id.switchConsumo)
                    if (switchConsumo.isChecked) {
                        balancoPatrimonial.totalDespesas =
                            (balancoPatrimonial.totalDespesas.toBigDecimal() + custoOperacao).toString()
                        atividade.custoDeProducao += custoOperacao.toDouble()
                        atividade.arrayCustos[position] =
                            atividade.arrayCustos[position]!! - custoOperacao.toDouble()
                        item.valorInicial = "0.00"
                        item.valorAtual = item.valorInicial
                    } else {
                        atividade.vendasAtividade += custoOperacao.toDouble()
                        atividade.arrayCustos[position] =
                            atividade.arrayCustos[position]!! + custoOperacao.toDouble()
                    }
                    itemInventario.quantidadeFinal -= item.quantidadeInicial
                    item.anoProducao = itemInventario.anoProducao
                    bool = true


                }
            }
            realm.commitTransaction()
        } else {
            val custoOperacao = item.valorInicial.toDouble() * item.quantidadeInicial
            realm.beginTransaction()
            if (exitingMoney) {

                atividade.custoDeProducao += custoOperacao
                atividade.arrayCustos[position] = atividade.arrayCustos[position]!! - custoOperacao
            } else {
                atividade.vendasAtividade += custoOperacao
                atividade.arrayCustos[position] = atividade.arrayCustos[position]!! + custoOperacao

            }
            atividade.attModificacao()
            realm.commitTransaction()
            bool = true
        }

        return bool
    }

    private fun processarMovimentacao(item: ItemFluxoCaixa) {
        realm.beginTransaction()
        fluxoCaixa.list.add(item)
        fluxoCaixa.attModificacao()
        balancoPatrimonial.attModificacao()
        val custoOperacao = item.valorInicial.toDouble() * item.quantidadeInicial
        val atividade = listaAtividades[positionAtividades]
        if (exitingMoney) {
            if (item.pagamentoPrazo) {
                balancoPatrimonial.totalContasPagar =
                    (balancoPatrimonial.totalContasPagar.toDouble() + custoOperacao).toString()
            } else {
                balancoPatrimonial.totalDespesas =
                    (balancoPatrimonial.totalDespesas.toBigDecimal() + custoOperacao.toBigDecimal()).toString()
                balancoPatrimonial.dinheiroBanco =
                    (balancoPatrimonial.dinheiroBanco.toDouble() - custoOperacao).toString()

                when (positionTipoDespesa) {
                    0 -> {
                        atividade.custoSemente += custoOperacao
                    }
                    1 -> {
                        atividade.custoFertilizante += custoOperacao
                    }
                    2 -> {
                        atividade.custoDefensivo += custoOperacao
                    }
                    3 -> {
                        atividade.custoMaodeobra += custoOperacao
                    }
                    4 -> {
                        atividade.custoMaquina += custoOperacao
                    }
                    5 -> {
                        atividade.custoOutros += custoOperacao
                    }
                }
            }
        } else {
            if (item.pagamentoPrazo) {
                balancoPatrimonial.totalContasReceber =
                    (balancoPatrimonial.totalContasReceber.toDouble() + custoOperacao).toString()
            } else {
                //Adicionado para caso o item tenha sido produzido em outros anos só aumentar o dinheiro no banco.
                if (item.anoProducao == 2020) {
                    balancoPatrimonial.totalReceitas =
                        (balancoPatrimonial.totalReceitas.toDouble() + custoOperacao).toString()
                    balancoPatrimonial.dinheiroBanco =
                        (balancoPatrimonial.dinheiroBanco.toDouble() + custoOperacao).toString()
                } else {


                    balancoPatrimonial.dinheiroBanco =
                        (balancoPatrimonial.dinheiroBanco.toDouble() + custoOperacao).toString()
                }
            }
        }
        balancoPatrimonial.atualizarBalanco()
        realm.commitTransaction()
        root.findNavController().navigate(R.id.from_cadastrofluxo_to_nav_fazenda)
    }




    private fun switchsAndVisibility() {


        val switchPrazo: Switch = root.findViewById(R.id.switchPrazo)
        switchPrazo.setOnClickListener {
            val layoutVencimento: LinearLayout = root.findViewById(R.id.layoutVencimento)
            if (switchPrazo.isChecked) {
                layoutVencimento.visibility = View.VISIBLE
            } else {
                layoutVencimento.visibility = View.GONE
            }
        }

        val switchReforma: Switch = root.findViewById(R.id.switchReforma)
        val layoutReforma: LinearLayout = root.findViewById(R.id.layoutInventario)
        switchReforma.setOnClickListener {
            if (switchReforma.isChecked) {
                layoutReforma.visibility = View.VISIBLE
            } else {
                layoutReforma.visibility = View.GONE
            }
        }

        val switchPropriedade: Switch = root.findViewById(R.id.switchPropriedade)
        val layoutPropriedade: LinearLayout = root.findViewById(R.id.layoutPropriedade)
        switchPropriedade.setOnClickListener {
            if (switchPropriedade.isChecked) {
                layoutPropriedade.visibility = View.VISIBLE
            } else {
                layoutPropriedade.visibility = View.GONE
            }
        }


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (parent != null) {

            if (parent.id == R.id.tipoReformaSpinner) {
                val otherSeriesList =
                    ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.bensDepreciaveis)))
                val listaItens = mutableListOf<ItemBalancoPatrimonial>()
                listBenfeitoriasMaquinas.clear()
                for (item in balancoPatrimonial.listaItens) {
                    if (item.tipo.equals(otherSeriesList[position])) {
                        listaItens.add(item)
                        listBenfeitoriasMaquinas.add(item.nome)
                    }
                }
                if (listBenfeitoriasMaquinas.size == 0) {
                    Toast.makeText(
                        context,
                        "Não há itens desse tipo cadastrados",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val adapter = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item,
                        listBenfeitoriasMaquinas
                    )
                }
                val spinnerItens: Spinner = root.findViewById(R.id.objetoReformaSpinner)
                spinnerItens.adapter = adapter
                if (listBenfeitoriasMaquinas.size == 0) {
                    idItemReforma = null
                } else {
                    idItemReforma = listaItens[0].idItem //era 0
                }

            }

            if (parent.id == R.id.objetoReformaSpinner) {
                for (item in balancoPatrimonial.listaItens) {
                    if (item.nome.equals(listBenfeitoriasMaquinas[position])) {
                        idItemReforma = item.idItem
                    }
                }
            }

            if (parent.id == R.id.itemInventarioSpinner) {
                for (item in balancoPatrimonial.listaItens) {
                    if (item.nome.equals(listBens[position])) {
                        idItemTransacao = item.idItem
                    }
                }

            }

            if (parent.id == R.id.tipoPropriedadeSpinner) {
                val otherSeriesList =
                    ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.bensCompraVenda)))
                val listaItens = mutableListOf<ItemBalancoPatrimonial>()
                listBens.clear()
                for (item in balancoPatrimonial.listaItens) {
                    if (item.tipo.equals(otherSeriesList[position])) {
                        listaItens.add(item)
                        listBens.add(item.nome)
                    }
                }
                if (listaItens.size == 0) {
                    Toast.makeText(
                        context,
                        "Não há itens desse tipo cadastrados",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val adapterBens = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_dropdown_item,
                        listBens
                    )
                }
                val spinnerItens: Spinner = root.findViewById(R.id.itemInventarioSpinner)
                spinnerItens.adapter = adapterBens
                if (listaItens.size == 0) {
                    idItemTransacao = null
                } else {
                    idItemTransacao = listaItens[0].idItem //era 0
                }
            }

            if (parent.id == R.id.contaCadastroItemFluxoCaixa) {
                positionAtividades = position
            }
            if (parent.id == R.id.tipoDespesaFluxoCaixa) {
                positionTipoDespesa = position

            }
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {

            when (v.id) {
                R.id.entradaSaidaButton -> {
                    exitingMoney = !exitingMoney
                    val informacoesReforma = root.findViewById<LinearLayout>(R.id.layoutReforma)
                    val switchConsumo = root.findViewById<Switch>(R.id.switchConsumo)
                    val tipoMovimentacao = root.findViewById<Spinner>(R.id.tipoDespesaFluxoCaixa)
                    val textMovimentacao = root.findViewById<TextView>(R.id.tipoDespesaText)

                    if (exitingMoney) {
                        tipoMovimentacao.visibility = View.VISIBLE
                        textMovimentacao.visibility = View.VISIBLE
                        switchConsumo.visibility = View.GONE
                        informacoesReforma.visibility = View.VISIBLE

                    } else {
                        tipoMovimentacao.visibility = View.GONE
                        textMovimentacao.visibility = View.GONE
                        informacoesReforma.visibility = View.GONE
                        switchConsumo.visibility = View.VISIBLE

                    }
                }
            }
        }
    }


    private fun spinnerConfiguration() {


        for (item in balancoPatrimonial.listaItens) {
            if (item.tipo.equals("Benfeitoria")) {
                listBenfeitoriasMaquinas.add(item.nome)
            }
        }

        val adapterReformaObjeto = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                listBenfeitoriasMaquinas
            )
        }

        val spinnerReformaTipo: Spinner = root.findViewById(R.id.tipoReformaSpinner)
        spinnerReformaTipo.onItemSelectedListener = this
        val spinnerTipoDespesa: Spinner = root.findViewById(R.id.tipoDespesaFluxoCaixa)
        spinnerTipoDespesa.onItemSelectedListener = this
        val spinnerReformaObjeto: Spinner = root.findViewById(R.id.objetoReformaSpinner)
        spinnerReformaObjeto.adapter = adapterReformaObjeto
        spinnerReformaObjeto.onItemSelectedListener = this


        for (itemBalanco in balancoPatrimonial.listaItens) {
            if (itemBalanco.tipo.equals("Animais")) {
                listBens.add(itemBalanco.nome)
            }
        }
        val adapterProperties = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                listBens
            )
        }


        val spinnerPropriedade: Spinner = root.findViewById(R.id.tipoPropriedadeSpinner)
        spinnerPropriedade.onItemSelectedListener = this
        val spinnerItemPropriedade: Spinner = root.findViewById(R.id.itemInventarioSpinner)
        spinnerItemPropriedade.onItemSelectedListener = this
        spinnerItemPropriedade.adapter = adapterProperties

        listaAtividades =
            realm.where<AtividadesEconomicas>().contains("fazendaID", farmID).findAll()
        val listAtividadesEconomicas = mutableListOf<String>()
        for (atividade in listaAtividades) {
            listAtividadesEconomicas.add(atividade.nome)
        }

        val adapterAtividadesEconomicas = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                listAtividadesEconomicas
            )
        }
        val spinnerAtividadesEconomicas: Spinner =
            root.findViewById(R.id.contaCadastroItemFluxoCaixa)
        spinnerAtividadesEconomicas.adapter = adapterAtividadesEconomicas
        atividade = listAtividadesEconomicas[0]
        spinnerAtividadesEconomicas.onItemSelectedListener = this


    }

    private lateinit var root: View
    private lateinit var realm: Realm
    private lateinit var fluxoCaixa: FluxoCaixa
    private lateinit var balancoPatrimonial: BalancoPatrimonial
    private var listBenfeitoriasMaquinas = mutableListOf<String>()
    private var listBens = mutableListOf<String>()
    private var idItemReforma: String? = null
    private var idItemTransacao: String? = null
    private var exitingMoney = true
    private var farmID = ""
    private var atividade = ""
    private var listaAtividades = mutableListOf<AtividadesEconomicas>()
    private var positionAtividades = 0
    private var positionTipoDespesa = 0
    private var selectedYear = -1
    private var selectedMonth = -1
    private var selectedDay = -1

}
