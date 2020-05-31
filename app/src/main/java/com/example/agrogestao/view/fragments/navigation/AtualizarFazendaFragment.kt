package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agrogestao.R
import com.example.agrogestao.viewmodel.AtualizarFazendaViewModel

/**
 * A simple [Fragment] subclass.
 */
class AtualizarFazendaFragment : Fragment() {
    private lateinit var atualizarFazendaViewModel: AtualizarFazendaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        atualizarFazendaViewModel =
            ViewModelProvider(this).get(AtualizarFazendaViewModel::class.java)
        val root: View = inflater.inflate(R.layout.atualizar_fazenda, container, false)


        observer(root)

        return view
    }

    private fun observer(view: View) {
        atualizarFazendaViewModel.myFarm.observe(viewLifecycleOwner, Observer {

            val editCodigo = view.findViewById<EditText>(R.id.codigoFazendaEdit)
            editCodigo.setText(it.codigoFazenda)
            val areaFazenda = view.findViewById<EditText>(R.id.areaFazendaEdit)
            areaFazenda.setText(it.area.toString())
            val dividasLongoPrazo = view.findViewById<EditText>(R.id.dividaLongoPrazoFazendaEdit)
            dividasLongoPrazo.setText(it.dividaLongoPrazo.toString())
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
            val metaSolvencia = view.findViewById<EditText>(R.id.solvenciaFazendaEdit)
            metaSolvencia.setText(it.metaSolvencia.toString())
            val metaLiquidez = view.findViewById<EditText>(R.id.liquidezFazendaEdit)
            metaLiquidez.setText(it.metaLiquidez.toString())
        })

        atualizarFazendaViewModel.myBalanco.observe(viewLifecycleOwner, Observer {

            val remuneracaoCapital = view.findViewById<EditText>(R.id.taxaCapitalFazendaEdit)
            remuneracaoCapital.setText(it.taxaRemuneracaoCapital.toString())
            val custoOportunidade = view.findViewById<EditText>(R.id.custoTrabalhoFazendaEdit)
            custoOportunidade.setText(it.custoOportunidadeTrabalho.toString())


        })

    }

}
