package com.example.agrogestao.models.realmclasses

import android.annotation.SuppressLint
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.RealmObject
import java.text.SimpleDateFormat
import java.util.*

open class Farm(
    var codigoFazenda: String = "",
    var programa: String = "",
    var senha: String = "",
    var id: String = ""
) : RealmObject() {

    var area = 0.0
    var metaMargemLiquida = 0.0
    var metaMargemBruta = 0.0
    var metaRendaBruta = 0.0
    var metaPatrimonioLiquido = 0.0
    var metaLucro = 0.0
    var metasaldo = 0.0
    var metaLiquidezGeral = 0.0
    var metaLiquidezCorrente = 0.0
    var observacao: String = ""
    var modificacao: String = ""

    @SuppressLint("SimpleDateFormat")
    fun attModificacao() {
        val todayDate: Date = Calendar.getInstance().getTime()
        val formatter = SimpleDateFormat("dd/MMM/yyyy HH:mm:ss")
        val todayString: String = formatter.format(todayDate)
        modificacao = todayString
    }

    fun saveToDb() {
        attModificacao()
        val database = Firebase.database
        val db = database.getReference().child("fazendas").child(programa).child(id)
        db.setValue(this)
    }
}
