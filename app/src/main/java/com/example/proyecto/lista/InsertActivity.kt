package com.example.proyecto.lista

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.example.proyecto.R
import com.example.proyecto.modelo.DatabaseHelper
import com.example.proyecto.modelo.Producto




class InsertActivity : AppCompatActivity() {
    lateinit var save : Button
    lateinit var edtFname : EditText
    lateinit var edtLname : EditText
    lateinit var spinner: Spinner


    lateinit var  databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        save=findViewById<Button>(R.id.btn_save)
        edtFname=findViewById(R.id.edt_fname)
        edtLname=findViewById(R.id.edt_lname)

        spinner=findViewById(R.id.spinner_std)
        databaseHelper=DatabaseHelper(this)


        save.setOnClickListener {
            if (edtFname.text.toString() == ""){
               Toast.makeText(this,"No ha añadido ningún producto",Toast.LENGTH_LONG).show()
            } else {
                insertFunction()
                finish()
          }
        }

    }



    private fun insertFunction() {


        val nombreP = edtFname.text.toString()
        val marcaP = edtLname.text.toString()


        val isTipo = spinner.selectedItemPosition

        val product = Producto()
        product.nombre = nombreP
        product.marca = marcaP

        product.isTipo = isTipo

            val result: Boolean = databaseHelper.onStoreData(product)

            when {
                result -> {
                    Toast.makeText(this, "Producto añadido", Toast.LENGTH_LONG).show()
                    finish()
                }
                else -> Toast.makeText(this, "Error al insertar producto", Toast.LENGTH_LONG).show()
            }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId) {
            R.id.cancelar -> {
                val intent = Intent(this, ListaCompra::class.java)
                startActivity(intent)
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
