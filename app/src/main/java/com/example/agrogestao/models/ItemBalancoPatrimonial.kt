package com.example.agrogestao.models

import io.realm.RealmObject

open class ItemBalancoPatrimonial(nome: String = "", dataCompra: String = "") : RealmObject() {

    companion object {
        const val ITEM_TERRA = "Terra"
        const val ITEM_BENFEITORIA = "Benfeitoria"
        const val ITEM_MAQUINAS = "Máquinas"
        const val ITEM_ANIMAIS = "Animais"
        const val ITEM_INSUMOS = "Insumos"
        const val ITEM_PRODUTOS = "Produtos"
    }

    var quantidadeInicial: Float = 0f
    var quantidadeFinal: Float = 0f
    var receitaBruta: Float = 0f
    var valorUnitario: Float = 0f
    var valorAtual: Float = 0f
    var valorInicial: Float = 0f
    var vidaUtil: Int = 0
    var tipo: String = ""
    var depreciacao: Float = 0f
    var reforma: Float = 0f
    var anoProducao: Int = 0

    fun calcularValorAtual() {
        calcularDepreciacao()
        valorAtual = valorInicial - depreciacao + reforma
    }

    fun calcularDepreciacao() {
        depreciacao = valorAtual / calcularVidaUtilRestante()
    }


    fun calcularVidaUtilRestante(): Float {
        //return hoje - dataCompra
        return 0f
    }


}