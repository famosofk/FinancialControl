package com.example.agrogestao.models

class ItemBalancoPatrimonial(val nome: String, val dataCompra: String) {

    companion object {
        const val ITEM_TERRA = 'a'
        const val ITEM_BENFEITORIA = 'b'
        const val ITEM_MAQUINAS = 'c'
        const val ITEM_ANIMAIS = 'd'
        const val ITEM_INSUMOS = 'e'
        const val ITEM_PRODUTOS = 'f'
    }

    var quantidadeInicial: Float = 0f
    var quantidadeFinal: Float = 0f
    var receitaBruta: Float = 0f
    var valorUnitario: Float = 0f
    var valorAtual: Float = 0f
    var valorInicial: Float = 0f
    var vidaUtil: Int = 0
    var tipo: Char = 'a'
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