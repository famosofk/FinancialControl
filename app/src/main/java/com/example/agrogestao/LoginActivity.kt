package com.example.agrogestao

import android.R.attr.password
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

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
}
