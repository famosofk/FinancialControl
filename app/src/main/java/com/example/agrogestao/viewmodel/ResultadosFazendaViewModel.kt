package com.example.agrogestao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrogestao.models.BalancoPatrimonial

class ResultadosFazendaViewModel(application: Application) : AndroidViewModel(application) {

    private val mbalancoPatrimonial = MutableLiveData<BalancoPatrimonial>()
    val myBalancoPatrimonial: LiveData<BalancoPatrimonial> = mbalancoPatrimonial

    var inicio = 0

    private val _cadastrarAtividade = MutableLiveData(false)
    val cadastrarAtividade: LiveData<Boolean>
        get() = _cadastrarAtividade

    fun load() {}

    fun openDialog() {
        if (inicio == 1) {
            _cadastrarAtividade.value = !(_cadastrarAtividade.value!!)
        }
    }

}