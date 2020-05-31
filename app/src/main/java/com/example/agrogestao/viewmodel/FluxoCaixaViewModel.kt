package com.example.agrogestao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrogestao.models.BalancoPatrimonial

class FluxoCaixaViewModel(application: Application) : AndroidViewModel(application) {

    private val mBalancoPatrimonial = MutableLiveData<BalancoPatrimonial>()
    val myBalancoPatrimonial: LiveData<BalancoPatrimonial> = mBalancoPatrimonial


    fun load() {

    }


}

