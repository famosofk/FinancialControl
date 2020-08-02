package com.example.agrogestao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import io.realm.Realm
import io.realm.kotlin.where

class BalancoPatrimonialViewModel(application: Application) : AndroidViewModel(application) {

    private val mBalancoPatrimonial = MutableLiveData<BalancoPatrimonial>()
    val myBalancoPatrimonial: LiveData<BalancoPatrimonial> = mBalancoPatrimonial

    fun load(key: String) {
        val realm = Realm.getDefaultInstance()
        mBalancoPatrimonial.value =
            realm.where<BalancoPatrimonial>().contains("farmID", key).findFirst()
    }


}