package com.example.proyecto.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.proyecto.R
import com.example.proyecto.ui.Inicio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registro.*

class Registro : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        auth = FirebaseAuth.getInstance()
    }

    fun registrarUsuario(view: View) {

        val email = etCorreo.text.toString()
        val password = etPass.text.toString()
        if (etCorreo.text.toString() == ""){
            Toast.makeText(this, "Rellene el Correo Eléctronico", Toast.LENGTH_LONG).show()
        } else if (etPass.text.toString() == "") {
            Toast.makeText(this, "Rellene la contraseña", Toast.LENGTH_LONG).show()
        } else {
        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Log.d("TAG", "createUserWithEmail:success")
                    val user = auth!!.getCurrentUser()
                    enviarVerificacionEmail(user)
                    updateUI(user)


                } else {

                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Error de Autentificación",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }

            }
            }
    }

    fun enviarVerificacionEmail(usuario: FirebaseUser?){
        usuario!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "Email sent.")
                }
            }
    }

    fun updateUI(usuario: FirebaseUser?) {

            if (usuario!!.isEmailVerified) {

            } else {
                Toast.makeText(this, "Hemos enviado un correo a ${usuario.email} para su verificación", Toast.LENGTH_LONG).show()
                enviarSiguientePantalla()
            }
        }

    fun enviarSiguientePantalla(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }


}
