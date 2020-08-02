package com.example.agrogestao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrogestao.models.realmclasses.AtividadesEconomicas
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import io.realm.Realm
import io.realm.kotlin.where

class ResultadosFazendaViewModel(application: Application) : AndroidViewModel(application) {

    private val mbalancoPatrimonial = MutableLiveData<BalancoPatrimonial>()
    val myBalancoPatrimonial: LiveData<BalancoPatrimonial> = mbalancoPatrimonial

    var inicio = 0

    private val _cadastrarAtividade = MutableLiveData(false)
    val cadastrarAtividade: LiveData<Boolean>
        get() = _cadastrarAtividade

    var list = mutableListOf<AtividadesEconomicas>()

    private val _carregarAtividade = MutableLiveData(false)
    val carregarAtividade: LiveData<Boolean>
        get() = _carregarAtividade

    fun load(idFarm: String) {
        val realm = Realm.getDefaultInstance()
        val results = realm.where<AtividadesEconomicas>().contains("fazendaID", idFarm).findAll()
        list.addAll(results)
        mbalancoPatrimonial.value =
            realm.where<BalancoPatrimonial>().contains("farm", idFarm).findFirst()
    }

    fun openDialog() {
        if (inicio == 1) {
            _cadastrarAtividade.value = !(_cadastrarAtividade.value!!)
        }
    }

}