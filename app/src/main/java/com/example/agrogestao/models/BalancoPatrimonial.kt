package com.example.agrogestao.models

import io.realm.RealmList
import io.realm.RealmObject

open class BalancoPatrimonial : RealmObject() {


    var farm = ""
    var liquidezGeral: Float = 0f
    var liquidezCorrente: Float = 0f
    var listaItens = RealmList<ItemBalancoPatrimonial>()
    var margemLiquida: Float = 0f
    var margemBruta: Float = 0f
    var taxaRemuneracaoCapital: Float = 0f //em porcentagem. Lido ao cadastrar fazenda.
    var receitaBruta: Float = 0f
    var custoOperacionalEfetivo: Float = 0f
    var custoOperacionalTotal: Float = 0f
    var totalDespesas: Float = 0f
    var totalReceitas: Float =
        0f  // Não será inserido. É calculado a partir de receitas e despesas cadastrados.
    var ativo: Float = 0f
    var passivo: Float = 0f
    var patrimonioLiquido: Float = 0f
    var rentabilidade: Float = 0f
    var lucro: Float = 0f
    var saldo: Float = 0f
    var dividasLongoPrazo: Float = 0f
    var dinheiroBanco: Float = 0f
    var custoOportunidadeTrabalho: Float = 0f
    var trabalhoFamiliarNaoRemunerado: Float = 0f
    var pendenciasPagamento: Float =
        0f //Será atualizado com o valor das contas não pagas do ano anterior.
    var pendenciasRecebimento: Float =
        0f //Será atualizado com o valor das contas não pagas do ano anterior.
    var totalContasPagar: Float = 0f
    var totalContasReceber: Float = 0f


    fun calcularPassivo() {
        passivo = dividasLongoPrazo + totalContasPagar
    }

    fun calcularAtivo() {
        ativo = totalContasReceber + saldo + calcularValorAnimaisInsumosProdutos()
    }

    fun calcularLiquidezGeral() {
        liquidezGeral = (ativo / passivo)
    }

    fun calcularSaldo() {
        saldo = totalReceitas - totalDespesas
    }

    fun calcularLiquidezCorrente() {

        liquidezCorrente = totalContasPagar / (saldo + calcularPatrimonioBens() + dinheiroBanco)

    }

    fun calcularValorAnimaisInsumosProdutos(): Float {
        var total: Float = 0.0f
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_ANIMAIS) || item.tipo.equals(
                    ItemBalancoPatrimonial.ITEM_INSUMOS
                ) || item.tipo.equals(
                    ItemBalancoPatrimonial.ITEM_PRODUTOS
                )
            ) {
                total += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return total
    }

    fun calcularPatrimonioBens(): Float {

        var total = 0.0f
        for (item in listaItens) {
            total += (item.quantidadeFinal * item.valorUnitario + item.reforma)
        }
        return total
    }


    fun calcularPatrimonioLiquido() {
        patrimonioLiquido = (ativo - passivo)
    }

    fun calcularLucro() {
        lucro = receitaBruta - calcularCustoTotal()
    }

    fun calcularMargemLiquida() {
        margemLiquida = receitaBruta - custoOperacionalTotal
    }

    fun calcularMargemBruta() {
        margemBruta = receitaBruta - custoOperacionalEfetivo
    }

    fun calcularReceitaBruta() {
        receitaBruta = totalReceitas + calcularValorProdutos()
    }

    fun calcularValorProdutos(): Float {
        var valorProdutos: Float = 0.0f
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_PRODUTOS) && item.anoProducao.equals(
                    2020
                )
            ) {
                valorProdutos += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return valorProdutos
    }

    fun calcularCustoTotal(): Float {
        return custoOperacionalTotal + calcularOportunidadeCapital() + custoOportunidadeTrabalho
    }

    fun calcularOportunidadeCapital(): Float {
        return patrimonioLiquido * taxaRemuneracaoCapital
    }

    fun calcularCustoOportunidadeCapital(): Float {
        return patrimonioLiquido * taxaRemuneracaoCapital
    }

    fun calcularCustoOperacionalTotal() {

        var depreciacao = 0f
        for (item in listaItens) {
            depreciacao += item.depreciacao
        }

        custoOperacionalTotal =
            custoOperacionalEfetivo + depreciacao + trabalhoFamiliarNaoRemunerado
    }


    fun calcularCustoOperacionalEfetivo() {
        custoOperacionalEfetivo = totalDespesas + totalContasPagar - pendenciasPagamento
    }


    fun calcularRentabilidade() {
        rentabilidade = receitaBruta - patrimonioLiquido
    }


    fun calcularValorAnimais(): Float {
        var valorAnimais = 0.0f
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_ANIMAIS)) {
                valorAnimais += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return valorAnimais
    }


    fun calcularValorInsumos(): Float {
        var valorInsumos = 0.0f
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_INSUMOS)) {
                valorInsumos += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return valorInsumos
    }

}