package com.example.agrogestao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrogestao.models.BalancoPatrimonial
import com.example.agrogestao.models.Farm
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
        // mBalancoPatrimonial.value =


    }
}