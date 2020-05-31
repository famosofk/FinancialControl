package com.example.agrogestao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrogestao.models.BalancoPatrimonial
import com.example.agrogestao.models.Farm

class AtualizarFazendaViewModel(application: Application) : AndroidViewModel(application) {

    private val mFarm = MutableLiveData<Farm>()
    val myFarm: LiveData<Farm> = mFarm

    private val mBalancoPatrimonial = MutableLiveData<BalancoPatrimonial>()
    val myBalanco = mBalancoPatrimonial


    fun load() {
        //Recuperar fazenda, balanço e fluxo de caixa

    }


}