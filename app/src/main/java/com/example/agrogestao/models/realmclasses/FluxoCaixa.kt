package com.example.agrogestao.models.realmclasses

import android.annotation.SuppressLint
import com.example.agrogestao.models.ItemFluxoCaixa
import com.example.agrogestao.models.firebaseclasses.FluxoCaixaFirebase
import com.google.firebase.database.Exclude
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

open class FluxoCaixa() : RealmObject() {

    var farmID = ""
    var list = RealmList<ItemFluxoCaixa>()
    var modificacao: String = ""

    @Exclude
    var atualizado: Boolean = false


    constructor(firebase: FluxoCaixaFirebase) : this() {
        farmID = firebase.farmID
        list.addAll(firebase.list)
        modificacao = firebase.modificacao
    }

    fun saveToDb() {
        attModificacao()
        val db = Firebase.database.reference.child("fluxoCaixa").child(this.farmID)
        db.setValue(this)
    }


    @SuppressLint("SimpleDateFormat")
    fun attModificacao() {
        val todayDate: Date = Calendar.getInstance().getTime()
        modificacao = todayDate.time.toString()
    }



}