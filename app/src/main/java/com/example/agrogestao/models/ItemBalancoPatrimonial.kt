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
    var quantidadeInicial = 0.0
    var quantidadeFinal = 0.0
    var valorUnitario = 0.0
    var atividade: String = ""
    var valorAtual = 0.0
    var valorInicial = 0.0
    var vidaUtil: Int = 99999
    var tipo = ""
    var depreciacao = 0.0
    var reforma = 0.0
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