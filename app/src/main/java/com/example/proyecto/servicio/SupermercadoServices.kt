package com.example.proyecto.servicio

import com.example.proyecto.modelo.Supermercado
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL

class SupermercadoServices(private var API_KEY: String?) {
    fun setApiKey(apikey: String) {
        this.API_KEY = apikey
    }
    fun findSupermarkets(latitude: Double, longitude: Double): ArrayList<Supermercado>{
        val urlString = makeUrl(latitude, longitude, "supermercado")
        var arrayList = ArrayList<Supermercado>()
        try {
            val json = getJSON(urlString)
            val objeto = JSONObject(json)
            val array = objeto.getJSONArray("results")

            for (i in 0 until array.length()){
                    var supermercado =
                        Supermercado.jsonToPointReference(
                            array[i] as
                                    JSONObject)
                    arrayList.add(supermercado!!)
                }
            } catch (e: Exception){

           e.printStackTrace()
        }

        return arrayList
    }

    private fun makeUrl(latitude: Double, longitude: Double, supermercado: String): String {
        val urlString = StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?")


    urlString.append("&location=$latitude,$longitude")
    urlString.append("&radius=1000")
    urlString.append("&type=supermarket")
    urlString.append("&query=supermarket")
    urlString.append("&sensor=true")
        urlString.append("&key=AIzaSyAj-Y2Hp4AyTSx-TW4wlei03XukAmS24kA")

        return urlString.toString()
    }


    fun getJSON(theUrl: String): String {
        val content = StringBuilder()
        try {
            val url = URL(theUrl)
            val urlConnection = url.openConnection()
            val bufferedReader = BufferedReader(InputStreamReader(urlConnection.getInputStream()) as Reader?)
            var line: String?
            do {
                line = bufferedReader.readLine()
                content.append(line + "\n")
            } while (line != null)
            bufferedReader.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
        return content.toString()
    }
}