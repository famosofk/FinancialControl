package com.example.agrogestao.models

data class Farm(var codigoFazenda: String) {
    val municipio: String? = null
    var atividades: String? = null
    var area: Float = 0f
    var margemLiquida: Float = 0f
    var margemBruta: Float = 0f
    var receitaBruta: Float = 0f
    var custoOperacionalEfetivo: Float = 0f
    var custoOperacionalTotal: Float = 0f
    var receitas: Float = 0f
    var despesas: Float = 0f
    var ativo: Float = 0f
    var passivo: Float = 0f
    var patrimonioLiquido: Float = 0f
    var solvencia: Float = 0f
    var liquidez: Float = 0f
    var rentabilidade: Float = 0f

    fun calcularPatrimonioLiquido() {
        patrimonioLiquido = ativo - passivo
    }

}