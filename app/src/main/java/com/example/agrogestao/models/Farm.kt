package com.example.agrogestao.models

data class Farm(var codigoFazenda: String, val programa: String) {

    //Criar um sistema de cadastro de Atividades -> Nome + Rateio

    val senhaAcesso: String = ""
    val municipio: String = ""
    var atividades = arrayListOf<String>()
    var area: Float = 0f
    var metaMargemLiquida: Float = 0f
    var metaMargemBruta: Float = 0f
    var metaReceitaBruta: Float = 0f
    var metaCustoOperacionalEfetivo: Float = 0f
    var metaCustoOperacionalTotal: Float = 0f
    var metaTotalDespesas: Float = 0f
    var metaAtivo: Float = 0f
    var metaPassivo: Float = 0f
    var metaPatrimonioLiquido: Float = 0f
    var totalContasPagar: Float = 0f
    var metaRentabilidade: Float = 0f
    var metaLucro: Float = 0f
    var metasaldo: Float = 0f
    var dividaLongoPrazo: Float = 0f

    var depreciacao: Float = 0f //Não será lido. Calculado através do inventário.

















}