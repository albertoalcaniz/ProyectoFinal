package com.example.proyecto.lista

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.proyecto.mapa.MapaSUPER
import com.example.proyecto.R
import com.example.proyecto.adapter.DBAdapter
import com.example.proyecto.login.MainActivity
import com.example.proyecto.modelo.DatabaseHelper
import com.example.proyecto.modelo.Producto
import com.example.proyecto.ui.Inicio
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlinx.android.synthetic.main.activity_lista_compra.*
import java.util.ArrayList

class ListaCompra: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var fab: FloatingActionButton? = null

    lateinit var databaseHelper: DatabaseHelper
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DBAdapter





    var hashMapArrayList: ArrayList<HashMap<String, String>> = ArrayList()

    var list: List<Producto>? = null
    var w = window

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.lista_nav)
        setSupportActionBar(toolbar2)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar2,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        fab = findViewById<View>(R.id.fab2) as FloatingActionButton
        recyclerView=findViewById(R.id.recilerViewCompra2)
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        databaseHelper= DatabaseHelper(this)



        fab!!.setOnClickListener {
            val intent = Intent(this, InsertActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inicio,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_salir -> {
                databaseHelper.deleteAll()
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(this, MainActivity::class.java)
                finishAffinity()
                startActivity(intent)

            }
            R.id.nav_inicio -> {
                val intent = Intent(this, Inicio::class.java)
                startActivity(intent)

            }
            R.id.nav_super -> {
                val intent = Intent(this, MapaSUPER::class.java)
                startActivity(intent)
            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true

    }

    override fun onResume() {
        ReadData()
        registerForContextMenu(recyclerView)
        super.onResume()
    }

    private fun ReadData() {
        val list=databaseHelper.producto

        hashMapArrayList.clear()
        if (list != null && list.size>0)
        {

            for (producto:Producto in list) {

                val hashMap = HashMap<String,String>()
                hashMap.put(ID, producto.id.toString())
                hashMap.put(PRODUCTO, producto.nombre)
                hashMap.put(MARCA, producto.marca)
                hashMap.put(TIPO,producto.isTipo.toString())
                hashMapArrayList.add(hashMap)

            }
            adapter = DBAdapter(this, hashMapArrayList)

            recyclerView.adapter= adapter
            Log.d("array", hashMapArrayList.toString())


        }else{
            Toast.makeText(this,"No se ha encontrado productos",Toast.LENGTH_LONG).show()

        }



    }
    companion object {

        private val ID = "Id"
        private val PRODUCTO = "nameP"
        private val MARCA= "marcaP"
        private val TIPO = "tipoP"


    }



}





