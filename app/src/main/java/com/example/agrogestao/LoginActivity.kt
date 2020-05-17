package com.example.agrogestao

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun loginUsuario(view:View){

        auth.signInWithEmailAndPassword(editEmailLogin.text.toString(), editSenhaLogin.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                } else {
                    Toast.makeText(baseContext, "Authentication failed." + task.exception,
                        Toast.LENGTH_SHORT).show()
                }
            }


    }

    fun redefinirSenha(view: View){
        val auth = FirebaseAuth.getInstance()
        val emailAddress = editEmailLogin.text.toString()

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("Seu e-mail foi enviado.")
                }
            }
    }



    fun toast(s:String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {
        val id = v.id

        if (id == R.id.loginButton) {
        }

        if (id == R.id.forgotPasswordText) {
        }

        if (id == R.id.signupButton) {
            val i = Intent(this, CadastroActivity::class.java)
            startActivity(i)
        }
    }

}
