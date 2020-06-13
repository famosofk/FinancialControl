package com.example.agrogestao.models

import io.realm.RealmList
import io.realm.RealmObject

open class FluxoCaixa() : RealmObject() {

    var farm = ""
    var list = RealmList<ItemFluxoCaixa>()


}