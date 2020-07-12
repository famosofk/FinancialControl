package com.example.agrogestao.models

import android.util.Log
import io.realm.RealmList
import io.realm.RealmObject

open class BalancoPatrimonial : RealmObject() {


    var farm = ""
    var liquidezGeral: Float = 0f
    var liquidezCorrente: Float = 0f
    var listaItens = RealmList<ItemBalancoPatrimonial>()
    var margemLiquida: Float = 0f
    var margemBruta: Float = 0f
    var taxaRemuneracaoCapital: Float = 0.06f
    var receitaBruta: Float = 0f
    var custoOperacionalEfetivo: Float = 0f
    var custoOperacionalTotal: Float = 0f
    var totalDespesas: Float = 0f
    var totalReceitas: Float = 0f
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
    var pendenciasPagamento: Float = 0f
    var pendenciasRecebimento: Float = 0f
    var totalContasPagar: Float = 0f
    var totalContasReceber: Float = 0f

    fun atualizarBalanco() {
        calcularAtivo()
        calcularPassivo()
        calcularPatrimonioLiquido()
        calcularSaldo()
        calcularLiquidezGeral()
        calcularLiquidezCorrente()
        calcularCustoOperacionalEfetivo()
        calcularCustoOperacionalTotal()

        calcularReceitaBruta()
        calcularMargemLiquida()
        calcularMargemBruta()
        calcularLucro()
        calcularRentabilidade()

    }


    fun calcularPassivo() {
        calcularDividasLongoPrazo()
        passivo = dividasLongoPrazo + totalContasPagar
    }

    fun calcularAtivo() {
        ativo = totalContasReceber + calcularPatrimonioBens() + dinheiroBanco
    }

    fun calcularLiquidezGeral() {
        liquidezGeral = (ativo / passivo)
    }

    fun calcularSaldo() {
        //saldo = totalReceitas - (totalDespesas - totalContasPagar) + dinheiroBanco
        saldo = dinheiroBanco
    }

    fun calcularLiquidezCorrente() {

        liquidezCorrente = (saldo + calcularValorAnimaisInsumosProdutos()) / totalContasPagar

    }

    fun calcularValorAnimaisInsumosProdutos(): Float {
        var total = 0.0f
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
            if (item.tipo != ItemBalancoPatrimonial.ITEM_DIVIDAS_LONGO_PRAZO) {
                total += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return total
    }


    fun calcularPatrimonioLiquido() {
        patrimonioLiquido = (ativo - passivo)
    }

    fun calcularLucro() {
        lucro = receitaBruta - calcularCustoTotal()
    }

    fun calcularCustoTotal(): Float {
        return custoOperacionalTotal + calcularOportunidadeCapital() + custoOportunidadeTrabalho
    }

    fun calcularMargemLiquida() {

        margemLiquida = receitaBruta - custoOperacionalTotal
    }

    fun calcularMargemBruta() {
        margemBruta = receitaBruta - custoOperacionalEfetivo
    }

    fun calcularReceitaBruta() {
        receitaBruta = totalReceitas + calcularValorProdutos() + totalContasReceber
    }

    fun calcularValorProdutos(): Float {
        var valorProdutos = 0.0f
        //Atualizar o valor ano a ano
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_PRODUTOS) && item.anoProducao == 2020
            ) {
                valorProdutos += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return valorProdutos
    }


    fun calcularOportunidadeCapital(): Float {
        return patrimonioLiquido * taxaRemuneracaoCapital
    }



    fun calcularCustoOperacionalTotal() {

        var depreciacao = 0f
        for (item in listaItens) {
            //benfeitoria e maquina
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_BENFEITORIA) || item.tipo.equals(
                    ItemBalancoPatrimonial.ITEM_MAQUINAS
                )
            )
                item.calcularDepreciacao()
            depreciacao += item.depreciacao
        }

        custoOperacionalTotal =
            custoOperacionalEfetivo + depreciacao + trabalhoFamiliarNaoRemunerado
    }


    fun calcularCustoOperacionalEfetivo() {
        custoOperacionalEfetivo = totalDespesas + totalContasPagar
        /*
        * Adicionar os custos de produção
        * */

    }


    private fun calcularRentabilidade() {
        calcularMargemBruta()
        rentabilidade = margemBruta / patrimonioLiquido
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

    //nada chama esse método
    fun calcularCustoOportunidadeCapital(): Float {
        return patrimonioLiquido * taxaRemuneracaoCapital
    }

    fun calcularDividasLongoPrazo() {
        dividasLongoPrazo = 0f
        for (item in listaItens) {
            if (item.tipo == ItemBalancoPatrimonial.ITEM_DIVIDAS_LONGO_PRAZO) {
                dividasLongoPrazo += item.quantidadeFinal * item.valorAtual
                Log.e("entrou aqui: ", "true")
            }
        }
    }


}