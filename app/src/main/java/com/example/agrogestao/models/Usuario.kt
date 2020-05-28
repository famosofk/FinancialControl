package com.example.agrogestao.models

class Usuario(s: String) {

    var email: String = ""
    val tipo = s
    var listaFazendas: MutableList<String> = ArrayList<String>()

}