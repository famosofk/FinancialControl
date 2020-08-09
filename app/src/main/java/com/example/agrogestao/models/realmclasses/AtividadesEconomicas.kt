package com.example.agrogestao.models.realmclasses

import android.annotation.SuppressLint
import com.example.agrogestao.models.firebaseclasses.AtividadeFirebase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.RealmList
import io.realm.RealmObject
import java.text.SimpleDateFormat
import java.util.*

open class AtividadesEconomicas(var nome: String = "") : RealmObject() {

    constructor(firebase: AtividadeFirebase) : this() {
        fazendaID = firebase.fazendaID
        rateio = firebase.rateio
        nome = firebase.nome
        custoDeProducao = firebase.custoDeProducao
        vendasAtividade = firebase.vendasAtividade
        lucroAtividade = firebase.lucroAtividade
        arrayCustos.addAll(firebase.arrayCustos)
        custoSemente = firebase.custoSemente
        custoFertilizante = firebase.custoFertilizante
        custoDefensivo = firebase.custoDefensivo
        custoMaodeobra = firebase.custoMaodeobra
        custoMaquina = firebase.custoMaquina
        custoOutros = firebase.custoOutros
        modificacao = firebase.modificacao
    }

    var fazendaID: String = ""
    var rateio: Double = 1.0
    var custoDeProducao: Double = 0.0
    var vendasAtividade: Double = 0.0
    var lucroAtividade: String = ""
        get() = "R$: ${vendasAtividade - custoDeProducao}"

    var arrayCustos = RealmList<Double>(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    var custoSemente = 0.0
    var custoFertilizante = 0.0
    var custoDefensivo = 0.0
    var custoMaodeobra = 0.0
    var custoMaquina = 0.0
    var custoOutros = 0.0
    var modificacao: String = ""

    @SuppressLint("SimpleDateFormat")
    fun attModificacao() {
        val todayDate: Date = Calendar.getInstance().getTime()
        val formatter = SimpleDateFormat("dd/MMM/yyyy HH:mm:ss")
        val todayString: String = formatter.format(todayDate)
        modificacao = todayString
    }

    fun saveToDb() {
        attModificacao()
        val db = Firebase.database.reference.child("atividadesEconomicas").child(fazendaID)
            .child(this.nome)
        db.setValue(this)

    }


}