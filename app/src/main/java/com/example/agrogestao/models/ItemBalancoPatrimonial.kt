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
        const val ITEM_DIVIDAS_LONGO_PRAZO = "Dívidas de longo prazo"
        const val ITEM_DIVIDAS_A_VENCER = "Dívidas a vencer"
    }

    var nome: String = "";
    var idItem = UUID.randomUUID().toString()
    var idFazenda = ""
    var quantidadeInicial: Float = 0f
    var quantidadeFinal: Float = 0f
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
        get() = "R$ ${String.format(
            "%.2f",
            valorInicial * quantidadeFinal + reforma - depreciacao
        )}"
    var quantidadeString: String = ""
        get() = "$quantidadeFinal un"

    fun calcularValorAtual() {
        valorAtual = valorInicial - depreciacao + reforma
    }

    fun calcularDepreciacao() {
        depreciacao = valorInicial / vidaUtil
        calcularValorAtual()
    }

    fun getQuantidadeText(): String {
        return "$quantidadeFinal un"
    }

    fun getValorFinal(): String {
        return "R$: ${valorAtual + reforma}) "
    }


}