package com.example.agrogestao.view.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.agrogestao.R
import com.example.agrogestao.models.AtividadesEconomicas
import com.example.agrogestao.models.Farm
import com.example.agrogestao.models.FarmProgram
import com.example.agrogestao.viewmodel.AtividadesViewModel
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.cadastro_programa_fazenda.view.*
import java.util.*

class AtividadesActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var atividadesViewModel: AtividadesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividades)
        atividadesViewModel = ViewModelProvider(this).get(AtividadesViewModel::class.java)


        observe();

        realm = Realm.getDefaultInstance()
        inicializarButtons()


    }

    private fun observe() {

        atividadesViewModel.farmList.observe(this, androidx.lifecycle.Observer {

        })

    }


    private fun createAlertDialog(title: String) {
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.cadastro_programa_fazenda, null)
        val cadastrarButton: Button = mDialogView.findViewById(R.id.cadastrarDialogButton)
        val cancelButton: Button = mDialogView.findViewById(R.id.cancelDialogButton)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle(title)
            .create()

        if (title.equals("Criar programa")) {
            mDialogView.textComplementoDialog.visibility = View.GONE
            mDialogView.editComplementoText.visibility = View.GONE
        } else if (title.equals("Criar fazenda")) {
            mDialogView.editProgramaDialog.visibility = View.VISIBLE
            mDialogView.textProgramaDialog.visibility = View.VISIBLE
        } else if (title.equals("Criar atividade")) {
            mDialogView.textComplementoDialog.visibility = View.GONE
            mDialogView.editComplementoText.visibility = View.GONE
        }
        mBuilder.show()
        cadastrarButton.setOnClickListener {
            mBuilder.dismiss()

            val name = mDialogView.editNomeDialog.text.toString()
            val id = UUID.randomUUID().toString()

            if (title.equals("Criar programa")) {
                salvarRealm(program = FarmProgram(name))
            } else if (title.equals("Criar fazenda")) {
                val complemento = mDialogView.editComplementoText.text.toString()
                val programa = mDialogView.editProgramaDialog.text.toString()
                val farm = Farm(name, programa, complemento, id)
                val db = FirebaseDatabase.getInstance().reference.child("farms").child(id)
                db.setValue(farm)
                salvarRealm(farm = farm)
            } else if (title.equals("Criar atividade")) {
                val atividade = AtividadesEconomicas(name)
                salvarRealm(economicalActivity = atividade)
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
            }
            economicalActivity != null -> {
                realm.copyToRealm(economicalActivity)
            }
            program != null -> {
                realm.copyToRealm(program)
            }
        }
        realm.commitTransaction()
    }

    private fun createToast(info: String) {
        Toast.makeText(applicationContext, info, Toast.LENGTH_SHORT).show()
    }


    override fun onStop() {
        super.onStop()
        realm.close()
    }

    private fun inicializarButtons() {
        val fabPrograma: com.github.clans.fab.FloatingActionButton =
            findViewById(R.id.fabAddPrograma)
        fabPrograma.setOnClickListener { view ->
            createAlertDialog("Criar programa")
        }
        val fabFazenda: com.github.clans.fab.FloatingActionButton = findViewById(R.id.fabAddFazenda)
        fabFazenda.setOnClickListener { view ->
            realm = Realm.getDefaultInstance()
            val query = realm.where<FarmProgram>()

            if (query.count() < 1) {
                createToast("Para criar uma fazenda, primeiro crie um programa.")
            } else {
                createAlertDialog("Criar fazenda")
            }
        }
        val fabAtividade: com.github.clans.fab.FloatingActionButton =
            findViewById(R.id.fabAddAtividade)
        fabAtividade.setOnClickListener { view -> createAlertDialog("Criar atividade") }
    }


}
