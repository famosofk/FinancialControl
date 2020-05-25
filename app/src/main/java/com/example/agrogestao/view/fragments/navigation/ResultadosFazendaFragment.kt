package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agrogestao.R

/**
 * A simple [Fragment] subclass.
 */
class ResultadosFazendaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.resultados_fazenda, container, false)

        return root
    }

}
