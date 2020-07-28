package com.example.agrogestao.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agrogestao.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("myrealm.realm")
            .build()
        Realm.setDefaultConfiguration(config)


        auth.setLanguageCode("pt")
        skipLogin()

        loginButton.setOnClickListener(this)
        forgotPasswordText.setOnClickListener(this)
        signupButton.setOnClickListener(this)


    }


    fun toast(s:String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {
        val id = v.id

        if (id == R.id.loginButton) {
            if (editEmailLogin.text.toString().isEmpty() || editSenhaLogin.text.toString()
                    .isEmpty()
            ) {
                toast("Para fazer login, e-mail e senha precisam ser informados.")
            } else {
                loginUsuario()
            }
        }

        if (id == R.id.forgotPasswordText) {
            if (editEmailLogin.text.toString().isEmpty()) {
                toast("Para redefinir a senha, preencha seu email.")
            } else {
                redefinirSenha()
            }
        }

        if (id == R.id.signupButton) {
            val i = Intent(this, CadastroActivity::class.java)
            startActivity(i)
        }
    }

    private fun redefinirSenha() {
        val auth = FirebaseAuth.getInstance()
        val emailAddress = editEmailLogin.text.toString()

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("Seu e-mail foi enviado.")
                } else {
                    toast("Falha ao enviar: " + task.exception.toString())
                }
            }
    }

    private fun loginUsuario() {

        auth.signInWithEmailAndPassword(
            editEmailLogin.text.toString(),
            editSenhaLogin.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed." + task.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun skipLogin() {
        val user: FirebaseUser? = auth.currentUser
        if (user != null) {
            val i = Intent(this, AtividadesActivity::class.java)
            startActivity(i)
            finish()
        }
    }

}
