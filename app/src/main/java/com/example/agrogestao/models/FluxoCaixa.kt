package com.example.agrogestao.models

import io.realm.RealmList
import io.realm.RealmObject

open class FluxoCaixa() : RealmObject() {

    var farm = ""
    var list = RealmList<ItemFluxoCaixa>()

    fun calcularEntradas(): Float {
        var total: Float = 0f;
        for (item in list) {
            if (!item.tipo) {
                total += item.valor
            }
        }
        return total;
    }

    fun calcularSaidas(): Float {
        var total: Float = 0f;
        for (item in list) {
            if (item.tipo) {
                total += item.valor
            }
        }
        return total;
    }


}