package com.example.proyecto.modelo


import org.json.JSONObject

class Supermercado {

    var id: String? = null
    var icon: String? = null
    var name: String? = null
    var formatted_address: String? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0


    companion object {
        internal fun jsonToPointReference(pointRef: JSONObject): Supermercado? {
            try {
                val result = Supermercado()
                val geometry = pointRef.get("geometry") as JSONObject
                val location = geometry.get("location") as JSONObject
                result.latitude = location.get("lat") as Double
                result.longitude = location.get("lng") as Double
                result.icon = pointRef.get("icon") as String
                result.name = pointRef.get("name") as String
                result.formatted_address = pointRef.get("formatted_address") as String
                result.id = pointRef.get("id") as String

                return result
            } catch (e: Exception){
                e.printStackTrace()
            }
            return null
        }
    }
}