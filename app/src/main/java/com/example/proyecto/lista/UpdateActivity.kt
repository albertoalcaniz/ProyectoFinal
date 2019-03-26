package com.example.proyecto.lista


import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.example.proyecto.R
import com.example.proyecto.modelo.DatabaseHelper
import com.example.proyecto.modelo.Producto


class UpdateActivity : AppCompatActivity() {
    lateinit var update : Button
    lateinit var edtFname : EditText
    lateinit var edtLname : EditText
    lateinit var spinner: Spinner

    lateinit var  databaseHelper: DatabaseHelper

    var id: String = ""
    var nameP: String = ""
    var marcaP: String = ""
    var tipoP: String = ""


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        update= this.findViewById(R.id.btn_update)
        edtFname=findViewById(R.id.edt_fname)
        edtLname=findViewById(R.id.edt_lname)
        spinner=findViewById(R.id.spinner_std)


        databaseHelper=DatabaseHelper(this)

        edtFname.setText(intent.getStringExtra(PRODUCTO).toString())
        id=intent.getStringExtra(ID)
        edtLname.setText(intent.getStringExtra(MARCA))


        tipoP=intent.getStringExtra(TIPO)

        val testArray = resources.getStringArray(R.array.standard)



        val adapter=ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,testArray)
        spinner.adapter=adapter

        if(tipoP!=null) {
            val spinnerPosition = adapter.getPosition(tipoP)
            spinner.setSelection(spinnerPosition)
        }

        update.setOnClickListener {
            UpdateData()
            finish()
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

    private fun UpdateData() {

        nameP=edtFname.text.toString()
        marcaP=edtLname.text.toString()

        tipoP=spinner.selectedItemPosition.toString()

        val producto = Producto()
        producto.id=Integer.parseInt(id)
        producto.nombre=nameP
        producto.marca=marcaP
        producto.isTipo= tipoP.toInt()


        val result : Boolean = databaseHelper.updateUser(producto)

        when{
            result->{
                Toast.makeText(this,"Producto actualizado",Toast.LENGTH_LONG).show()
                finish()
            }
            else->Toast.makeText(this,"Error al actualizar",Toast.LENGTH_LONG).show()
        }

    }


    companion object {

        private val ID = "Id"
        private val PRODUCTO = "nameP"
        private val MARCA= "marcaP"
        private val TIPO = "tipoP"


    }
}
