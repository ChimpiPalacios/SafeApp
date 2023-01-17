package com.customsoftware.safeapp.adapters


import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safeapp.R
import com.example.safeapp.modelos.Residentes

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement



class AdapterFragmentResidente(val data: ArrayList<Residentes>, clicklistener: ClickListener) : RecyclerView.Adapter<AdapterFragmentResidente.MyViewHolder>() {
    var position= -1
    private var AdapterResidenteList : List<Residentes> = arrayListOf()
    private var clickListener: ClickListener = clicklistener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.irems_residentes_rv, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        this.position = position
        holder.bindItems(data[position])

        var residentes = data[position]
        var nombre =  residentes.nombre
        var numero_cel = residentes.numero_cel
        var ap = residentes.ap
        var am = residentes.am
        var perfil = residentes.perfil
        var iddom = residentes.iddom

        holder.nombre.text = nombre
        holder.numero_cel.text = numero_cel.toString()
        holder.ap.text = ap
        holder.am.text = am
        holder.perfil.text = perfil
        holder.iddom.text = iddom.toString()

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(residentes)
        }


    }

    interface ClickListener{
        fun clickedItem(residentes: Residentes)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nombre = itemView.findViewById<TextView>(R.id.item_nombre_residente)
        var numero_cel = itemView.findViewById<TextView>(R.id.item_numerocel_residente)
        var ap = itemView.findViewById<TextView>(R.id.item_ap_residente)
        var am = itemView.findViewById<TextView>(R.id.item_am_residente)
        var perfil = itemView.findViewById<TextView>(R.id.item_perfil_residente)
        var iddom = itemView.findViewById<TextView>(R.id.item_iddom_residente)



        fun bindItems(data: Residentes) {
            val txtNombre = itemView.findViewById<TextView>(R.id.item_nombre_residente)
            val txtNumeroCel = itemView.findViewById<TextView>(R.id.item_numerocel_residente)
            val txtAp = itemView.findViewById<TextView>(R.id.item_ap_residente)
            val txtAm = itemView.findViewById<TextView>(R.id.item_am_residente)
            val txtPerfil = itemView.findViewById<TextView>(R.id.item_perfil_residente)
            val txtIdDom = itemView.findViewById<TextView>(R.id.item_iddom_residente)
            val btnRvResidentesDelete = itemView.findViewById<Button>(R.id.btnRvResidentesDelete)
            //var RESIDENTE : String = data.idresidente.toString()
            txtNombre.text = data.nombre.toString()
            txtNumeroCel.text = data.nombre
            txtAp.text = data.nombre
            txtAm.text = data.nombre
            txtPerfil.text = data.nombre
            txtIdDom.text = data.nombre


            btnRvResidentesDelete.setOnClickListener{
                eliminar(txtNombre.text.toString())
            }

        }

        private fun conexionDB(): Connection? {
            var cnn: Connection? = null
            try {
                val politica = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(politica)
                Class.forName("com.mysql.jdbc.Driver").newInstance()
                cnn = DriverManager.getConnection(
                    "jdbc:mysql://www.customsoftware.com.mx:3306/i2721332_wp1",
                    "chimpi",
                    "Chimpi8108"
                )
            } catch (e: java.lang.Exception) {

            }
            return cnn
        }

        private fun eliminar(RESIDENTE: String) {
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: Int = stm.executeUpdate("DELETE FROM SP_RESIDENTE WHERE NOMBRE = '$RESIDENTE'")
            } catch (e: java.lang.Exception) {

            }
        }
    }

}

