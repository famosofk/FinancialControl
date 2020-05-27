package com.example.agrogestao.view.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrogestao.R
import kotlinx.android.synthetic.main.cadastro_programa_fazenda.view.*

class AtividadesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividades)


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
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle(title)
            .create()
        mBuilder.show()
        cadastrarButton.setOnClickListener {
              mBuilder.dismiss()
              val name = mDialogView.editNomeDialog.text.toString()
              val complemento = mDialogView.editComplementoText.text.toString()
              Toast.makeText(this, "" + name+complemento, Toast.LENGTH_SHORT).show()
        }

    }
}
