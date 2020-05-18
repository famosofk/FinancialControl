package com.example.agrogestao.models

data class Item(val nome: String, val dataCompra: String) {

    companion object {
        const val ITEM_TERRA = 'a'
        const val ITEM_BENFEITORIA = 'b'
        const val ITEM_MAQUINAS = 'c'
        const val ITEM_ANIMAIIS = 'd'
        const val ITEM_INSUMOS = 'e'
        const val ITEM_PRODUTOS = 'f'
    }

    var descricao: String = ""
    var quantidade: Float = 0f
    var valorUnitario: Float = 0f
    var vidaUtil: Int = 0
    var atividade: Char = 'a'
    var tipo: Char = 'a'
    var depreciacao: Float = 0f

    fun calcularDepreciacao() {
        depreciacao = valorInicial / calcularVidaUtilRestante()
    }


    fun calcularVidaUtilRestante(): Int {
        return hoje - dataCompra
    }


}