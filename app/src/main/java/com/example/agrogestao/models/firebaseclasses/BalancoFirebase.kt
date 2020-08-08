package com.example.agrogestao.models.firebaseclasses

import com.example.agrogestao.models.ItemBalancoPatrimonial

data class BalancoFirebase(
    var farmID: String = "",
    var liquidezGeral: Double = 0.0,
    var liquidezCorrente: Double = 0.0,
    var listaItens: List<ItemBalancoPatrimonial> = ArrayList(),
    var margemLiquida: Double = 0.0,
    var margemBruta: Double = 0.0,
    var taxaRemuneracaoCapital: Double = 0.06,
    var receitaBruta: Double = 0.0,
    var custoOperacionalEfetivo: Double = 0.0,
    var custoOperacionalTotal: Double = 0.0,
    var totalDespesas: Double = 0.0,
    var totalReceitas: Double = 0.0,
    var ativo: Double = 0.0,
    var passivo: Double = 0.0,
    var patrimonioLiquido: Double = 0.0,
    var rentabilidade: Double = 0.0,
    var lucro: Double = 0.0,
    var saldo: Double = 0.0,
    var dividasLongoPrazo: Double = 0.0,
    var dinheiroBanco: Double = 0.0,
    var custoOportunidadeTrabalho: Double = 0.0,
    var trabalhoFamiliarNaoRemunerado: Double = 0.0,
    var pendenciasPagamento: Double = 0.0,
    var pendenciasRecebimento: Double = 0.0,
    var totalContasPagar: Double = 0.0,
    var totalContasReceber: Double = 0.0


)