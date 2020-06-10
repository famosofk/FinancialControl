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


    var senhaAcesso: String = ""
    var municipio: String = ""
    var atividades: RealmList<AtividadesEconomicas> = RealmList()
    var area: Float = 0f
    var metaMargemLiquida: Float = 0f
    var metaMargemBruta: Float = 0f
    var metaRendaBruta: Float = 0f
    var metaTotalPagar: Float = 0f
    var metaTotalReceber: Float = 0f
    var metaPatrimonioLiquido: Float = 0f
    var metaLucro: Float = 0f
    var metasaldo: Float = 0f
    var metaLiquidezGeral: Float = 0f
    var metaLiquidezCorrente: Float = 0f
    var depreciacao: Float = 0f //Não será lido. Calculado através do inventário.
}
