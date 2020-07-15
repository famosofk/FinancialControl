package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.agrogestao.R
import com.example.agrogestao.models.*
import io.realm.Realm
import io.realm.kotlin.where
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CadastroFluxoCaixaFragment : Fragment(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {


    private fun recuperarFluxoCaixa() {
        val registradorFarm = realm.where<RegistradorFarm>().findFirst()!!
        fluxoCaixa = realm.where<FluxoCaixa>().contains("farm", registradorFarm.id).findFirst()!!
        balancoPatrimonial =
            realm.where<BalancoPatrimonial>().contains("farm", registradorFarm.id).findFirst()!!
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

        switchsAndVisibility()

        val button: Button = root.findViewById(R.id.cadastrarItemFluxoCaixa)
        button.setOnClickListener { salvarItem() }

        return root
    }

    private fun salvarItem() {

        val tipo: ToggleButton = root.findViewById(R.id.entradaSaidaButton)
        val item = ItemFluxoCaixa(tipo.isChecked)
        item.atividade = atividade
        val nome: EditText = root.findViewById(R.id.nomeItemCadastrarFluxoCaixa)
        item.nome = nome.text.toString()
        val data: EditText = root.findViewById(R.id.dataCadastroItemFluxoCaixa)
        item.data = data.text.toString()

        val quantidadeInicial: EditText =
            root.findViewById(R.id.quantidadeInicialCadastroItemFluxoCaixa)
        if (quantidadeInicial.text.isNotEmpty()) {
            item.quantidadeInicial = quantidadeInicial.text.toString().toFloat()
        }

        val valorInicial: EditText = root.findViewById(R.id.valorUnitarioCadastroItemFluxoCaixa)
        if (valorInicial.text.isNotEmpty()) {
            item.valorInicial = valorInicial.text.toString().toFloat()
            item.valorAtual = item.valorInicial
        }


        val switchPrazo: Switch = root.findViewById(R.id.switchPrazo)
        if (switchPrazo.isChecked) {
            val dataVencimento: EditText =
                root.findViewById(R.id.dataVencimentoCadastroItemFluxoCaixa)
            item.dataPagamentoPrazo = dataVencimento.text.toString()
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
                itemInventario.reforma += item.valorInicial
                realm.commitTransaction()
            }
        }

        val switchConsumo: Switch = root.findViewById(R.id.switchConsumo)
        if (switchConsumo.isChecked) {
            item.valorAtual = 0f
        }

        val switchInventario: Switch = root.findViewById(R.id.switchPropriedade)
        realizarTransacao(item, switchInventario.isChecked)


    }

    private fun realizarTransacao(item: ItemFluxoCaixa, existente: Boolean) {


        if (existente) {
            if (verificarViabilidadeTransacao(item)) {
                processarMovimentacao(item)
            } else {
                Toast.makeText(context, "Impossível movimentar.", Toast.LENGTH_SHORT).show()
            }
        } else {
            processarMovimentacao(item)
        }


    }

    private fun verificarViabilidadeTransacao(item: ItemFluxoCaixa): Boolean {
        var bool = false
        val itemInventario: ItemBalancoPatrimonial? =
            realm.where<ItemBalancoPatrimonial>().contains("idItem", idItemTransacao!!)
                .findFirst()
        if (exitingMoney) { //compra
            realm.beginTransaction()
            itemInventario!!.valorUnitario =
                itemInventario.quantidadeFinal.times(itemInventario.valorAtual)
                    .plus(item.quantidadeInicial.plus(item.valorAtual)).div(
                        itemInventario.quantidadeFinal.plus(item.quantidadeInicial)
                    )
            itemInventario.quantidadeFinal += item.quantidadeInicial
            realm.commitTransaction()
            bool = true

        } else { //venda

            if (item.quantidadeInicial <= itemInventario!!.quantidadeFinal) {

                val switchConsumo: Switch = root.findViewById(R.id.switchConsumo)
                realm.beginTransaction()
                val atividade = listaAtividades[positionAtividades]
                if (switchConsumo.isChecked) {/*Aumentar o custo da atividade selecionada.*/
                    atividade.custoDeProducao += itemInventario.valorAtual * item.quantidadeInicial

                } else {/*aumentar o lucro da atividade */atividade.vendasAtividade += item.valorInicial * item.quantidadeInicial
                }
                itemInventario.quantidadeFinal -= item.quantidadeInicial
                realm.commitTransaction()
                bool = true
            }


        }
        return bool
    }

    private fun processarMovimentacao(item: ItemFluxoCaixa) {

        realm.beginTransaction()
        fluxoCaixa.list.add(item)

        if (exitingMoney) {
            if (item.pagamentoPrazo) {
                balancoPatrimonial.totalContasPagar += item.valorInicial * item.quantidadeInicial
            } else {
                balancoPatrimonial.totalDespesas += item.valorInicial * item.quantidadeInicial
                balancoPatrimonial.dinheiroBanco -= item.valorInicial * item.quantidadeInicial
                val atividade = listaAtividades[positionAtividades]
                when (positionTipoDespesa) {
                    0 -> {
                        atividade.custoSemente += 0
                    }
                    1 -> {
                        atividade.custoFertilizante += 0
                    }
                    2 -> {
                        atividade.custoMaodeobra += 0
                    }
                    3 -> {
                        atividade.custoMaquina += 0
                    }
                    4 -> {
                        atividade.custoOutros += 0
                    }
                }
            }
        } else {
            if (item.pagamentoPrazo) {
                balancoPatrimonial.totalContasReceber += item.valorInicial * item.quantidadeInicial
            } else {
                balancoPatrimonial.totalReceitas += item.valorAtual * item.quantidadeInicial
                balancoPatrimonial.dinheiroBanco += item.valorAtual * item.quantidadeInicial
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
                val realm = Realm.getDefaultInstance()
                val otherSeriesList =
                    ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.bensCompraVenda)))
                val listaItens = mutableListOf<ItemBalancoPatrimonial>()
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
    private var exitingMoney = false
    private var farmID = ""
    private var atividade = ""
    private var listaAtividades = mutableListOf<AtividadesEconomicas>()
    private var positionAtividades = 0
    private var positionTipoDespesa = 0

}
