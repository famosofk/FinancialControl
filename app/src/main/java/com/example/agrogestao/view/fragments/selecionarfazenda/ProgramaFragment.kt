package com.example.agrogestao.view.fragments.selecionarfazenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.realmclasses.FarmProgram
import com.example.agrogestao.view.adapter.ProgramaAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ProgramaFragment : Fragment() {

    private var root: View? = null
    private val adapter = ProgramaAdapter()
    private val listPrograms = ArrayList<FarmProgram>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.programa_fragment, container, false)

        val recyclerView: RecyclerView = root!!.findViewById(R.id.recyclerProgramas)
        recyclerView.adapter = adapter

        val db = Firebase.database.reference.child("programas")

        val valueListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val snap = snapshot.children
                snap.forEach {
                    val item = it.getValue(FarmProgram::class.java)
                    if (item != null) {
                        listPrograms.add(item)
                    }
                }
                adapter.submitList(listPrograms)
            }
        }
        db.addValueEventListener(valueListener)

        return root!!
    }


}