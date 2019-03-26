package com.example.proyecto.adapter

import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.proyecto.R
import com.example.proyecto.lista.ListaCompra
import com.example.proyecto.lista.UpdateActivity
import com.example.proyecto.modelo.DatabaseHelper



class DBAdapter(val listaCompra: ListaCompra,
                val arrayList: ArrayList<HashMap<String, String>>): RecyclerView.Adapter<DBAdapter.ViewHolder>() {

    lateinit var helper : DatabaseHelper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vi= LayoutInflater.from(listaCompra).inflate(R.layout.item_task,parent,false)
        return ViewHolder(vi)
    }

    override fun getItemCount(): Int {
        return arrayList.size


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.nombre.text=arrayList.get(position).get(PRODUCTO)
        holder.marca.text=arrayList.get(position).get(MARCA)

        if (arrayList.get(position).get(TIPO)!!.toInt() == 0) {
            holder.tipo.setImageResource(R.drawable.ave)
        } else if (arrayList.get(position).get(TIPO)!!.toInt() == 1) {
            holder.tipo.setImageResource(R.drawable.carne)
        } else if (arrayList.get(position).get(TIPO)!!.toInt() == 2) {
            holder.tipo.setImageResource(R.drawable.drogueria)
        } else if (arrayList.get(position).get(TIPO)!!.toInt() == 3) {
            holder.tipo.setImageResource(R.drawable.fruta)
        } else if (arrayList.get(position).get(TIPO)!!.toInt() == 4) {
            holder.tipo.setImageResource(R.drawable.lacteos)
        } else if (arrayList.get(position).get(TIPO)!!.toInt() == 5) {
            holder.tipo.setImageResource(R.drawable.pescado)

        }

        helper= DatabaseHelper(listaCompra)
        Log.d("hiiii", arrayList.toString())
        //holder.container.setOnClickListener(onClickListener(position))

        holder.update.setOnClickListener {
            val intent = Intent(listaCompra, UpdateActivity::class.java)
            intent.putExtra(ID, arrayList.get(position).get(ID))
            intent.putExtra(PRODUCTO,arrayList.get(position).get(PRODUCTO))
            intent.putExtra(MARCA, arrayList.get(position).get(MARCA))
            intent.putExtra(TIPO,arrayList.get(position).get(TIPO))
            listaCompra.startActivity(intent)

        }

        holder.delete.setOnClickListener {
            val result : Boolean = helper.deleteUser(Integer.parseInt(arrayList.get(position).get(ID)))

            when{
                result->{
                    Toast.makeText(listaCompra,"Data deleted Successfully..", Toast.LENGTH_LONG).show()
                   val intent = Intent(listaCompra, ListaCompra::class.java)

                    listaCompra.startActivity(intent)
                    listaCompra.finish()
                }
                else-> Toast.makeText(listaCompra,"Failed to delete data", Toast.LENGTH_LONG).show()
            }

        }

    }





    inner class ViewHolder(item : View) : RecyclerView.ViewHolder(item) {

        val tipo: ImageView
        val nombre: TextView
        val marca: TextView
        val container: View
        val update: ImageButton
        val delete: ImageButton


        init {
            tipo = item.findViewById<View>(R.id.imageP) as ImageView
            nombre = item.findViewById<View>(R.id.nameProduct) as TextView
            marca = item.findViewById<View>(R.id.marcaProduct) as TextView
            container = item.findViewById(R.id.card_view) as CardView
            update = item.findViewById(R.id.btn_actualizar) as ImageButton
            delete = item.findViewById(R.id.btn_borrar)as ImageButton
        }
    }


    companion object {

        private val ID = "Id"
        private val PRODUCTO = "nameP"
        private val MARCA= "marcaP"
        private val TIPO = "tipoP"


    }
}

