package com.example.agrogestao.models.realmclasses

import com.example.agrogestao.models.ItemFluxoCaixa
import com.example.agrogestao.models.firebaseclasses.FluxoCaixaFirebase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.RealmList
import io.realm.RealmObject

open class FluxoCaixa() : RealmObject() {

    var farmID = ""
    var list = RealmList<ItemFluxoCaixa>()

    constructor(firebase: FluxoCaixaFirebase) : this() {
        farmID = firebase.farmID
        list.addAll(firebase.list)
    }

    fun saveToDb() {
        val database = Firebase.database
        val db = database.getReference().child("fluxoCaixa").child(this.farmID)
        db.setValue(this)
    }

    fun calcularEntradas(): Double {
        var total = 0.0
        for (item in list) {
            if (!item.tipo) {
                total += item.valorInicial - item.valorAmortizado
            }
        }
        return total;
    }

    fun calcularSaidas(): Double {
        var total = 0.0;
        for (item in list) {
            if (item.tipo) {
                total += item.valorInicial - item.valorAmortizado
            }
        }
        return total;
    }

    fun calcularSaldo(): Double {
        var total = 0.0;
        for (item in list) {
            if (item.tipo) {
                total -= item.valorInicial
            } else {
                total += item.valorInicial
            }
        }
        return total;
    }


}