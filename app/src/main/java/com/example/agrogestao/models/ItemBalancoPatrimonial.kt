package com.example.agrogestao.models

import com.google.firebase.database.Exclude
import io.realm.RealmObject
import java.util.*

open class ItemBalancoPatrimonial() : RealmObject() {


    companion object {
        const val ITEM_TERRA = "Terra"
        const val ITEM_BENFEITORIA = "Benfeitoria"
        const val ITEM_MAQUINAS = "Máquinas"
        const val ITEM_ANIMAIS = "Animais"
        const val ITEM_INSUMOS = "Insumos"
        const val ITEM_PRODUTOS = "Produtos"
        const val ITEM_DIVIDAS_LONGO_PRAZO = "Dívidas de longo prazo"
    }

    var nome: String = "";
    var idItem = UUID.randomUUID().toString()
    var idFazenda = ""
    var quantidadeInicial = 0
    var quantidadeFinal = 0
    var valorUnitario = "0.00"
    var atividade: String = ""
    var valorAtual = "0.00"
    var valorInicial = "0.00"
    var vidaUtil: Int = 99999
    var tipo = ""
    var depreciacao = "0.00"
    var reforma = "0.00"
    var anoProducao: Int = 0

    @Exclude
    fun calcularValorAtual() {
        valorAtual =
            (valorInicial.toBigDecimal() - depreciacao.toBigDecimal() + reforma.toBigDecimal()).toString()
    }

    @Exclude
    fun calcularDepreciacao() {
        depreciacao = (valorInicial.toBigDecimal() / vidaUtil.toBigDecimal()).toString()
        calcularValorAtual()
    }




}