package com.example.agrogestao.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrogestao.models.Farm
import io.realm.Realm
import io.realm.kotlin.where

class AtividadesViewModel(application: Application) : AndroidViewModel(application) {

    private val mFarms = MutableLiveData<List<Farm>>()
    val farmList: LiveData<List<Farm>> = mFarms

    fun load(filter: Int) {
        val list = mutableListOf<Farm>()
        val realm: Realm = Realm.getDefaultInstance()
        val query = realm.where<Farm>().findAll()
        list.addAll(query)
        mFarms.value = list


    }


}