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
class CadastroFluxoCaixaFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var root: View
    private lateinit var realm: Realm
    private lateinit var fluxoCaixa: FluxoCaixa
    private lateinit var balancoPatrimonial: BalancoPatrimonial
    private var list = mutableListOf<String>()
    private var idItem: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.cadastro_fluxo_caixa, container, false)
        realm = Realm.getDefaultInstance()
        recuperarFluxoCaixa()

        val resultados =
            realm.where<ItemBalancoPatrimonial>().contains("tipo", "Benfeitoria").findAll()
        for (atividade in resultados) {
            list.add(atividade.nome)
        }

        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )
        }

        val spinner: Spinner = root.findViewById(R.id.tipoReformaSpinner)
        spinner.onItemSelectedListener = this
        val spinner2: Spinner = root.findViewById(R.id.objetoReformaSpinner)
        spinner2.adapter = adapter
        spinner2.onItemSelectedListener = this

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

        Toast.makeText(context, "chamou salvar", Toast.LENGTH_SHORT).show()

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
        val valorAmortizado: EditText =
            root.findViewById(R.id.valorAmortizadoCadastroItemFluxoCaixa)
        item.valorAmortizado = valorAmortizado.text.toString().toFloat()


        val switchReforma: Switch = root.findViewById(R.id.switchReforma)
        if (switchReforma.isChecked) {
            var itemInventario: ItemBalancoPatrimonial? =
                realm.where<ItemBalancoPatrimonial>().contains("idItem", idItem).findFirst()
            if (itemInventario != null) {
                realm.beginTransaction()
                itemInventario.reforma += item.valorInicial
                realm.commitTransaction()
            }

        }

        val switchPrazo: Switch = root.findViewById(R.id.switchPrazo)
        if (switchPrazo.isChecked) {
            val dataVencimento: EditText =
                root.findViewById(R.id.dataVencimentoCadastroItemFluxoCaixa)
            item.dataPagamentoPrazo = dataVencimento.text.toString()
        }

        realm.beginTransaction()
        fluxoCaixa.list.add(item)
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
        val layoutReforma: LinearLayout = root.findViewById(R.id.layoutReforma)
        switchReforma.setOnClickListener {
            if (switchReforma.isChecked) {
                layoutReforma.visibility = View.VISIBLE
            } else {
                layoutReforma.visibility = View.GONE
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
                if (listaOpcoes.count() > 0) {
                    list.clear()
                    for (itens in listaOpcoes) {
                        list.add(itens.nome)
                    }
                    val spinnerItens: Spinner = root.findViewById(R.id.objetoReformaSpinner)
                    val adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            list
                        )
                    }
                    spinnerItens.adapter = adapter
                } else {
                    Toast.makeText(
                        context,
                        "Não há itens desse tipo cadastrados",
                        Toast.LENGTH_SHORT
                    ).show()
                    list.clear()
                    val adapter = context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            list
                        )
                    }
                    val spinnerItens: Spinner = root.findViewById(R.id.objetoReformaSpinner)
                    spinnerItens.adapter = adapter
                }

            }
            if (parent.id == R.id.objetoReformaSpinner) {
                idItem = list[position]
            }
        }
    }

}
