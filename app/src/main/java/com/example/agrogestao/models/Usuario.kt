package com.example.agrogestao.models

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Usuario(s: String = "") {

    var email: String = ""
    var tipo = s
    var senha = ""
    var listaFazendas = ArrayList<String>()

    fun saveToDb() {
        val database = Firebase.database
        val myRef = database.getReference().child("users").child(email.split('@')[0])
        myRef.setValue(this)
    }

}