package com.example.agrogestao.view.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.agrogestao.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AtividadesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividades)


        val fab: FloatingActionButton = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener { view ->
            createAlertDialog("teste")
        }
    }

    private fun createAlertDialog(title: String) {
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.cadastro_programa_fazenda, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle(title)
            .create()
        mBuilder.show()

        /*  cadastrarDialogButton.setOnClickListener{
              mBuilder.dismiss()
              val name = mDialogView.editNomeDialog.text.toString()
              val complemento = mDialogView.editComplementoText.text.toString()
              Toast.makeText(this, "" + name+complemento, Toast.LENGTH_SHORT).show()
          } */

    }
}
