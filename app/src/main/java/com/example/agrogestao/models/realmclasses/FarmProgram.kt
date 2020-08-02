package com.example.agrogestao.models.realmclasses

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.RealmObject

open class FarmProgram(var name: String = "") : RealmObject() {

    fun saveToDb() {
        val database = Firebase.database
        val db = database.getReference().child("programas").child(this.name)
        db.setValue(this)
    }


}