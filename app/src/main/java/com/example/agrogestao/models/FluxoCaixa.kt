package com.example.agrogestao.models

import io.realm.RealmList
import io.realm.RealmObject

open class FluxoCaixa : RealmObject() {

    var farm = ""
    var list = RealmList<ItemFluxoCaixa>()

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