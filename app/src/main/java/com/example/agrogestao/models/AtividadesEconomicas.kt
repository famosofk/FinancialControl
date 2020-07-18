package com.example.agrogestao.models

import io.realm.RealmList
import io.realm.RealmObject

open class AtividadesEconomicas(var nome: String = "") : RealmObject() {

    var fazendaID: String = ""
    var rateio: Float = 1f
    var custoDeProducao: Float = 0f
    var vendasAtividade: Float = 0f
    var lucroAtividade: String = ""
        get() = "R$: ${vendasAtividade - custoDeProducao}"

    var arrayCustos = RealmList<Float>(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

    var custoSemente = 0f
    var custoFertilizante = 0f
    var custoDefensivo = 0f
    var custoMaodeobra = 0f
    var custoMaquina = 0f
    var custoOutros = 0f



}