package com.example.agrogestao.view.fragments.selecionarfazenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agrogestao.R
import com.example.agrogestao.models.realmclasses.Farm
import com.example.agrogestao.view.adapter.SelectFarmAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SelecionarFazendaFragment : Fragment() {

    val adapter = SelectFarmAdapter()
    private lateinit var root: View
    val farmsList = ArrayList<Farm>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_selecionar_fazenda, container, false)
        if (arguments == null) {
            requireActivity().finish()
        }
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
        db.addValueEventListener(eventListener)




        return root
    }


}