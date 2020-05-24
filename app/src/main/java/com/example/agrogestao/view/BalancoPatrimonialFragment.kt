package com.example.agrogestao.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
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
            ViewModelProviders.of(this).get(BalancoPatrimonialViewModel::class.java)
        val root = inflater.inflate(R.layout.apresentacao_balanco_patrimonial, container, false)



        return root
    }
}
