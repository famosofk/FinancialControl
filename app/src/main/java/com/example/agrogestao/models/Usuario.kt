package com.example.agrogestao.models

import io.realm.RealmList
import io.realm.RealmObject

open class Usuario(s: String = "") : RealmObject() {

    var email: String = ""
    var tipo = s
    var listaFazendas: RealmList<String> = RealmList()

}