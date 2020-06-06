package com.example.agrogestao.view.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrogestao.R
import com.example.agrogestao.models.AtividadesEconomicas
import com.example.agrogestao.models.Farm
import com.example.agrogestao.models.FarmProgram
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import kotlinx.android.synthetic.main.cadastro_programa_fazenda.view.*
import java.util.*

class AtividadesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividades)
        Realm.init(applicationContext)
        //preciso recuperar o usuário pra salvar ele no dispositivo e atualizar ao criar programas.


        val fabPrograma: com.github.clans.fab.FloatingActionButton =
            findViewById(R.id.fabAddPrograma)
        fabPrograma.setOnClickListener { view -> createAlertDialog("Criar programa") }
        val fabFazenda: com.github.clans.fab.FloatingActionButton = findViewById(R.id.fabAddFazenda)
        fabFazenda.setOnClickListener { view -> createAlertDialog("Criar fazenda") }
        val fabAtividade: com.github.clans.fab.FloatingActionButton =
            findViewById(R.id.fabAddAtividade)
        fabAtividade.setOnClickListener { view -> createAlertDialog("Criar atividade") }
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
            val complemento = mDialogView.editComplementoText.text.toString()
            val programa = mDialogView.editProgramaDialog.text.toString()
            val id = UUID.randomUUID().toString()
            if (title.equals("Criar programa")) {
                salvarRealm(program = FarmProgram(programa))
            }
            else if (title.equals("Criar fazenda")) {
                val farm = Farm(name, programa, complemento, id)
                val db = FirebaseDatabase.getInstance().reference.child("farms").child(id)
                db.setValue(farm)
                salvarRealm(farm=farm)

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
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        when {
            farm != null -> {
                realm.copyToRealm(farm)
                createToast("fazenda criada")
            }
            economicalActivity != null -> {
                realm.copyToRealm(economicalActivity)
                createToast("atividade criada")
            }
            program != null -> {

            }
        }
        realm.commitTransaction()
        realm.close()
    }

    private fun createToast(s: String){
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }


}
