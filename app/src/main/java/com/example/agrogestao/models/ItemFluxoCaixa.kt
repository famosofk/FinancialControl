package com.example.agrogestao.models

import io.realm.RealmObject
import java.util.*

open class ItemFluxoCaixa(var tipo: Boolean = false) : RealmObject() {

    var itemID = UUID.randomUUID().toString()
    var valorAtual = 0.0
    var valorInicial = 0.0
    var nome = ""
    var data = ""
    var anoProducao = 2020
    var quantidadeInicial = 0.0

    var valorAmortizado = 0.0
    var reforma = false
    var pagamentoPrazo = false
    var dataPagamentoPrazo = ""

    var atividade = ""


}