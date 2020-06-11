package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.agrogestao.R
import com.example.agrogestao.models.AtividadesEconomicas
import com.example.agrogestao.models.BalancoPatrimonial
import com.example.agrogestao.models.ItemBalancoPatrimonial
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class CadastroInventarioFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var id: String
    private var atividadeSelecionada: String = ""
    private var tipoSelecionado: String = "Terra"
    val list = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.cadastro_inventario, container, false)
        val realm = Realm.getDefaultInstance()
        val resultados = realm.where<AtividadesEconomicas>().findAll()
        for (atividade in resultados) {
            list.add(atividade.nome)
        }
        atividadeSelecionada = list[0]
        val adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )
        }

        val spinner: Spinner = root.findViewById(R.id.spinnerAtividadeItem)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
        val spinner2: Spinner = root.findViewById(R.id.spinnerTipoItem)
        spinner2.onItemSelectedListener = this


        if (arguments?.get("id") != null) {
            id = arguments?.getString("id")!!
            var balanco: BalancoPatrimonial =
                realm.where<BalancoPatrimonial>().contains("farm", id).findFirst()!!
            processamentoDados(root, balanco, realm)
        }




        return root
    }

    private fun processamentoDados(root: View, balanco: BalancoPatrimonial, realm: Realm) {

        val button: Button = root.findViewById(R.id.salvarItemInventario)

        button.setOnClickListener {
            var item = ItemBalancoPatrimonial()
            val name: EditText = root.findViewById(R.id.nomeItemInventario)
            item.nome = name.text.toString()
            val quantidadeInicial: EditText =
                root.findViewById(R.id.quantidadeInicialItemInventario)
            item.quantidadeInicial = quantidadeInicial.text.toString().trim().toFloat()
            val valorUnitario: EditText = root.findViewById(R.id.valorUnitarioItemInventario)
            item.valorUnitario = valorUnitario.text.toString().trim().toFloat()
            val anoCompra: EditText = root.findViewById(R.id.anoCompraItemInventario)
            item.anoProducao = anoCompra.text.toString().trim().toInt()
            val reforma: EditText = root.findViewById(R.id.reformaItemInventario)
            item.reforma = reforma.text.toString().trim().toFloat()
            val quantidadeAtual: EditText = root.findViewById(R.id.quantidadeAtualItemCadastro)
            item.quantidadeFinal = quantidadeAtual.text.toString().trim().toFloat()
            item.tipo = tipoSelecionado
            item.atividade = atividadeSelecionada

            realm.beginTransaction()
            balanco.listaItens.add(item)
            realm.commitTransaction()

            root.findNavController().navigate(R.id.from_cadastroInventario_to_apresentarFazenda)


        }


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            if (parent.id == R.id.spinnerTipoItem) {
                val otherSeriesList =
                    ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.tiposItem)))
                tipoSelecionado = otherSeriesList[position]
            }

            if (parent.id == R.id.spinnerAtividadeItem) {
                atividadeSelecionada = list[position]
            }
        }
    }


}
