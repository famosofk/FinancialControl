package com.example.agrogestao.models

data class Farm(var codigoFazenda: String) {

    val municipio: String = ""
    var atividades: String = ""
    var area: Float = 0f
    var margemLiquida: Float = 0f
    var margemBruta: Float = 0f
    var receitaBruta: Float = 0f
    var custoOperacionalEfetivo: Float = 0f
    var custoOperacionalTotal: Float = 0f
    var receitas: Float = 0f
    var despesas: Float = 0f
    var ativo: Float = 0f
    var passivo: Float = 0f
    var patrimonioLiquido: Float = 0f
    var solvencia: Float = 0f
    var liquidez: Float = 0f
    var rentabilidade: Float = 0f
    var lucro: Float = 0f
    var saldo: Float = 0f


    fun calcularPatrimonioLiquido() {
        patrimonioLiquido = ativo - passivo
    }

    fun calcularLucro() {
        lucro = receitaBruta //- custoTotal
        //falta esse atributo
    }

    fun calcularMargemLiquida() {
        margemLiquida = receitaBruta - custoOperacionalTotal
    }

    fun calcularMargemBruta() {
        margemBruta = receitaBruta - custoOperacionalEfetivo
    }

    fun calcularReceitaBruta() {
        receitaBruta = receitas //+ variacaoInventario + vendaPrazo - receitasPgtoAnoAnterior
        //faltam esses atributos
    }

    fun calcularCustoTotal() {
        // custototal = custoOperacionalTotal + custoOportunidadeCapital + custoOportunidadeTrabalho
        //tirando custoOperacionalTotal não tem nenhuma dessas variáveis.
    }

    fun calcularOportunidadeCapital() {
        custoOportunidadeCapital = patrimonioLiquido * taxaRemuneracaoCapital
    }

    fun calcularCustoOperacionalTotal() {
        custoOperacionalTotal =
            custoOperacionalEfetivo + depreciacao + trabalhoFamiliarNaoRemunerado
    }


    fun calcularCustoOperacionalEfetivo() {
        custoOperacionalEfetivo = despesas + contasPagar - contasPagarAnoAnterior
    }

    fun calcularSaldo() {
        receitas - despesas
    }

    fun calcularSolvencia() {

    }

    fun calcularLiquidez() {

    }

    fun calcularRentabilidade() {
        rentabilidade = receitaBruta - patrimonioLiquido
    }









}