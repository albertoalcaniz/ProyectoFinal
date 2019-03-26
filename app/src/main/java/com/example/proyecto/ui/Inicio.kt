package com.example.proyecto.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.proyecto.mapa.MapaSUPER
import com.example.proyecto.R
import com.example.proyecto.lista.ListaCompra
import com.example.proyecto.login.MainActivity
import com.example.proyecto.modelo.DatabaseHelper

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_inicio.*
import kotlinx.android.synthetic.main.app_bar_inicio.*

class Inicio : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        databaseHelper= DatabaseHelper(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inicio, menu)
        return true
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_salir -> {
                databaseHelper.deleteAll()
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(this, MainActivity::class.java)
                finishAffinity()
                startActivity(intent)
            }
            R.id.nav_compra -> {
                val intent = Intent(this, ListaCompra::class.java)
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

    fun bCompra(view: View){
        val intent = Intent(this, ListaCompra::class.java)
        startActivity(intent)
    }
    fun bSupermercado(view: View){
        val intent = Intent(this, MapaSUPER::class.java)
        startActivity(intent)
    }




}


