package com.example.agrogestao.view.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrogestao.R
import com.example.agrogestao.models.Farm
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.cadastro_programa_fazenda.view.*
import java.util.*

class AtividadesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividades)

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
            } else if (title.equals("Criar fazenda")) {
                var farm = Farm(name, programa, complemento, id)
                val db = FirebaseDatabase.getInstance().getReference().child("fazendas").child(id)
                db.setValue(farm)

            } else if (title.equals("Criar atividade")) {
                //salvar no dispositivo
            }

              Toast.makeText(this, "" + name+complemento, Toast.LENGTH_SHORT).show()
        }
        cancelButton.setOnClickListener { mBuilder.dismiss() }

    }
}
