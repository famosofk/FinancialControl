package com.example.agrogestao.view.fragments.selecionarfazenda

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.firebaseclasses.AtividadeFirebase
import com.example.agrogestao.models.firebaseclasses.BalancoFirebase
import com.example.agrogestao.models.firebaseclasses.FluxoCaixaFirebase
import com.example.agrogestao.models.realmclasses.AtividadesEconomicas
import com.example.agrogestao.models.realmclasses.BalancoPatrimonial
import com.example.agrogestao.models.realmclasses.Farm
import com.example.agrogestao.models.realmclasses.FluxoCaixa
import com.example.agrogestao.view.activities.AtividadesActivity
import com.example.agrogestao.view.adapter.SelectFarmAdapter
import com.example.agrogestao.view.listener.FarmListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.kotlin.where


class SelecionarFazendaFragment : Fragment() {

    val adapter = SelectFarmAdapter()
    private lateinit var root: View
    val farmsList = ArrayList<Farm>()
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        realm = Realm.getDefaultInstance()

        root = inflater.inflate(R.layout.fragment_selecionar_fazenda, container, false)
        if (arguments == null) {
            requireActivity().finish()
        }
        val listener = object : FarmListener {
            override fun onClick(id: Int) {
                createAlertDialog(adapter.currentList[id])
            }
        }
        adapter.attachListener(listener)


        val recycler = root.findViewById<RecyclerView>(R.id.recyclerSelecionarFazenda)
        recycler.adapter = adapter
        callFarms()

        return root
    }

    fun callFarms() {
        val caminho = arguments?.getString("programa")
        val db = Firebase.database.reference.child("fazendas").child(caminho!!)
        val eventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val snap = snapshot.children
                snap.forEach {
                    val item = it.getValue(Farm::class.java)
                    if (item != null) {
                        farmsList.add(item)
                    }
                }
                adapter.submitList(farmsList)
            }
        }
        db.addListenerForSingleValueEvent(eventListener)
    }

    fun createAlertDialog(farm: Farm) {

        val results = realm.where<Farm>().contains("id", farm.id).findAll()
        if (results.size > 0) {
            Toast.makeText(context, "Você já faz parte dessa fazenda", Toast.LENGTH_LONG).show()
        } else {
            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_participar_fazenda, null)
            val confirmarButton: Button = mDialogView.findViewById(R.id.confirmarEntradaFazenda)
            val cancelarButton: Button = mDialogView.findViewById(R.id.cancelarEntradaFazenda)
            val senhaFazenda: EditText = mDialogView.findViewById(R.id.senhaFazendaParticiparDialog)
            val mBuider = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setTitle(farm.codigoFazenda)
                .setMessage("Para participar da fazenda ${farm.codigoFazenda}, insira a senha no campo abaixo e clique no botão laranja. Caso contrário, clique no botão azul")
                .create()
            mBuider.show()
            confirmarButton.setOnClickListener {
                if (farm.senha.equals(senhaFazenda.text.toString())) {
                    realm.beginTransaction()
                    realm.copyToRealm(farm)
                    realm.commitTransaction()
                    val db = Firebase.database.reference.child("balancoPatrimonial").child(farm.id)
                    val listener = object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {}

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val firebase = snapshot.getValue(BalancoFirebase::class.java)
                            if (firebase != null) {
                                val balanco = BalancoPatrimonial(firebase)
                                realm.beginTransaction()
                                realm.copyToRealm(balanco)
                                realm.commitTransaction()

                                baixarFluxoCaixa(farm.id)
                            }
                        }
                    }

                    db.addListenerForSingleValueEvent(listener)

                } else {
                    Toast.makeText(context, "Senha incorreta.", Toast.LENGTH_SHORT).show()
                    senhaFazenda.text.clear()
                }
            }
            cancelarButton.setOnClickListener {
                mBuider.dismiss()
            }
        }
    }


    private fun finalizarActivity() {
        val i = Intent(context, AtividadesActivity::class.java)
        startActivity(i)
        requireNotNull(activity).finish()
    }

    private fun baixarFluxoCaixa(id: String) {
        val db = Firebase.database.reference.child("fluxoCaixa").child(id)
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val firebase = snapshot.getValue(FluxoCaixaFirebase::class.java)
                if (firebase != null) {
                    val fluxoCaixa = FluxoCaixa(firebase)
                    realm.beginTransaction()
                    realm.copyToRealm(fluxoCaixa)
                    realm.commitTransaction()
                    baixarAtividadesFazenda(id)
                }
            }
        }

        db.addListenerForSingleValueEvent(listener)
    }

    private fun baixarAtividadesFazenda(id: String) {
        val db = Firebase.database.reference.child("atividadesEconomicas").child(id)

        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                realm.beginTransaction()
                snapshot.children.forEach {
                    val atividade = it.getValue(AtividadeFirebase::class.java)
                    if (atividade != null) {
                        val atividadetoRealm = AtividadesEconomicas(atividade)
                        realm.copyToRealm(atividadetoRealm)
                    }
                }
                realm.commitTransaction()
                finalizarActivity()
            }

        }
        db.addListenerForSingleValueEvent(listener)

    }

}