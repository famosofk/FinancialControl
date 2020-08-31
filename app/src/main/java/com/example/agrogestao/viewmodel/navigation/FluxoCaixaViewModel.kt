package com.example.agrogestao.viewmodel.navigation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agrogestao.models.ItemFluxoCaixa
import com.example.agrogestao.models.realmclasses.AtividadesEconomicas
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import com.example.agrogestao.models.realmclasses.FluxoCaixa
import io.realm.Realm
import io.realm.kotlin.where

class FluxoCaixaViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var farmKey: String
    private val mBalancoPatrimonial = MutableLiveData<BalancoPatrimonial>()
    val myBalancoPatrimonial: LiveData<BalancoPatrimonial> = mBalancoPatrimonial
    private val mFluxoCaixa = MutableLiveData<FluxoCaixa>()
    val myFluxoCaixa: LiveData<FluxoCaixa> = mFluxoCaixa
    private val mAtividades = MutableLiveData<List<AtividadesEconomicas>>()
    val myAtividades: LiveData<List<AtividadesEconomicas>> = mAtividades
    val realm = Realm.getDefaultInstance()

    val listaVista = MutableLiveData<List<ItemFluxoCaixa>>()
    val listaPrazo = MutableLiveData<List<ItemFluxoCaixa>>()

    fun load(key: String) {
        farmKey = key
        mAtividades.value = realm.where<AtividadesEconomicas>().contains("fazendaID", key).findAll()
        mFluxoCaixa.value = realm.where<FluxoCaixa>().contains("farmID", key).findFirst()
        mBalancoPatrimonial.value =
            realm.where<BalancoPatrimonial>().contains("farmID", key).findFirst()
    }


    fun loadLists(atividade: String) {

        val listaPrazoTemporaria = arrayListOf<ItemFluxoCaixa>()
        val listaVistaTemporaria = arrayListOf<ItemFluxoCaixa>()

        for (item in mFluxoCaixa.value?.list!!) {
            if (item.pagamentoPrazo && item.atividade.equals(atividade)) {
                listaPrazoTemporaria.add(item)
            } else if (!item.pagamentoPrazo && atividade.equals(atividade)) {
                listaVistaTemporaria.add(item)
            }
        }

        listaVista.value = listaVistaTemporaria
        listaPrazo.value = listaPrazoTemporaria


    }

}

