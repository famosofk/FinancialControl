package com.example.agrogestao.models.firebaseclasses

import com.example.agrogestao.models.ItemFluxoCaixa
import com.example.agrogestao.models.realmclasses.FluxoCaixa

data class FluxoCaixaFirebase(
    var farmID: String = "",
    var list: List<ItemFluxoCaixa> = listOf(),
    var modificacao: String = ""
) {
    constructor(fluxo: FluxoCaixa) : this() {
        farmID = fluxo.farmID
        modificacao = fluxo.modificacao


    }
}