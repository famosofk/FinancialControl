package com.example.agrogestao.models

import io.realm.RealmObject

open class ItemFluxoCaixa(var tipo: Boolean = false) : RealmObject() {

    var valor = 0f;
    var nome = ""
    var data = ""
    var conta = ""
    var quantidadeInicial = ""
    var valorUnitario = 0f
    var valorAmortizado = 0f
    var reforma = false
    var pagamentoPrazo = false
    var dataPagamentoPrazo = ""
    var tipoReforma = ""
    var itemReforma = ""
}