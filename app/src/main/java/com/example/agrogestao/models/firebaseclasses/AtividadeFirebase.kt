package com.example.agrogestao.models.firebaseclasses

import com.example.agrogestao.models.realmclasses.AtividadesEconomicas

data class AtividadeFirebase(
    var nome: String = "",
    var fazendaID: String = "",
    var rateio: Double = 1.0,
    var custoDeProducao: Double = 0.0,
    var vendasAtividade: Double = 0.0,
    var lucroAtividade: String = "",

    var arrayCustos: List<Double> = listOf(
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0
    ),

    var custoSemente: Double = 0.0,
    var custoFertilizante: Double = 0.0,
    var custoDefensivo: Double = 0.0,
    var custoMaodeobra: Double = 0.0,
    var custoMaquina: Double = 0.0,
    var custoOutros: Double = 0.0,
    var modificacao: String = ""
) {
    constructor(atividade: AtividadesEconomicas) : this() {

        nome = atividade.nome
        fazendaID = atividade.fazendaID
        rateio = atividade.rateio
        custoDeProducao = atividade.custoDeProducao
        vendasAtividade = atividade.vendasAtividade
        lucroAtividade = atividade.lucroAtividade

        arrayCustos = atividade.arrayCustos
        custoSemente = atividade.custoSemente
        custoFertilizante = atividade.custoFertilizante
        custoDefensivo = atividade.custoDefensivo
        custoMaodeobra = atividade.custoMaodeobra
        custoMaquina = atividade.custoMaquina
        custoOutros = atividade.custoOutros
        modificacao = atividade.modificacao

    }
}