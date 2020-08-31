package com.example.agrogestao.view.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrogestao.R
import com.example.agrogestao.models.firebaseclasses.AtividadeFirebase
import com.example.agrogestao.models.firebaseclasses.BalancoFirebase
import com.example.agrogestao.models.firebaseclasses.FluxoCaixaFirebase
import com.example.agrogestao.models.realmclasses.*
import com.example.agrogestao.view.adapter.MyFarmAdapter
import com.example.agrogestao.view.listener.FarmListener
import com.example.agrogestao.viewmodel.navigation.AtividadesViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_atividades.*
import kotlinx.android.synthetic.main.cadastro_programa_fazenda.view.*
import java.util.*
import kotlin.collections.ArrayList


class AtividadesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var realm: Realm
    private lateinit var atividadesViewModel: AtividadesViewModel
    private val mAdapter = MyFarmAdapter()
    private lateinit var mListener: FarmListener
    private var list = mutableListOf<String>()
    private var programa = ""
    private lateinit var idFarm: String
    private lateinit var farm: Farm
    private var farmBool = false
    private lateinit var balancoPatrimonial: BalancoPatrimonial
    private var balancoBool = false
    private lateinit var fluxoCaixa: FluxoCaixa
    private var fluxoBool = false
    private var atividadesBool = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividades)
        userVerification()

        realm = Realm.getDefaultInstance()
        atividadesViewModel = ViewModelProvider(this).get(AtividadesViewModel::class.java)

        observe()

        inicializarButtons()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerFazendas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

        mListener = object : FarmListener {
            override fun onClick(id: Int) {
                idFarm = mAdapter.get(id).id
                farm = realm.where<Farm>().contains("id", idFarm).findFirst()!!
                balancoPatrimonial =
                    realm.where<BalancoPatrimonial>().contains("farmID", idFarm).findFirst()!!
                fluxoCaixa = realm.where<FluxoCaixa>().contains("farmID", idFarm).findFirst()!!
                dataVerification(mAdapter.get(id).programa)
            }
        }

        mAdapter.attachListener(mListener)
    }

    private fun dataVerification(programa: String) {
        if (!Usuario.isOnline(applicationContext)) {
            val bundle = Bundle()
            bundle.putString("fazenda", idFarm)
            val intent = Intent(applicationContext, NavigationActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        } else {
            habilitarLoading()
            getFarm(programa)
            getBalanco()
            getAtividades()
            getFluxo()
        }
    }

    private fun getFarm(programa: String) {
        val db = Firebase.database.reference.child("fazendas").child(programa).child(idFarm)
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val firebaseFarm = snapshot.getValue(Farm::class.java)
                if (firebaseFarm != null) {
                    if (!farm.myEquals(firebaseFarm)) {
                        realm.beginTransaction()
                        val farmResult = realm.where<Farm>().contains("id", idFarm).findAll()
                        farmResult.deleteAllFromRealm()
                        realm.copyToRealm(firebaseFarm)
                        realm.commitTransaction()
                    }
                }
                farmBool = true
                startActivity()
            }
        }
        db.addListenerForSingleValueEvent(listener)
    }

    private fun getBalanco() {
        val db = Firebase.database.reference.child("balancoPatrimonial").child(idFarm)
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val firebaseFarm = snapshot.getValue(BalancoFirebase::class.java)
                if (firebaseFarm != null) {
                    if (balancoPatrimonial.modificacao.toLong() < firebaseFarm.modificacao.toLong()) {
                        if (firebaseFarm.listaItens.size >= balancoPatrimonial.listaItens.size) {
                            realm.beginTransaction()
                            realm.where<BalancoPatrimonial>().contains("farmID", idFarm).findAll()
                                .deleteAllFromRealm()
                            realm.copyToRealm(BalancoPatrimonial(firebaseFarm))
                            realm.commitTransaction()
                        }
                    }
                }
                balancoBool = true
                startActivity()
            }
        }
        db.addListenerForSingleValueEvent(listener)
    }

    private fun getAtividades() {
        val db =
            Firebase.database.reference.child("atividadesEconomicas").child(idFarm).orderByKey()
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaAtividades = ArrayList<AtividadesEconomicas>()
                snapshot.children.forEach {
                    val atividadeFirebase = it.getValue(AtividadeFirebase::class.java)
                    if (atividadeFirebase != null) {
                        listaAtividades.add(AtividadesEconomicas(atividadeFirebase))
                    }
                }
                if (listaAtividades.size > 0) {
                    var item = AtividadesEconomicas()
                    listaAtividades.forEach {
                        if (it.nome.equals("Geral")) {
                            item = it
                        }
                    }

                    val geralLocal =
                        realm.where<AtividadesEconomicas>().contains("fazendaID", idFarm)
                            .contains("nome", "Geral").findFirst()!!
                    if (geralLocal.modificacao.toLong() < item.modificacao.toLong()) {

                        realm.beginTransaction()
                        val results =
                            realm.where<AtividadesEconomicas>().contains("fazendaID", idFarm)
                                .findAll()
                        results.deleteAllFromRealm()
                        listaAtividades.forEach {
                            realm.copyToRealm(it)
                        }
                        realm.commitTransaction()
                    }
                }
                atividadesBool = true
                startActivity()
            }
        }
        db.addListenerForSingleValueEvent(listener)
    }

    private fun getFluxo() {
        val db = Firebase.database.reference.child("fluxoCaixa").child(idFarm)
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val firebaseFluxo = snapshot.getValue(FluxoCaixaFirebase::class.java)
                if (firebaseFluxo != null) {
                    val aux = FluxoCaixaFirebase(fluxoCaixa)
                    if (firebaseFluxo != aux) {

                        if (fluxoCaixa.modificacao.toLong() < firebaseFluxo.modificacao.toLong()) {
                            if (firebaseFluxo.list.size > fluxoCaixa.list.size) {
                                realm.beginTransaction()
                                realm.where<FluxoCaixa>().contains("farmID", idFarm).findFirst()
                                    ?.deleteFromRealm()
                                realm.copyToRealm(FluxoCaixa(firebaseFluxo))
                                realm.commitTransaction()
                            }
                        }
                    }

                }
                fluxoBool = true
                startActivity()
            }

        }
        db.addListenerForSingleValueEvent(listener)
    }

    private fun startActivity() {
        if (farmBool && fluxoBool && atividadesBool && balancoBool) {
            desabilitarLoading()
            val bundle = Bundle()
            bundle.putString("fazenda", idFarm)
            val intent = Intent(applicationContext, NavigationActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }
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
                farm.attModificacao()
                realm.copyToRealm(farm)
                val balancoPatrimonial = BalancoPatrimonial()
                balancoPatrimonial.farmID = farm.id
                balancoPatrimonial.attModificacao()
                val fluxoCaixa = FluxoCaixa()
                fluxoCaixa.farmID = farm.id
                fluxoCaixa.attModificacao()
                realm.copyToRealm(fluxoCaixa)
                realm.copyToRealm(balancoPatrimonial)
                fluxoCaixa.saveToDb()
                balancoPatrimonial.saveToDb()
            }
            economicalActivity != null -> {
                economicalActivity.attModificacao()
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


    private fun downloadProgramas() {
        if (Usuario.isOnline(applicationContext)) {
            habilitarLoading()
            val db = Firebase.database.reference.child("programas")
            val listener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    realm.beginTransaction()
                    val result = realm.where<FarmProgram>().findAll()

                        snapshot.children.forEach {
                            val programa = it.getValue(FarmProgram::class.java)
                            if (programa != null && (result.where().contains("name", programa.name)
                                    .count() == 0.toLong())
                            ) {
                                realm.copyToRealm(programa)
                            }
                        }

                    realm.commitTransaction()
                    desabilitarLoading()
                }
            }
            db.addListenerForSingleValueEvent(listener)
        }
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
                list.clear()
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
        downloadProgramas()

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

    private fun habilitarLoading() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        atividadesLoadingLayout.visibility = View.VISIBLE
    }

    private fun desabilitarLoading() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        atividadesLoadingLayout.visibility = View.GONE
    }

}


