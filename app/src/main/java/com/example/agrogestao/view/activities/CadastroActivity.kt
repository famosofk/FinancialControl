package com.example.agrogestao.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrogestao.R
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

    }


    fun createUser(view: View) {

        if (!editSenhaConsultorCadastro.text.toString().equals(R.string.senhaConsultor) && radioConsultorCadastro.isChecked) {
            Toast.makeText(this, "Senha de produtor incorreta.", Toast.LENGTH_SHORT).show() }
        else {
            val tipoUsuario: String = if (radioConsultorCadastro.isChecked) "consultor" else "produtor"
            mAuth.createUserWithEmailAndPassword(
                editEmailCadastro.text.toString(),
                editSenhaCadastro.text.toString()).addOnCompleteListener(this) { task ->
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
