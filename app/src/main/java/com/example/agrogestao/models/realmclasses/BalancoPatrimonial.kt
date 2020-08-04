package com.example.agrogestao.models.realmclasses

import android.util.Log
import com.example.agrogestao.models.ItemBalancoPatrimonial
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.RealmList
import io.realm.RealmObject

open class BalancoPatrimonial : RealmObject() {


    var farmID = ""
    var liquidezGeral = 0.0
    var liquidezCorrente = 0.0
    var listaItens = RealmList<ItemBalancoPatrimonial>()
    var margemLiquida = 0.0
    var margemBruta = 0.0
    var taxaRemuneracaoCapital = 0.06
    var receitaBruta = 0.0
    var custoOperacionalEfetivo = 0.0
    var custoOperacionalTotal = 0.0
    var totalDespesas = 0.0
    var totalReceitas = 0.0
    var ativo = 0.0
    var passivo = 0.0
    var patrimonioLiquido = 0.0
    var rentabilidade = 0.0
    var lucro = 0.0
    var saldo = 0.0
    var dividasLongoPrazo = 0.0
    var dinheiroBanco = 0.0
    var custoOportunidadeTrabalho = 0.0
    var trabalhoFamiliarNaoRemunerado = 0.0
    var pendenciasPagamento = 0.0
    var pendenciasRecebimento = 0.0
    var totalContasPagar = 0.0
    var totalContasReceber = 0.0

    fun saveToDb() {
        val database = Firebase.database
        val db = database.getReference().child("balancoPatrimonial").child(this.farmID)
        db.setValue(this)
    }

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
        passivo = dividasLongoPrazo + totalContasPagar + pendenciasPagamento
    }

    fun calcularAtivo() {
        ativo =
            totalContasReceber + calcularPatrimonioBens() + dinheiroBanco + pendenciasRecebimento
    }

    fun calcularLiquidezGeral() {
        liquidezGeral = (ativo / passivo)
    }

    fun calcularSaldo() {
        saldo = dinheiroBanco
    }

    fun calcularLiquidezCorrente() {

        liquidezCorrente = (saldo + calcularValorAnimaisInsumosProdutos()) / totalContasPagar

    }

    fun calcularValorAnimaisInsumosProdutos(): Double {
        var total = 0.0
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

    fun calcularPatrimonioBens(): Double {

        var total = 0.0
        for (item in listaItens) {
            item.calcularDepreciacao()
            if (item.tipo != ItemBalancoPatrimonial.ITEM_DIVIDAS_LONGO_PRAZO) {
                total += (item.quantidadeFinal * item.valorAtual)
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

    fun calcularCustoTotal(): Double {
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

    fun calcularValorProdutos(): Double {
        var valorProdutos = 0.0
        //Atualizar o valor ano a ano
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_PRODUTOS) && item.anoProducao == 2020
            ) {
                valorProdutos += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return valorProdutos
    }


    fun calcularOportunidadeCapital(): Double {
        return ativo * taxaRemuneracaoCapital
    }



    fun calcularCustoOperacionalTotal() {

        var depreciacao = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_BENFEITORIA) || item.tipo.equals(
                    ItemBalancoPatrimonial.ITEM_MAQUINAS
                )
            )
                item.calcularDepreciacao()
            depreciacao += item.depreciacao - item.reforma
        }

        custoOperacionalTotal =
            custoOperacionalEfetivo + depreciacao + trabalhoFamiliarNaoRemunerado
    }


    fun calcularCustoOperacionalEfetivo() {
        custoOperacionalEfetivo = totalDespesas + totalContasPagar


    }


    private fun calcularRentabilidade() {
        calcularMargemBruta()
        rentabilidade = margemBruta / patrimonioLiquido
    }


    fun calcularValorAnimais(): Double {
        var valorAnimais = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_ANIMAIS)) {
                valorAnimais += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return valorAnimais
    }

    fun calcularValorTerras(): Double {
        var valorAnimais = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_TERRA)) {
                valorAnimais += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return valorAnimais
    }

    fun calcularValorMaquinas(): Double {
        var valorAnimais = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_MAQUINAS)) {
                valorAnimais += (item.quantidadeFinal * item.valorAtual + item.reforma)
            }
        }
        return valorAnimais
    }

    fun calcularValorBenfeitorias(): Double {
        var valorAnimais = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_BENFEITORIA)) {
                valorAnimais += (item.quantidadeFinal * item.valorAtual + item.reforma)
            }
        }
        return valorAnimais
    }


    fun calcularValorInsumos(): Double {
        var valorInsumos = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_INSUMOS)) {
                valorInsumos += (item.quantidadeFinal * item.valorUnitario + item.reforma)
            }
        }
        return valorInsumos
    }


    fun calcularDividasLongoPrazo() {
        dividasLongoPrazo = 0.0
        for (item in listaItens) {
            if (item.tipo == ItemBalancoPatrimonial.ITEM_DIVIDAS_LONGO_PRAZO) {
                dividasLongoPrazo += item.quantidadeFinal * item.valorAtual
                Log.e("entrou aqui: ", "true")
            }
        }
    }


}