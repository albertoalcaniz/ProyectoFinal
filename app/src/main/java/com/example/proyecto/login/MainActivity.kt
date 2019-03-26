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


class MainActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


      mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        if (user != null) {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            this.finish()
        }

}

    //Botones
     fun entrar(view: View){
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()



        if (etEmail.text.toString() == ""){
            Toast.makeText(this, "Rellene el Correo Eléctronico", Toast.LENGTH_LONG).show()
        } else if (etPassword.text.toString() == "") {
            Toast.makeText(this, "Rellene la contraseña", Toast.LENGTH_LONG).show()
        } else {
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Log.d("TAG", "signInWithEmail:success")
                        val user = mAuth!!.currentUser


                        updateUI(user)



                    } else {

                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this, "Error de autentificación.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }


                }
        }
    }

   fun registrarse(view: View){
        val intenta = Intent(this, Registro::class.java )
        startActivity(intenta)
    }





   private fun updateUI(usuario: FirebaseUser?) {
        if (usuario != null){
            Log.i("TAG", "Usuario conectado")
            etEmail.setText("")
            etPassword.setText("")
            Toast.makeText(this, "Usuario Conectado: ${usuario.email}", Toast.LENGTH_LONG).show()



                if (usuario.isEmailVerified) {
                    enviarSiguientePantalla()


                } else {
                    Toast.makeText(this, "Verificaion del email: ${usuario.email}", Toast.LENGTH_LONG).show()

                }

        }

    }

    fun enviarSiguientePantalla(){
        val intent = Intent(this, Inicio::class.java)
        startActivity(intent)
        this.finish()
    }

    fun passOlvidado(view: View){
        val auth = FirebaseAuth.getInstance()
        val emailAddress = etEmail.text.toString()

        if (emailAddress == ""){
            Toast.makeText(this, "Rellena tu email", Toast.LENGTH_LONG).show()
        }else {

            auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "Email sent.")
                    }
                }
        }
    }







}







