package com.example.agrogestao.models.realmclasses

import com.google.firebase.database.Exclude
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.RealmObject
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

    @Exclude
    var atualizado = false


    fun myEquals(farm: Farm): Boolean {
        if (codigoFazenda == farm.codigoFazenda
            && programa == farm.programa
            && senha == farm.senha
            && id == farm.id
            && area == farm.area
            && metaMargemLiquida == farm.metaMargemLiquida
            && metaMargemBruta == farm.metaMargemBruta
            && metaRendaBruta == farm.metaRendaBruta
            && metaPatrimonioLiquido == farm.metaPatrimonioLiquido
            && metaLucro == farm.metaLucro
            && metasaldo == farm.metasaldo
            && metaLiquidezGeral == farm.metaLiquidezGeral
            && metaLiquidezCorrente == farm.metaLiquidezCorrente
            && observacao == farm.observacao
        ) {
            return true
        }
        return false
    }

    @Exclude
    fun attModificacao() {
        val todayDate: Date = Calendar.getInstance().getTime()
        modificacao = todayDate.time.toString()
    }

    @Exclude
    fun saveToDb() {
        attModificacao()
        val db = Firebase.database.reference.child("fazendas").child(programa).child(id)
        db.setValue(this)
    }
}
