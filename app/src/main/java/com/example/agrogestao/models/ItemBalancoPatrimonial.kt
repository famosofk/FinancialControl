package com.example.agrogestao.models

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
    }

    var nome: String = "";
    var dataCompra: String = ""
    var idItem = UUID.randomUUID().toString()
    var quantidadeInicial: Float = 0f
    var quantidadeFinal: Float = 0f
    var receitaBruta: Float = 0f
    var valorUnitario: Float = 0f
    var atividade: String = ""
    var valorAtual: Float = 0f
    var valorInicial: Float = 0f
    var vidaUtil: Int = 99999
    var tipo: String = ""
    var depreciacao: Float = 0f
    var reforma: Float = 0f
    var anoProducao: Int = 0
    var precoString: String = ""
        get() = "R$ $valorAtual"
    var quantidadeString: String = ""
        get() = "$quantidadeFinal un"

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

    fun getQuantidadeText(): String {
        return "$quantidadeFinal un"
    }

    fun getValorFinal(): String {
        return "R$: $valorAtual "
    }


}