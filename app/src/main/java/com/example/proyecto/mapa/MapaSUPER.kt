package com.example.proyecto.mapa

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import com.example.proyecto.R
import com.example.proyecto.lista.ListaCompra
import com.example.proyecto.login.MainActivity
import com.example.proyecto.modelo.DatabaseHelper
import com.example.proyecto.modelo.Supermercado
import com.example.proyecto.servicio.SupermercadoServices
import com.example.proyecto.ui.Inicio
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_mapa_super.*
import kotlinx.android.synthetic.main.map_nav.*

class MapaSUPER : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private lateinit var gMap: GoogleMap
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_nav)
        setSupportActionBar(toolbar)

        databaseHelper= DatabaseHelper(this)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.inicio,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
            R.id.nav_compra -> {
            val intent = Intent(this, ListaCompra::class.java)
                startActivity(intent)
                finish()
        }
            R.id.nav_inicio -> {
                val intent = Intent(this, Inicio::class.java)
                startActivity(intent)
                finish()
            }

            R.id.nav_salir ->{
                databaseHelper.deleteAll()
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(this, MainActivity::class.java)
                finishAffinity()
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMapReady(p0: GoogleMap?) {
        gMap = p0!!
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location?) {
                if (p0 != null) {
                    val userLocation = LatLng(p0!!.latitude, p0!!.longitude)
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f))
                    gMap.addMarker(MarkerOptions().position(userLocation).title("Yo"))

                }

            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) { }

            override fun onProviderEnabled(p0: String?) { }

            override fun onProviderDisabled(p0: String?) { }

        }
        //Solicitar permisos
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        } else {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1f, locationListener)
        }

        val posicionActual = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val obtenerLugares = GetPlaces(this, posicionActual)
        obtenerLugares.execute()
    }



    internal inner class GetPlaces(var context: Context, location: Location?): AsyncTask<Void, Void, Void>(){
        var supermercados: ArrayList<Supermercado>? = null
        var localizacionActual: Location? = location
        override fun doInBackground(vararg p0: Void?): Void? {
            supermercados = getSupermercadosFromGoogle(gMap, localizacionActual)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            setMarkers(gMap, supermercados!!)
        }

        fun getMarkerFromResult(result: Supermercado): MarkerOptions {
            val mo = MarkerOptions()
            mo.position(LatLng(result.latitude, result.longitude)).title(result.name).snippet(result.formatted_address)
                .infoWindowAnchor(0.5f, 0.5f)

            return mo
        }
        fun setMarkers(map: GoogleMap, makers: ArrayList<Supermercado>){
            for (marker in makers){
                map.addMarker(getMarkerFromResult(marker))
            }
        }

        fun getSupermercadosFromGoogle(map: GoogleMap, location: Location?): ArrayList<Supermercado> {
            val service = SupermercadoServices(R.string.google_maps_key.toString())
            val misSupermercados: ArrayList<Supermercado> = service.findSupermarkets(location!!.latitude,
                location.longitude
            )
            return misSupermercados
        }


    }

}