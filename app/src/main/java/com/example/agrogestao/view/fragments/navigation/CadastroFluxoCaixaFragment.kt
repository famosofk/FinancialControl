package com.example.agrogestao.view.fragments.navigation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.agrogestao.R
import com.example.agrogestao.models.*
import io.realm.Realm
import io.realm.kotlin.where
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class CadastroFluxoCaixaFragment : Fragment(), AdapterView.OnItemSelectedListener,
    View.OnClickListener {
    private lateinit var root: View
    private lateinit var realm: Realm
    private lateinit var fluxoCaixa: FluxoCaixa
    private lateinit var balancoPatrimonial: BalancoPatrimonial
    private var listBenfeitoriasMaquinas = mutableListOf<String>()
    private var listBens = mutableListOf<String>()
    private var idItemReforma: String? = null
    private var idItemTransacao: String? = null
    private var controleEntradaSaida = false


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.cadastro_fluxo_caixa, container, false)
        realm = Realm.getDefaultInstance()
        recuperarFluxoCaixa()
        val buttonCompraVenda: ToggleButton = root.findViewById(R.id.entradaSaidaButton)
        buttonCompraVenda.setOnClickListener(this)

        val resultadosBenfeitoriasMaquinas =
            realm.where<ItemBalancoPatrimonial>().contains("tipo", "Benfeitoria").findAll()

        for (atividade in resultadosBenfeitoriasMaquinas) {
            listBenfeitoriasMaquinas.add(atividade.nome)
        }

        val adapterReformaObjeto = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                listBenfeitoriasMaquinas
            )
        }
        val resultadosTerra =
            realm.where<ItemBalancoPatrimonial>().contains("tipo", "Animais").findAll()
        for (itemBalancoo in resultadosTerra) {
            listBens.add(itemBalancoo.nome)
        }
        val adapterPropriedades = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                listBens
            )
        }


        val spinnerReformaTipo: Spinner = root.findViewById(R.id.tipoReformaSpinner)
        spinnerReformaTipo.onItemSelectedListener = this
        val spinnerReformaObjeto: Spinner = root.findViewById(R.id.objetoReformaSpinner)
        spinnerReformaObjeto.adapter = adapterReformaObjeto
        spinnerReformaObjeto.onItemSelectedListener = this

        val spinnerPropriedade: Spinner = root.findViewById(R.id.tipoPropriedadeSpinner)
        spinnerPropriedade.onItemSelectedListener = this
        val spinnerItemPropriedade: Spinner = root.findViewById(R.id.itemInventarioSpinner)
        spinnerItemPropriedade.onItemSelectedListener = this
        spinnerItemPropriedade.adapter = adapterPropriedades


        val date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val formatedDate = sdf.format(date)
        val editText: EditText = root.findViewById(R.id.dataCadastroItemFluxoCaixa)
        editText.setText(formatedDate)

        listenersAndVisibility()

        val button: Button = root.findViewById(R.id.cadastrarItemFluxoCaixa)
        button.setOnClickListener { salvarItem() }

        return root
    }

    private fun salvarItem() {

        val tipo: ToggleButton = root.findViewById(R.id.entradaSaidaButton)
        var item = ItemFluxoCaixa(tipo.isChecked)
        val nome: EditText = root.findViewById(R.id.nomeItemCadastrarFluxoCaixa)
        item.nome = nome.text.toString()
        val data: EditText = root.findViewById(R.id.dataCadastroItemFluxoCaixa)
        item.data = data.text.toString()
        val conta: EditText = root.findViewById(R.id.contaCadastroItemFluxoCaixa)
        item.conta = conta.text.toString()
        val quantidadeInicial: EditText =
            root.findViewById(R.id.quantidadeInicialCadastroItemFluxoCaixa)
        item.quantidadeInicial = quantidadeInicial.text.toString().toFloat()
        val valorInicial: EditText = root.findViewById(R.id.valorUnitarioCadastroItemFluxoCaixa)
        item.valorInicial = valorInicial.text.toString().toFloat()
        item.valorAtual = item.valorInicial
        val valorAmortizado: EditText =
            root.findViewById(R.id.valorAmortizadoCadastroItemFluxoCaixa)
        item.valorAmortizado = valorAmortizado.text.toString().toFloat()


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
            var itemInventario: ItemBalancoPatrimonial? =
                realm.where<ItemBalancoPatrimonial>().contains("idItem", idItemReforma).findFirst()
            if (itemInventario != null) {
                realm.beginTransaction()
                itemInventario.reforma += item.valorInicial
                realm.commitTransaction()
            }
        }
        val switchInventario: Switch = root.findViewById(R.id.switchPropriedade)

        realizarTransacao(item, switchInventario.isChecked)


    }

    private fun realizarTransacao(item: ItemFluxoCaixa, existente: Boolean) {


        if (existente) {
            if (verificarViabilidadeTransacao(item, controleEntradaSaida)) {
                processarMovimentacao(item)
            } else {
                Toast.makeText(context, "Impossível movimentar.", Toast.LENGTH_SHORT).show()
            }
        } else {
            processarMovimentacao(item)
        }


    }

    private fun verificarViabilidadeTransacao(item: ItemFluxoCaixa, venda: Boolean): Boolean {
        var bool = false
        if (venda) {
            var itemInventario: ItemBalancoPatrimonial? =
                realm.where<ItemBalancoPatrimonial>().contains("idItem", idItemTransacao!!)
                    .findFirst()
            if (controleEntradaSaida) {
                if (item.quantidadeInicial < itemInventario!!.quantidadeFinal) {
                    realm.beginTransaction()
                    itemInventario.quantidadeFinal -= item.quantidadeInicial
                    realm.commitTransaction()
                    bool = true
                }
            } else {
                realm.beginTransaction()
                itemInventario!!.quantidadeFinal += item.quantidadeInicial
                realm.commitTransaction()
                bool = true
            }
        } else {
            bool = true
        }
        return bool
    }

    private fun processarMovimentacao(item: ItemFluxoCaixa) {

        realm.beginTransaction()
        fluxoCaixa.list.add(item)


        /*  Eu adiciono em totaldespesas, porem se é a longo prazo, coloco em contas a pagar. Caso seja uma conta a pagar, isso deveria
          entrar no saldo? Se não deve entrar, não podemos somar o valor em totalDespesas, pois a função calcular saldo tem
            saldo = totalReceitas - totalDespesas */

        if (controleEntradaSaida) {
            //está saindo dinheiro
            balancoPatrimonial.totalDespesas += item.valorInicial * item.quantidadeInicial
            if (item.pagamentoPrazo) {
                balancoPatrimonial.totalContasPagar += item.valorInicial * item.quantidadeInicial
            } else {
                balancoPatrimonial.dinheiroBanco += item.valorInicial * item.quantidadeInicial
            }
        } else {
            //está entrando dinheiro
            balancoPatrimonial.totalReceitas += item.valorInicial * item.quantidadeInicial
            if (item.pagamentoPrazo) {
                balancoPatrimonial.totalContasReceber += item.valorInicial * item.quantidadeInicial
            } else {
                balancoPatrimonial.dinheiroBanco += item.valorInicial * item.quantidadeInicial
            }


        }

        balancoPatrimonial.atualizarBalanco()
        realm.commitTransaction()
    }


    private fun recuperarFluxoCaixa() {
        val registradorFarm = realm.where<RegistradorFarm>().findFirst()!!
        fluxoCaixa = realm.where<FluxoCaixa>().contains("farm", registradorFarm.id).findFirst()!!
        balancoPatrimonial =
            realm.where<BalancoPatrimonial>().contains("farm", registradorFarm.id).findFirst()!!
    }

    private fun listenersAndVisibility() {


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
                val realm = Realm.getDefaultInstance()
                val otherSeriesList =
                    ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.bensDepreciaveis)))
                val listaOpcoes = realm.where<ItemBalancoPatrimonial>()
                    .contains("tipo", otherSeriesList[position]).findAll()
                listBenfeitoriasMaquinas.clear()
                if (listaOpcoes.count() > 0) {
                    for (itens in listaOpcoes) {
                        listBenfeitoriasMaquinas.add(itens.nome)
                    }
                } else {
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
                if (listaOpcoes.size == 0) {
                    idItemReforma = null
                } else {
                    idItemReforma = listaOpcoes[0]!!.idItem
                }

            }
            if (parent.id == R.id.objetoReformaSpinner) {
                idItemReforma = listBenfeitoriasMaquinas[position]
            }

            if (parent.id == R.id.itemInventarioSpinner) {
                idItemTransacao = listBens[position]
            }

            if (parent.id == R.id.tipoPropriedadeSpinner) {
                val realm = Realm.getDefaultInstance()
                val otherSeriesList =
                    ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.bensCompraVenda)))
                val listaOpcoes = realm.where<ItemBalancoPatrimonial>()
                    .contains("tipo", otherSeriesList[position]).findAll()
                listBens.clear()
                if (listaOpcoes.count() > 0) {
                    for (itens in listaOpcoes) {
                        listBens.add(itens.nome)
                    }

                } else {
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
                if (listaOpcoes.size == 0) {
                    idItemTransacao = null
                } else {
                    idItemTransacao = listaOpcoes[0]!!.idItem
                }
            }


        }
    }

    override fun onClick(v: View?) {
        if (v != null) {

            when (v.id) {
                R.id.entradaSaidaButton -> {
                    controleEntradaSaida = !controleEntradaSaida
                    if (controleEntradaSaida) {
                        val informacoesReforma = root.findViewById<LinearLayout>(R.id.layoutReforma)
                        informacoesReforma.visibility = View.VISIBLE
                    } else {
                        val informacoesReforma = root.findViewById<LinearLayout>(R.id.layoutReforma)
                        informacoesReforma.visibility = View.GONE
                    }
                }
            }
        }
    }


}
