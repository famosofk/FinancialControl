package com.example.agrogestao.models

class BalancoPatrimonial() {

    var solvencia: Float = 0f
    var liquidez: Float = 0f
    var listaItens = arrayListOf<Item>()
    var margemLiquida: Float = 0f
    var margemBruta: Float = 0f
    var taxaRemuneracaoCapital: Float = 0f //em porcentagem. Lido ao cadastrar fazenda.
    var receitaBruta: Float = 0f
    var custoOperacionalEfetivo: Float = 0f
    var custoOperacionalTotal: Float = 0f
    var totalDespesas: Float = 0f
    var totalReceitas: Float =
        0f  // Não será inserido. É calculado a partir de receitas e despesas cadastrados.
    var ativo: Float = 0f
    var passivo: Float = 0f
    var patrimonioLiquido: Float = 0f
    var totalContasPagar: Float = 0f
    var totalContasReceber: Float = 0f
    var rentabilidade: Float = 0f
    var lucro: Float = 0f
    var saldo: Float = 0f
    var dinheiroBanco: Float = 0f
    var custoOportunidadeTrabalho: Float = 0f //valor. Lido ao cadastrar balanço.
    var trabalhoFamiliarNaoRemunerado: Float = 0f //valor. Lido ao cadastrar balanço.
    var pendenciasPagamento: Float =
        0f //Será atualizado com o valor das contas não pagas do ano anterior.
    var pendenciasRecebimento: Float =
        0f //Será atualizado com o valor das contas não pagas do ano anterior.

    fun atualizarBalanco(contasPagar: Float, dividas: Float) {

    }

    fun calcularSolvencia(dividaLongoPrazo: Float) {
        solvencia = dividaLongoPrazo / patrimonioLiquido
        //divida é um atributo da fazenda.
    }

    fun calcularLiquidez(contasPagar: Float) {

        liquidez = contasPagar / (saldo + calcularValorAnimaisInsumosProdutos() + dinheiroBanco)

    }

    fun calcularValorAnimaisInsumosProdutos(): Float {
        var total: Float = 0.0f
        for (item in listaItens!!) {
            if (item.tipo.equals(Item.ITEM_ANIMAIS) || item.tipo.equals(Item.ITEM_INSUMOS) || item.tipo.equals(
                    Item.ITEM_PRODUTOS
                )
            ) {
                total += (item.quantidadeFinal * item.valorUnitario)
            }
        }
        return total
    }

    fun calcularPatrimonioBens(): Float {
        return listaItens.sumByDouble { (it.valorUnitario * it.quantidadeFinal).toDouble() }
            .toFloat()
    }


    fun calcularPatrimonioLiquido() {
        patrimonioLiquido = ativo - passivo
    }

    fun calcularLucro() {
        lucro = receitaBruta - custoOperacionalTotal
    }

    fun calcularMargemLiquida() {
        margemLiquida = receitaBruta - custoOperacionalTotal
    }

    fun calcularMargemBruta() {
        margemBruta = receitaBruta - custoOperacionalEfetivo
    }

    fun calcularReceitaBruta() {
        receitaBruta = totalReceitas + calcularValorProdutos()
    }

    fun calcularValorProdutos(): Float {
        var valorProdutos: Float = 0.0f
        for (item in listaItens!!) {
            if (item.tipo.equals(Item.ITEM_PRODUTOS)) {
                valorProdutos += (item.quantidadeFinal * item.valorUnitario)
            }
        }
        return valorProdutos
    }

    fun calcularCustoTotal(): Float {
        return custoOperacionalTotal + calcularOportunidadeCapital() + custoOportunidadeTrabalho
    }

    fun calcularOportunidadeCapital(): Float {
        return patrimonioLiquido * taxaRemuneracaoCapital // (será inserido)
    }

    fun calcularCustoOportunidadeCapital(): Float {
        return patrimonioLiquido * taxaRemuneracaoCapital
    }

    fun calcularCustoOperacionalTotal(depreciacao: Float) {
        custoOperacionalTotal =
            custoOperacionalEfetivo + depreciacao + trabalhoFamiliarNaoRemunerado
    }


    fun calcularCustoOperacionalEfetivo() {
        custoOperacionalEfetivo = totalDespesas + totalContasPagar - pendenciasPagamento
    }

    fun calcularSaldo() {
        saldo = totalReceitas - totalDespesas
    }

    fun calcularRentabilidade() {
        rentabilidade = receitaBruta - patrimonioLiquido
    }


    fun calcularValorAnimais(): Float {
        var valorAnimais: Float = 0.0f
        for (item in listaItens!!) {
            if (item.tipo.equals(Item.ITEM_ANIMAIS)) {
                valorAnimais += (item.quantidadeFinal * item.valorUnitario)
            }
        }
        return valorAnimais
    }

    fun calcularValorInsumos(): Float {
        var valorInsumos: Float = 0.0f
        for (item in listaItens!!) {
            if (item.tipo.equals(Item.ITEM_INSUMOS)) {
                valorInsumos += (item.quantidadeFinal * item.valorUnitario)
            }
        }
        return valorInsumos
    }

}