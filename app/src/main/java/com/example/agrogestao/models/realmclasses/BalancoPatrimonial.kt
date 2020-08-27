package com.example.agrogestao.models.realmclasses

import android.annotation.SuppressLint
import android.util.Log
import com.example.agrogestao.models.ItemBalancoPatrimonial
import com.example.agrogestao.models.firebaseclasses.BalancoFirebase
import com.google.firebase.database.Exclude
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.RealmList
import io.realm.RealmObject
import java.math.BigDecimal
import java.util.*

open class BalancoPatrimonial() : RealmObject() {

    constructor(firebase: BalancoFirebase) : this() {
        farmID = firebase.farmID
        liquidezGeral = firebase.liquidezGeral
        liquidezCorrente = firebase.liquidezCorrente
        listaItens.addAll(firebase.listaItens)
        margemBruta = firebase.margemBruta
        margemLiquida = firebase.margemLiquida
        taxaRemuneracaoCapital = firebase.taxaRemuneracaoCapital
        receitaBruta = firebase.receitaBruta
        custoOperacionalEfetivo = firebase.custoOperacionalEfetivo
        custoOperacionalTotal = firebase.custoOperacionalTotal
        totalDespesas = firebase.totalDespesas
        totalReceitas = firebase.totalReceitas
        ativo = firebase.ativo
        passivo = firebase.passivo
        patrimonioLiquido = firebase.patrimonioLiquido
        rentabilidade = firebase.rentabilidade
        lucro = firebase.lucro
        saldo = firebase.saldo
        dividasLongoPrazo = firebase.dividasLongoPrazo
        dinheiroBanco = firebase.dinheiroBanco
        custoOportunidadeTrabalho = firebase.custoOportunidadeTrabalho
        trabalhoFamiliarNaoRemunerado = firebase.trabalhoFamiliarNaoRemunerado
        pendenciasRecebimento = firebase.pendenciasRecebimento
        pendenciasPagamento = firebase.pendenciasPagamento
        totalContasPagar = firebase.totalContasPagar
        totalContasReceber = firebase.totalContasReceber
        modificacao = firebase.modificacao
    }

    var farmID = ""
    var liquidezGeral = "0.00"
    var liquidezCorrente = "0.00"
    var listaItens = RealmList<ItemBalancoPatrimonial>()
    var margemLiquida = "0.00"
    var margemBruta = "0.00"
    var taxaRemuneracaoCapital = "0.06"
    var receitaBruta = "0.00"
    var custoOperacionalEfetivo = "0.00"
    var custoOperacionalTotal = "0.00"
    var totalDespesas = "0.00"
    var totalReceitas = "0.00"
    var ativo = "0.00"
    var passivo = "0.00"
    var patrimonioLiquido = "0.00"
    var rentabilidade = "0.00"
    var lucro = "0.00"
    var saldo = "0.00"
    var dividasLongoPrazo = "0.00"
    var dinheiroBanco = "0.00"
    var custoOportunidadeTrabalho = "0.00"
    var trabalhoFamiliarNaoRemunerado = "0.00"
    var pendenciasPagamento = "0.00"
    var pendenciasRecebimento = "0.00"
    var totalContasPagar = "0.00"
    var totalContasReceber = "0.00"
    var modificacao: String = ""

    @Exclude
    var atualizado = false


    fun saveToDb() {
        attModificacao()
        val db = Firebase.database.reference.child("balancoPatrimonial").child(this.farmID)
        db.setValue(this)
    }

