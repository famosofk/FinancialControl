package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.agrogestao.R
import com.example.agrogestao.models.BalancoPatrimonial
import com.example.agrogestao.viewmodel.BalancoPatrimonialViewModel

class BalancoPatrimonialFragment : Fragment() {

    private lateinit var balancoPatrimonialViewModel: BalancoPatrimonialViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        balancoPatrimonialViewModel =
            ViewModelProvider(this).get(BalancoPatrimonialViewModel::class.java)
        val root = inflater.inflate(R.layout.apresentacao_balanco_patrimonial, container, false)

        //incluir listagem dos bens


        return root
    }


    private fun popularTextos(it: BalancoPatrimonial, view: View) {

        val textPatrimonio = view.findViewById<TextView>(R.id.textPatrimonioBalanco)
        textPatrimonio.setText(it.patrimonioLiquido.toString())
        val textSolvencia = view.findViewById<TextView>(R.id.textSolvenciaBalanco)
        textSolvencia.setText(it.solvencia.toString())
        val textLiquidez = view.findViewById<TextView>(R.id.textLiquidezBalanco)
        textLiquidez.setText(it.liquidez.toString())
        val textRentabilidade = view.findViewById<TextView>(R.id.textRentabilidadeBalanco)
        textRentabilidade.setText(it.rentabilidade.toString())
        val textAtivo = view.findViewById<TextView>(R.id.textAtivoBalanco)
        textAtivo.setText(it.ativo.toString())
        val textPassivo = view.findViewById<TextView>(R.id.textPassivoBalanco)
        textPassivo.setText(it.passivo.toString())

        //falta configurar os gráficos

    }

}
