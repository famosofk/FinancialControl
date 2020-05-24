package com.example.agrogestao.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agrogestao.R

/**
 * A simple [Fragment] subclass.
 */
class CriarFazendProgramaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.cadastro_programa_fazenda, container, false)
        return root

    }

}
