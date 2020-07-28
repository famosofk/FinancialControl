package com.example.agrogestao.models

import io.realm.RealmList
import io.realm.RealmObject

open class AtividadesEconomicas(var nome: String = "") : RealmObject() {

    var fazendaID: String = ""
    var rateio: Double = 1.0
    var custoDeProducao: Double = 0.0
    var vendasAtividade: Double = 0.0
    var lucroAtividade: String = ""
        get() = "R$: ${vendasAtividade - custoDeProducao}"

    var arrayCustos = RealmList<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    var custoSemente = 0.0
    var custoFertilizante = 0.0
    var custoDefensivo = 0.0
    var custoMaodeobra = 0.0
    var custoMaquina = 0.0
    var custoOutros = 0.0


}