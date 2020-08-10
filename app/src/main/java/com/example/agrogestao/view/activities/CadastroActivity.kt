package com.example.agrogestao.view.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrogestao.R
import com.example.agrogestao.models.realmclasses.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        mAuth = FirebaseAuth.getInstance()
        radioConsultorCadastro.setOnClickListener(this)
        radioProdutorCadastro.setOnClickListener(this)
        cadastroButton.setOnClickListener { createUser() }

    }


    fun createUser() {
        progressBar.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        cadastroFrameLayout.setBackgroundColor(Color.parseColor("#EF000000"))
        val senhaInserida = editSenhaConsultorCadastro.text.toString()
        if (!(senhaInserida.equals(getString(R.string.senhaConsultor)) || senhaInserida.equals(
                getString(R.string.senhaProfessor)
            )) && radioConsultorCadastro.isChecked
        ) {
            Toast.makeText(this, "Senha de consultor incorreta.", Toast.LENGTH_SHORT).show()
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            cadastroFrameLayout.setBackgroundColor(Color.WHITE)
            progressBar.visibility = View.GONE
        } else {
            var tipoUsuario: String =
                if (radioConsultorCadastro.isChecked) "consultor" else "produtor"
            if (tipoUsuario == "consultor") {
                if (senhaInserida.equals(getString(R.string.senhaProfessor))) {
                    tipoUsuario = "professor"
                }
            }
            val email = editEmailCadastro.text.toString()
            val senha = editSenhaCadastro.text.toString()
            mAuth.createUserWithEmailAndPassword(
                email, senha
            ).addOnCompleteListener(this) { task ->
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                cadastroFrameLayout.setBackgroundColor(Color.WHITE)
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(tipoUsuario)
                        .build()
                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { task ->

                            if (!task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "erro ao atualizar dados.",
                                    Toast.LENGTH_SHORT
                                )
                            }

                            val usuario = Usuario(tipoUsuario)
                            usuario.email = email
                            usuario.senha = senha
                            usuario.saveToDb()


                            startActivity(Intent(this, AtividadesActivity::class.java))
                            finish()
                        }
                } else {
                    Toast.makeText(this, "Falha ao cadastrar."+ task.exception, Toast.LENGTH_SHORT).show()
                }
                }
        }


    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.radioConsultorCadastro) {
            editSenhaConsultorCadastro.visibility = View.VISIBLE
        }
        if (id == R.id.radioProdutorCadastro) {
            editSenhaConsultorCadastro.visibility = View.INVISIBLE
        }
    }


}
