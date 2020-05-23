package com.example.agrogestao.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.agrogestao.R

class ApresentacaoFazendaFragment : Fragment() {

    private lateinit var apresentacaoFazendaViewModel: ApresentacaoFazendaViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        apresentacaoFazendaViewModel =
            ViewModelProviders.of(this).get(ApresentacaoFazendaViewModel::class.java)
        val root = inflater.inflate(R.layout.apresentacao_fazenda, container, false)

        return root
    }
}
