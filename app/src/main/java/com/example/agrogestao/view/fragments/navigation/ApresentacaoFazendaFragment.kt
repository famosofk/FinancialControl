package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.agrogestao.R
import com.example.agrogestao.viewmodel.ApresentacaoFazendaViewModel

class ApresentacaoFazendaFragment : Fragment() {

    private lateinit var apresentacaoFazendaViewModel: ApresentacaoFazendaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        apresentacaoFazendaViewModel =
            ViewModelProvider(this).get(ApresentacaoFazendaViewModel::class.java)
        val root = inflater.inflate(R.layout.apresentacao_fazenda, container, false)
        inicializarListeners(root)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun inicializarListeners(root: View) {

        val indicadoresFinanceirosAtividade =
            root.findViewById<Button>(R.id.indicadoresFinanceirosAtividadeButton)
        val indicadoresFinanceirosDetalhes =
            root.findViewById<Button>(R.id.indicadoresFinanceirosDetalhes)
        val fluxoCaixaAtividade = root.findViewById<Button>(R.id.fluxoCaixaAtividade)
        val fluxoCaixaDetalhes = root.findViewById<Button>(R.id.fluxoCaixaDetalhes)
        val fluxoCaixaCadastrar = root.findViewById<Button>(R.id.fluxoCaixaCadastrar)
        val balancoPatrimonialDetalhes = root.findViewById<Button>(R.id.balancoPatrimonialDetalhes)
        val balancoPatrimonialInventario =
            root.findViewById<Button>(R.id.balancoPatrimonialInventario)

        indicadoresFinanceirosAtividade.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.toResultadosAtividadeFragment
            )
        )
        indicadoresFinanceirosDetalhes.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toResultadosFazendaFragment))
        fluxoCaixaAtividade.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toFluxoCaixa))
        fluxoCaixaDetalhes.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toFluxoCaixa))
        fluxoCaixaCadastrar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCadastroFluxoCaixaFragment))
        balancoPatrimonialDetalhes.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toBalancoPatrimonial))
        balancoPatrimonialInventario.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCadastroInventarioFragment))


    }
}