    fun atualizarBalanco() {
        attModificacao()
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

    @SuppressLint("SimpleDateFormat")
    fun attModificacao() {
        val todayDate: Date = Calendar.getInstance().getTime()
        modificacao = todayDate.time.toString()
    }


    private fun calcularPassivo() {
        calcularDividasLongoPrazo()
        passivo =
            (dividasLongoPrazo.toBigDecimal() + totalContasPagar.toBigDecimal() + pendenciasPagamento.toBigDecimal()).toString()
    }

    private fun calcularAtivo() {

        ativo =
            (calcularPatrimonioBens().toBigDecimal() +
                    pendenciasRecebimento.toBigDecimal() +
                    dinheiroBanco.toBigDecimal() +
                    totalContasReceber.toBigDecimal()).toString()
    }

    private fun calcularLiquidezGeral() {
        if (passivo.toBigDecimal() > 1.toBigDecimal()) {
            liquidezGeral = (ativo.toBigDecimal() / passivo.toBigDecimal()).toString()
        } else {
            liquidezGeral = "0.00"
        }
    }

    private fun calcularSaldo() {
        saldo = dinheiroBanco
    }

    fun calcularLiquidezCorrente() {
        if (totalContasPagar.toBigDecimal() > 1.toBigDecimal()) {
            liquidezCorrente =
                ((saldo.toBigDecimal() + calcularValorAnimaisInsumosProdutos().toBigDecimal()) / totalContasPagar.toBigDecimal()).toString()
        } else {
            liquidezCorrente = (saldo.toBigDecimal() + calcularValorAnimaisInsumosProdutos().toBigDecimal()).toString()
        }

    }

    fun calcularValorAnimaisInsumosProdutos(): String {
        var total = BigDecimal.ZERO
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_ANIMAIS) || item.tipo.equals(
                    ItemBalancoPatrimonial.ITEM_INSUMOS
                ) || item.tipo.equals(
                    ItemBalancoPatrimonial.ITEM_PRODUTOS
                )
            ) {
                total += (item.quantidadeFinal.toBigDecimal() * item.valorUnitario.toBigDecimal() + item.reforma.toBigDecimal())
            }
        }
        return total.toString()
    }

    fun calcularPatrimonioBens(): String {

        var total = BigDecimal.ZERO
        for (item in listaItens) {
            item.calcularDepreciacao()
            if (item.tipo != ItemBalancoPatrimonial.ITEM_DIVIDAS_LONGO_PRAZO) {
                total += (item.quantidadeFinal.toBigDecimal() * item.valorAtual.toBigDecimal())
            }
        }
        return total.toString()
    }


    fun calcularPatrimonioLiquido() {
        if (passivo.toBigDecimal() != BigDecimal.ZERO) {
            patrimonioLiquido = (ativo.toBigDecimal() - passivo.toBigDecimal()).toString()
        } else {
            patrimonioLiquido = ativo
        }
    }

    fun calcularLucro() {
        lucro = (receitaBruta.toBigDecimal() - calcularCustoTotal()).toString()
    }

    fun calcularCustoTotal(): BigDecimal {
        return custoOperacionalTotal.toBigDecimal() + calcularOportunidadeCapital() + custoOportunidadeTrabalho.toBigDecimal()
    }

    fun calcularMargemLiquida() {

        margemLiquida =
            (receitaBruta.toBigDecimal() - custoOperacionalTotal.toBigDecimal()).toString()
    }

    fun calcularMargemBruta() {
        margemBruta =
            (receitaBruta.toBigDecimal() - custoOperacionalEfetivo.toBigDecimal()).toString()
    }

    fun calcularReceitaBruta() {
        receitaBruta =
            (totalReceitas.toBigDecimal() + calcularValorProdutos() + totalContasReceber.toBigDecimal()).toString()
    }

    fun calcularValorProdutos(): BigDecimal {
        var valorProdutos = BigDecimal.ZERO
        for (item in listaItens) {
            if (item.tipo == ItemBalancoPatrimonial.ITEM_PRODUTOS && item.anoProducao == 2020
            ) {
                valorProdutos += (item.quantidadeFinal.toBigDecimal() * item.valorAtual.toBigDecimal())
            }
        }
        return valorProdutos
    }


    fun calcularOportunidadeCapital(): BigDecimal {
        return ativo.toBigDecimal() * taxaRemuneracaoCapital.toBigDecimal()
    }


    fun calcularCustoOperacionalTotal() {

        var depreciacao = BigDecimal.ZERO
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_BENFEITORIA) || item.tipo.equals(
                    ItemBalancoPatrimonial.ITEM_MAQUINAS
                )
            )
                item.calcularDepreciacao()
            depreciacao += item.depreciacao.toBigDecimal()
        }
        if (depreciacao < 1.toBigDecimal()) {
            depreciacao = 0.toBigDecimal()
        }

        custoOperacionalTotal =
            (custoOperacionalEfetivo.toBigDecimal() + depreciacao + trabalhoFamiliarNaoRemunerado.toBigDecimal()).toString()
    }


    fun calcularCustoOperacionalEfetivo() {
        var reformas = 0f
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_BENFEITORIA) || item.tipo.equals(
                    ItemBalancoPatrimonial.ITEM_MAQUINAS
                )
            )
                reformas += item.reforma.toFloat()
        }

        Log.e("despesas:", totalDespesas)
        custoOperacionalEfetivo =
            (totalDespesas.toFloat() +
                    totalContasPagar.toFloat()
                    - reformas).toString()


    }


    private fun calcularRentabilidade() {
        calcularMargemBruta()
        try {
            rentabilidade =
                (margemBruta.toBigDecimal() / patrimonioLiquido.toBigDecimal()).toString()
        } catch (e: Exception) {
            rentabilidade = margemBruta
        }

    }


    fun calcularValorAnimais(): Double {
        var valorAnimais = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_ANIMAIS)) {
                valorAnimais += (item.quantidadeFinal.toDouble() * item.valorUnitario.toDouble() + item.reforma.toDouble())
            }
        }
        return valorAnimais
    }

    fun calcularValorTerras(): Double {
        var valorAnimais = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_TERRA)) {
                valorAnimais += (item.quantidadeFinal.toDouble() * item.valorUnitario.toDouble() + item.reforma.toDouble())
            }
        }
        return valorAnimais
    }

    fun calcularValorMaquinas(): Double {
        var valorAnimais = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_MAQUINAS)) {
                valorAnimais += (item.quantidadeFinal.toDouble() * item.valorAtual.toDouble() + item.reforma.toDouble())
            }
        }
        return valorAnimais
    }

    fun calcularValorBenfeitorias(): Double {
        var valorAnimais = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_BENFEITORIA)) {
                valorAnimais += (item.quantidadeFinal.toDouble() * item.valorAtual.toDouble() + item.reforma.toDouble())
            }
        }
        return valorAnimais
    }


    fun calcularValorInsumos(): Double {
        var valorInsumos = 0.0
        for (item in listaItens) {
            if (item.tipo.equals(ItemBalancoPatrimonial.ITEM_INSUMOS)) {
                valorInsumos += (item.quantidadeFinal.toDouble() * item.valorUnitario.toDouble() + item.reforma.toDouble())
            }
        }
        return valorInsumos
    }


    fun calcularDividasLongoPrazo() {
        var valor = BigDecimal.ZERO
        for (item in listaItens) {
            if (item.tipo == ItemBalancoPatrimonial.ITEM_DIVIDAS_LONGO_PRAZO) {
                valor += item.quantidadeFinal.toBigDecimal() * item.valorAtual.toBigDecimal()

            }
        }
        if (valor != BigDecimal.ZERO) {
            dividasLongoPrazo = valor.toString()
        }
    }


}