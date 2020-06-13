package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.agrogestao.R
import com.example.agrogestao.models.BalancoPatrimonial
import com.example.agrogestao.models.FluxoCaixa
import com.example.agrogestao.models.ItemBalancoPatrimonial
import com.example.agrogestao.models.RegistradorFarm
import io.realm.Realm
import io.realm.kotlin.where
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.cadastro_fluxo_caixa, container, false)
        realm = Realm.getDefaultInstance()
        recuperarFluxoCaixa()

        val resultados = realm.where<ItemBalancoPatrimonial>().contains("tipo", "Terra").findAll()
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
        listenersAndVisibility()

        return root
    }

    private fun recuperarFluxoCaixa() {
        val registradorFarm = realm.where<RegistradorFarm>().findFirst()!!
        fluxoCaixa = realm.where<FluxoCaixa>().contains("farm", registradorFarm.id).findFirst()!!
        balancoPatrimonial =
            realm.where<BalancoPatrimonial>().contains("farm", registradorFarm.id).findFirst()!!
    }

    private fun listenersAndVisibility() {


        val switchPrazo: Switch = root.findViewById(R.id.switchPrazo);
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
                    ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.tiposItem)))
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

            }
        }
    }

}
