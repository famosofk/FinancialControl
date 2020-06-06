package com.example.agrogestao.models

import io.realm.RealmObject

open class FarmProgram(var name: String = "") : RealmObject() {
    var numeroEjs = 0;
}