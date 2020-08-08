package com.example.agrogestao.models.firebaseclasses

import com.example.agrogestao.models.ItemFluxoCaixa

data class FluxoCaixaFirebase(
    var farmID: String = "",
    var list: List<ItemFluxoCaixa> = listOf<ItemFluxoCaixa>()
)