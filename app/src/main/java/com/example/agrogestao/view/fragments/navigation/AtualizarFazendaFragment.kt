package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.agrogestao.R
import com.example.agrogestao.models.BalancoPatrimonial
import com.example.agrogestao.models.Farm
import com.example.agrogestao.viewmodel.AtualizarFazendaViewModel
import io.realm.Realm
import io.realm.kotlin.where


class AtualizarFazendaFragment : Fragment() {

    private lateinit var atualizarFazendaViewModel: AtualizarFazendaViewModel
    private lateinit var id: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        atualizarFazendaViewModel =
            ViewModelProvider(this).get(AtualizarFazendaViewModel::class.java)
        val root: View = inflater.inflate(R.layout.atualizar_fazenda, container, false)
        val button: Button = root.findViewById(R.id.salvarFazendaButton)
        button.setOnClickListener {
            updateFarm(root)
        }

        observer(root)
        if (arguments?.get("id") != null) {
            id = arguments?.getString("id")!!
            atualizarFazendaViewModel.load(id)
        }


        return root
    }

    private fun observer(view: View) {
        atualizarFazendaViewModel.myFarm.observe(viewLifecycleOwner, Observer {

            val editCodigo = view.findViewById<EditText>(R.id.codigoFazendaEdit)
            editCodigo.setText(it.codigoFazenda)
            val areaFazenda = view.findViewById<EditText>(R.id.areaFazendaEdit)
            areaFazenda.setText(it.area.toString())
            val metaLucroFazenda = view.findViewById<EditText>(R.id.lucroFazendaEdit)
            metaLucroFazenda.setText(it.metaLucro.toString())
            val metaMargemLiquida = view.findViewById<EditText>(R.id.margemLiquidaFazendaEdit)
            metaMargemLiquida.setText(it.metaMargemLiquida.toString())
            val metaMargemBruta = view.findViewById<EditText>(R.id.margemBrutaFazendaEdit)
            metaMargemBruta.setText(it.metaMargemBruta.toString())
            val metaRendaBruta = view.findViewById<EditText>(R.id.rendaBrutaFazendaEdit)
            metaRendaBruta.setText(it.metaRendaBruta.toString())
            val metaSaldo = view.findViewById<EditText>(R.id.saldoFazendaEdit)
            metaSaldo.setText(it.metasaldo.toString())
            val metapagar = view.findViewById<EditText>(R.id.pagarFazendaEdit)
            metapagar.setText(it.metaTotalPagar.toString())
            val metareceber = view.findViewById<EditText>(R.id.receberFazendaEdit)
            metareceber.setText(it.metaTotalReceber.toString())
            val metaPatrimonioLiquido =
                view.findViewById<EditText>(R.id.patrimonioLiquidoFazendaEdit)
            metaPatrimonioLiquido.setText(it.metaPatrimonioLiquido.toString())
            val metaLiquidezGeraltext = view.findViewById<EditText>(R.id.liquidezGeralFazendaEdit)
            metaLiquidezGeraltext.setText(it.metaLiquidezGeral.toString())
            val metaLiquidez = view.findViewById<EditText>(R.id.liquidezCorrenteFazendaEdit)
            metaLiquidez.setText(it.metaLiquidezCorrente.toString())
        })

        atualizarFazendaViewModel.myBalanco.observe(viewLifecycleOwner, Observer {

            val remuneracaoCapital = view.findViewById<EditText>(R.id.taxaCapitalFazendaEdit)
            remuneracaoCapital.setText(it.taxaRemuneracaoCapital.toString())
            val custoOportunidade = view.findViewById<EditText>(R.id.custoTrabalhoFazendaEdit)
            custoOportunidade.setText(it.custoOportunidadeTrabalho.toString())
            val dividasLongoPrazo = view.findViewById<EditText>(R.id.dividaLongoPrazoFazendaEdit)
            dividasLongoPrazo.setText(it.dividasLongoPrazo.toString())


        })

    }

    private fun updateFarm(root: View) {
        val realm = Realm.getDefaultInstance()
        val farm = realm.where<Farm>().contains("id", id).findFirst()!!
        val balanco = realm.where<BalancoPatrimonial>().contains("farm", id).findFirst()!!
        val remuneracaoCapital = root.findViewById<EditText>(R.id.taxaCapitalFazendaEdit)
        val custoOportunidade = root.findViewById<EditText>(R.id.custoTrabalhoFazendaEdit)
        val dividasLP = root.findViewById<EditText>(R.id.dividaLongoPrazoFazendaEdit)
        val codigoFazenda = root.findViewById<EditText>(R.id.codigoFazendaEdit)
        val areaFazenda = root.findViewById<EditText>(R.id.areaFazendaEdit)
        val lucroFazenda = root.findViewById<EditText>(R.id.lucroFazendaEdit)
        val margemLiquidaFazenda = root.findViewById<EditText>(R.id.margemLiquidaFazendaEdit)
        val margemBrutaFazenda = root.findViewById<EditText>(R.id.margemBrutaFazendaEdit)
        val rendaBrutaFazenda = root.findViewById<EditText>(R.id.rendaBrutaFazendaEdit)
        val saldoFazenda = root.findViewById<EditText>(R.id.saldoFazendaEdit)
        val pagarFazenda = root.findViewById<EditText>(R.id.pagarFazendaEdit)
        val receberFazenda = root.findViewById<EditText>(R.id.receberFazendaEdit)
        val patrimonioLiquidoFazenda =
            root.findViewById<EditText>(R.id.patrimonioLiquidoFazendaEdit)
        val liquidezGeralFazenda = root.findViewById<EditText>(R.id.liquidezGeralFazendaEdit)
        val liquidezCorrenteFazenda = root.findViewById<EditText>(R.id.liquidezCorrenteFazendaEdit)
        realm.beginTransaction()

        farm.codigoFazenda = codigoFazenda.text.toString()
        farm.area = areaFazenda.text.toString().toFloat()
        farm.metaLucro = lucroFazenda.text.toString().toFloat()
        farm.metaMargemLiquida = margemLiquidaFazenda.text.toString().toFloat()
        farm.metaMargemBruta = margemBrutaFazenda.text.toString().toFloat()
        farm.metaRendaBruta = rendaBrutaFazenda.text.toString().toFloat()
        farm.metasaldo = saldoFazenda.text.toString().toFloat()
        farm.metaTotalPagar = pagarFazenda.text.toString().toFloat()
        farm.metaTotalReceber = receberFazenda.text.toString().toFloat()
        farm.metaPatrimonioLiquido = patrimonioLiquidoFazenda.text.toString().toFloat()
        farm.metaLiquidezGeral = liquidezGeralFazenda.text.toString().toFloat()
        farm.metaLiquidezCorrente = liquidezCorrenteFazenda.text.toString().toFloat()
        balanco.dividasLongoPrazo = dividasLP.text.toString().toFloat()
        balanco.taxaRemuneracaoCapital = remuneracaoCapital.text.toString().toFloat()
        balanco.custoOportunidadeTrabalho = custoOportunidade.text.toString().toFloat()

        realm.commitTransaction()

        root.findNavController().navigate(R.id.from_atualizar_to_Apresentar)


    }


}
