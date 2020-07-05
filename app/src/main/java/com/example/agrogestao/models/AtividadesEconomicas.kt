package com.example.agrogestao.models

import io.realm.RealmObject

open class AtividadesEconomicas(var nome: String = "") : RealmObject() {

    var fazendaID: String = ""
    var rateio: Float = 1f
    var custoDeProducao: Float = 0f
    var vendasAtividade: Float = 0f
    var lucroAtividade: String = ""
        get() = "R$: ${vendasAtividade - custoDeProducao}"


}