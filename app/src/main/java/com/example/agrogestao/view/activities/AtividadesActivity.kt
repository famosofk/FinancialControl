package com.example.agrogestao.view.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.realmclasses.*
import com.example.agrogestao.view.adapter.MyFarmAdapter
import com.example.agrogestao.view.listener.FarmListener
import com.example.agrogestao.viewmodel.navigation.AtividadesViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_atividades.*
import kotlinx.android.synthetic.main.cadastro_programa_fazenda.view.*
import java.util.*


class AtividadesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var realm: Realm
    private lateinit var atividadesViewModel: AtividadesViewModel
    private val mAdapter = MyFarmAdapter()
    private lateinit var mListener: FarmListener
    private var list = mutableListOf<String>()
    private var programa = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividades)
        //  userVerification()

        realm = Realm.getDefaultInstance()


        atividadesViewModel = ViewModelProvider(this).get(AtividadesViewModel::class.java)

        observe()

        inicializarButtons()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerFazendas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

        mListener = object : FarmListener {
            override fun onClick(id: Int) {
                val intent = Intent(applicationContext, NavigationActivity::class.java)
                val bundle = Bundle()
                bundle.putString("fazenda", mAdapter.get(id).id)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        mAdapter.attachListener(mListener)
    }

    private fun observe() {

        atividadesViewModel.farmList.observe(this, androidx.lifecycle.Observer {
            mAdapter.updateFarms(it)
        })

    }


    private fun createAlertDialog(title: String, list: List<String>?) {

        if (list != null) {
            programa = list[0]
        }
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.cadastro_programa_fazenda, null)
        val cadastrarButton: Button = mDialogView.findViewById(R.id.cadastrarDialogButton)
        val cancelButton: Button = mDialogView.findViewById(R.id.cancelDialogButton)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle(title)
            .create()

        when (title) {
            "Criar programa" -> {
                mDialogView.textComplementoDialog.visibility = View.GONE
                mDialogView.editComplementoText.visibility = View.GONE
            }
            "Criar fazenda" -> {
                mDialogView.textProgramaDialog.visibility = View.VISIBLE
                mDialogView.spinnerPrograma.visibility = View.VISIBLE
            }

        }
        mBuilder.show()

        if (list != null) {
            val adapter = ArrayAdapter(
                applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )
            mDialogView.spinnerPrograma.adapter = adapter
            mDialogView.spinnerPrograma.onItemSelectedListener = this

            cadastrarButton.setOnClickListener {
                mBuilder.dismiss()
                val name = mDialogView.editNomeDialog.text.toString()
                val id = UUID.randomUUID().toString()
                val complemento = mDialogView.editComplementoText.text.toString()
                val farm = Farm(
                    codigoFazenda = name,
                    programa = programa,
                    senha = complemento,
                    id = id
                )
                farm.saveToDb()

                salvarRealm(farm = farm)
                val atividade = AtividadesEconomicas("Geral")

                atividade.fazendaID = farm.id
                atividade.rateio = 100.0
                atividade.saveToDb()
                salvarRealm(economicalActivity = atividade)


            }
        } else {
            cadastrarButton.setOnClickListener {
                mBuilder.dismiss()
                val name = mDialogView.editNomeDialog.text.toString()
                salvarRealm(
                    program = FarmProgram(
                        name
                    )
                )

            }
        }

        cancelButton.setOnClickListener { mBuilder.dismiss() }

    }

    private fun salvarRealm(
        farm: Farm? = null,
        economicalActivity: AtividadesEconomicas? = null,
        program: FarmProgram? = null
    ) {

        realm.beginTransaction()
        when {
            farm != null -> {
                realm.copyToRealm(farm)
                val balancoPatrimonial =
                    BalancoPatrimonial()
                balancoPatrimonial.farmID = farm.id
                val fluxoCaixa =
                    FluxoCaixa()
                fluxoCaixa.farmID = farm.id
                realm.copyToRealm(fluxoCaixa)
                realm.copyToRealm(balancoPatrimonial)
                fluxoCaixa.saveToDb()
                balancoPatrimonial.saveToDb()

                //criar
            }
            economicalActivity != null -> {
                realm.copyToRealm(economicalActivity)
            }
            program != null -> {
                program.saveToDb()
                realm.copyToRealm(program)
            }
        }
        realm.commitTransaction()
        atividadesViewModel.load()
    }


    private fun inicializarButtons() {
        val fabPrograma: com.github.clans.fab.FloatingActionButton =
            findViewById(R.id.fabAddPrograma)
        fabPrograma.setOnClickListener {
            createAlertDialog("Criar programa", null)
        }
        fabParticiparFazenda.setOnClickListener {
            val i = Intent(this, FazendaActivity::class.java)
            startActivity(i)
            finish()
        }
        val fabFazenda: com.github.clans.fab.FloatingActionButton = findViewById(R.id.fabAddFazenda)
        fabFazenda.setOnClickListener {
            realm = Realm.getDefaultInstance()
            val query = realm.where<FarmProgram>().findAll()

            if (query.count() < 1) {
                Toast.makeText(
                    applicationContext,
                    "Para criar uma fazenda, primeiro crie um programa.",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                for (programas in query) {
                    list.add(programas.name)
                }
                createAlertDialog("Criar fazenda", list)
            }
        }

    }

    private fun userVerification() {
        val auth = Firebase.auth
        val user = auth.currentUser
        if (!user?.displayName.equals("professor")) {
            fabAddFazenda.visibility = View.GONE
            fabAddPrograma.visibility = View.GONE

        }
    }

    override fun onStop() {
        super.onStop()
        realm.close()
    }

    override fun onResume() {
        super.onResume()
        atividadesViewModel.load()
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        programa = list[position]
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {}


}

