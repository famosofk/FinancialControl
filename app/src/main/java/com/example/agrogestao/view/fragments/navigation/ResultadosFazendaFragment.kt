package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.agrogestao.R
import com.example.agrogestao.viewmodel.ResultadosFazendaViewModel

/**
 * A simple [Fragment] subclass.
 */
class ResultadosFazendaFragment : Fragment() {

    private lateinit var resultadosFazendaViewModel: ResultadosFazendaViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultadosFazendaViewModel =
            ViewModelProvider(this).get(ResultadosFazendaViewModel::class.java)
        val root: View = inflater.inflate(R.layout.resultados_fazenda, container, false)

        observe(root)

        if (arguments?.get("id") != null) {
            val id = arguments?.getString("id")!!
            Toast.makeText(context, id, Toast.LENGTH_SHORT).show()

        }

        return root
    }

    private fun observe(view: View) {

        resultadosFazendaViewModel.myBalancoPatrimonial.observe(viewLifecycleOwner, Observer {
            val resultadoLucro = view.findViewById<TextView>(R.id.resultadosLucroText)
            val resultadoLiquida = view.findViewById<TextView>(R.id.resultadosmLiquidaText)
            val resultadoBruta = view.findViewById<TextView>(R.id.resultadosmBrutaText)
            resultadoLucro.setText(it.lucro.toString())
            resultadoLiquida.setText(it.margemLiquida.toString())
            resultadoBruta.setText(it.margemBruta.toString())


        })

    }

}
