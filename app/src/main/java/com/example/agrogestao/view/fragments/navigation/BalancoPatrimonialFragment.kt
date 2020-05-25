package com.example.agrogestao.view.fragments.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.agrogestao.R
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
}