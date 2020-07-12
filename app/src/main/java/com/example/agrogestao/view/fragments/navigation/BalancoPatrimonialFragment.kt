package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.BalancoPatrimonial
import com.example.agrogestao.view.adapter.ItemPatrimonioAdapter
import com.example.agrogestao.viewmodel.BalancoPatrimonialViewModel

class BalancoPatrimonialFragment : Fragment() {

    private lateinit var balancoPatrimonialViewModel: BalancoPatrimonialViewModel
    var id = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        balancoPatrimonialViewModel =
            ViewModelProvider(this).get(BalancoPatrimonialViewModel::class.java)
        val root = inflater.inflate(R.layout.apresentacao_balanco_patrimonial, container, false)
        if (arguments?.get("id") != null) {
            id = arguments?.getString("id")!!
        }
        balancoPatrimonialViewModel.load(id)

        observe(root)


        return root
    }


    private fun observe(view: View) {
        balancoPatrimonialViewModel.myBalancoPatrimonial.observe(viewLifecycleOwner, Observer {
            popularTextos(it, view)
            preencherRecycler(it, view)
        })
    }

    private fun popularTextos(it: BalancoPatrimonial, view: View) {

        val textPatrimonio = view.findViewById<TextView>(R.id.textPatrimonioBalanco)
        textPatrimonio.text = "Patrimônio líquido: " + it.patrimonioLiquido.toString()
        val textSolvencia = view.findViewById<TextView>(R.id.textSolvenciaBalanco)
        textSolvencia.text = "Solvência: " + it.liquidezGeral.toString()
        val textLiquidez = view.findViewById<TextView>(R.id.textLiquidezBalanco)
        textLiquidez.text = "Liquidez: " + it.liquidezCorrente.toString()
        val textRentabilidade = view.findViewById<TextView>(R.id.textRentabilidadeBalanco)
        textRentabilidade.text = "Rentabilidade: " + it.rentabilidade.toString()
        val textAtivo = view.findViewById<TextView>(R.id.textAtivoBalanco)
        textAtivo.text = "Ativo: " + it.ativo.toString()
        val textAtivoCirculante = view.findViewById<TextView>(R.id.textAtivoCirculanteBalanco)
        textAtivoCirculante.text =
            "Ativo circulante: " + (it.calcularValorAnimaisInsumosProdutos() + it.dinheiroBanco).toString()
        val textPassivo = view.findViewById<TextView>(R.id.textPassivoBalanco)
        textPassivo.text = "Passivo: " + it.passivo.toString()

        //falta configurar os gráficos

    }

    private fun preencherRecycler(it: BalancoPatrimonial, root: View) {
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerItensBalanco)
        val adapter = ItemPatrimonioAdapter()
        adapter.submitList(it.listaItens)
        recyclerView.adapter = adapter
    }

}
