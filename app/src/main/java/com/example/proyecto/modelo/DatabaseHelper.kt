package com.example.proyecto.modelo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                ID + " INTEGER PRIMARY KEY," +
                PRODUCTO + " TEXT," + MARCA + " TEXT," +
                TIPO + " TEXT);"
        db!!.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME
        db!!.execSQL(DROP_TABLE)
        onCreate(db)
    }



    fun onStoreData(producto: Producto) : Boolean {
        val db : SQLiteDatabase =this.writableDatabase
        val contentValues= ContentValues()
        contentValues.put(PRODUCTO,producto.nombre)
        contentValues.put(MARCA,producto.marca)
        contentValues.put(TIPO,producto.isTipo)
        val insert_data = db.insert(TABLE_NAME,null,contentValues)
        db.close()

        return !insert_data.equals(-1)
    }

    val producto: List<Producto>
        get() {
            val productoList = ArrayList<Producto>()
            val db = writableDatabase
            val selectQuery = "SELECT  * FROM $TABLE_NAME"
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor != null) {
                cursor.moveToFirst()
                while (cursor.moveToNext()) {
                    val producto = Producto()
                    producto.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    producto.nombre = cursor.getString(cursor.getColumnIndex(PRODUCTO))
                    producto.marca = cursor.getString(cursor.getColumnIndex(MARCA))
                    producto.isTipo = cursor.getInt(cursor.getColumnIndex(TIPO))
                    productoList.add(producto)
                }
            }
            cursor.close()
            return productoList
        }

    fun updateUser(producto: Producto): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(PRODUCTO,producto.nombre)
        values.put(MARCA,producto.marca)
        values.put(TIPO,producto.isTipo)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(producto.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteUser(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAll() {
        val db = this.writableDatabase
       val _succses = db.delete(TABLE_NAME, null, null).toLong()
        //db.execSQL("DELETE FROM $TABLE_NAME")
       // db.execSQL("TRUNCATE  $TABLE_NAME")
        db.close()

    }


    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "CompraList"
        private val TABLE_NAME = "Compra"
        private val ID = "Id"
        private val PRODUCTO = "nameP"
        private val MARCA= "marcaP"
        private val TIPO = "tipoP"


    }
}