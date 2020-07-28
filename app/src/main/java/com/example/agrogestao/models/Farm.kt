package com.example.agrogestao.models

import io.realm.RealmList
import io.realm.RealmObject
import java.io.Serializable

open class Farm(
    var codigoFazenda: String = "",
    var programa: String = "",
    var senha: String = "",
    var id: String = ""
) : RealmObject(), Serializable {


    var atividades: RealmList<AtividadesEconomicas> = RealmList()
    var area = 0.0
    var metaMargemLiquida = 0.0
    var metaMargemBruta = 0.0
    var metaRendaBruta = 0.0
    var metaPatrimonioLiquido = 0.0
    var metaLucro = 0.0
    var metasaldo = 0.0
    var metaLiquidezGeral = 0.0
    var metaLiquidezCorrente = 0.0
    var observacao: String = ""
}
