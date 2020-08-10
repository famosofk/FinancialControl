package com.example.agrogestao.models.firebaseclasses

import com.example.agrogestao.models.realmclasses.Farm

data class FarmFirebase(
    var codigoFazenda: String = "",
    var programa: String = "",
    var senha: String = "",
    var id: String = "",
    var area: Double = 0.0,
    var metaMargemLiquida: Double = 0.0,
    var metaMargemBruta: Double = 0.0,
    var metaRendaBruta: Double = 0.0,
    var metaPatrimonioLiquido: Double = 0.0,
    var metaLucro: Double = 0.0,
    var metasaldo: Double = 0.0,
    var metaLiquidezGeral: Double = 0.0,
    var metaLiquidezCorrente: Double = 0.0,
    var observacao: String = "",
    var modificacao: String = ""

) {
    constructor(farm: Farm) : this() {
        codigoFazenda = farm.codigoFazenda
        programa = farm.programa
        senha = farm.senha
        id = farm.id
        area = farm.area
        metaMargemBruta = farm.metaMargemBruta
        metaMargemLiquida = farm.metaMargemLiquida
        metaRendaBruta = farm.metaRendaBruta
        metaPatrimonioLiquido = farm.metaPatrimonioLiquido
        metaLucro = farm.metaLucro
        metasaldo = farm.metasaldo
        metaLiquidezGeral = farm.metaLiquidezGeral
        metaLiquidezCorrente = farm.metaLiquidezCorrente
        observacao = farm.observacao
        modificacao = farm.modificacao
    }
}