package com.example.agrogestao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrogestao.models.BalancoPatrimonial
import com.example.agrogestao.models.Farm
import com.example.agrogestao.models.RegistradorFarm
import io.realm.Realm
import io.realm.kotlin.where

class ApresentacaoFazendaViewModel(application: Application) : AndroidViewModel(application) {

    private val mFarm = MutableLiveData<Farm>()
    val myFarm: LiveData<Farm> = mFarm
    private val mBalancoPatrimonial = MutableLiveData<BalancoPatrimonial>()
    val myBalancoPatrimonial: LiveData<BalancoPatrimonial> = mBalancoPatrimonial


    fun load(id: String) {
        val realm = Realm.getDefaultInstance()
        mFarm.value = realm.where<Farm>().contains("id", id).findFirst()
        mBalancoPatrimonial.value =
            realm.where<BalancoPatrimonial>().contains("farm", id).findFirst()
    }

    fun recuperacaoDrawer() {
        val realm = Realm.getDefaultInstance()
        val farm = realm.where<RegistradorFarm>().findFirst()!!
        load(farm.id)
    }

    fun verificarRegistro(id: String) {
        val realm = Realm.getDefaultInstance()
        val farm = realm.where<RegistradorFarm>().findFirst()
        if (farm == null) {
            realm.beginTransaction()
            var farm = RegistradorFarm()
            farm.id = id
            realm.copyToRealm(farm)
            realm.commitTransaction()
        } else {
            realm.beginTransaction()
            farm.id = id
            realm.commitTransaction()
        }
        load(id)

    }
}