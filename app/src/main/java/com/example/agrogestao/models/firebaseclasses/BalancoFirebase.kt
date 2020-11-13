package com.example.agrogestao.models.firebaseclasses

import com.example.agrogestao.models.ItemBalancoPatrimonial
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import java.util.*

data class BalancoFirebase(
    var farmID: String = "",
    var liquidezGeral: String = "0.00",
    var liquidezCorrente: String = "0.00",
    var margemLiquida: String = "0.00",
    var listaItens: ArrayList<ItemBalancoPatrimonial> = ArrayList(),
    var margemBruta: String = "0.00",
    var taxaRemuneracaoCapital: String = "0.06",
    var receitaBruta: String = "0.00",
    var custoOperacionalEfetivo: String = "0.00",
    var custoOperacionalTotal: String = "0.00",
    var totalDespesas: String = "0.00",
    var totalReceitas: String = "0.00",
    var ativo: String = "0.00",
    var passivo: String = "0.00",
    var patrimonioLiquido: String = "0.00",
    var rentabilidade: String = "0.00",
    var lucro: String = "0.00",
    var dividasLongoPrazo: String = "0.00",
    var dinheiroBanco: String = "0.00",
    var custoOportunidadeTrabalho: String = "0.00",
    var trabalhoFamiliarNaoRemunerado: String = "0.00",
    var pendenciasPagamento: String = "0.00",
    var pendenciasRecebimento: String = "0.00",
    var totalContasPagar: String = "0.00",
    var totalContasReceber: String = "0.00",
    var modificacao: String = ""
) {
    constructor(balanco: BalancoPatrimonial) : this() {
        farmID = balanco.farmID
        liquidezGeral = balanco.liquidezGeral
        liquidezCorrente = balanco.liquidezCorrente
        margemLiquida = balanco.margemLiquida
        margemBruta = balanco.margemBruta
        taxaRemuneracaoCapital = balanco.taxaRemuneracaoCapital
        receitaBruta = balanco.receitaBruta
        custoOperacionalEfetivo = balanco.custoOperacionalEfetivo
        custoOperacionalTotal = balanco.custoOperacionalTotal
        totalDespesas = balanco.totalDespesas
        totalReceitas = balanco.totalReceitas
        ativo = balanco.ativo
        passivo = balanco.passivo
        patrimonioLiquido = balanco.patrimonioLiquido
        rentabilidade = balanco.rentabilidade
        lucro = balanco.lucro
        dividasLongoPrazo = balanco.dividasLongoPrazo
        dinheiroBanco = balanco.dinheiroBanco
        custoOportunidadeTrabalho = balanco.custoOportunidadeTrabalho
        trabalhoFamiliarNaoRemunerado = balanco.trabalhoFamiliarNaoRemunerado
        pendenciasPagamento = balanco.pendenciasPagamento
        pendenciasRecebimento = balanco.pendenciasRecebimento
        totalContasPagar = balanco.totalContasPagar
        totalContasReceber = balanco.totalContasReceber
        modificacao = balanco.modificacao
    }
}

