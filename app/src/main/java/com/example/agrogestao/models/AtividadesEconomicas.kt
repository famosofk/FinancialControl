package com.example.agrogestao.models

import io.realm.RealmObject

open class AtividadesEconomicas(var nome: String = "") : RealmObject() {

    var rateio: Float = 0f
    var presente: Boolean = false
    var custoDeProducao: Float = 0f

}