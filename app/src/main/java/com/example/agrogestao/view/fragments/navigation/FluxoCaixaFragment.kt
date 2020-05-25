package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.agrogestao.R
import com.example.agrogestao.viewmodel.FluxoCaixaViewModel

class FluxoCaixaFragment : Fragment() {

    private lateinit var fluxoCaixaViewModel: FluxoCaixaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fluxoCaixaViewModel =
            ViewModelProviders.of(this).get(FluxoCaixaViewModel::class.java)
        val root = inflater.inflate(R.layout.fluxo_caixa_atividade, container, false)

        //fazer a distinção se é por atividade ou por fazenda

        return root
    }
}
