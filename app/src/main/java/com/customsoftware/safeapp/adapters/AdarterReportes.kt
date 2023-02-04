package com.customsoftware.safeapp.adapters

import android.content.Context
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.R
import com.customsoftware.safeapp.modelos.Fraccionamientos
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class AdarterReportes (val fraccionamientos:ArrayList<Fraccionamientos>, clickListener: AdarterReportes.ClickListener): RecyclerView.Adapter<AdarterReportes.ViewHolder>(){

    var position= -1
    private var FraccionamientoList: List<Fraccionamientos> = arrayListOf()
    private var clickListener: ClickListener = clickListener
    private lateinit var context: Context

    fun setData(fraccionamientos: List<Fraccionamientos>){
        this.FraccionamientoList = fraccionamientos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AdarterReportes.ViewHolder {
        var v= LayoutInflater.from(parent.context).inflate(R.layout.items_fracc_rv,parent, false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return fraccionamientos.size
    }

    override fun onBindViewHolder(holder: AdarterReportes.ViewHolder, position:Int) {
        this.position = position
        holder.bindItems(fraccionamientos[position])

        var fractionation = fraccionamientos[position]
        var estado = fractionation.estado
        var pais = fractionation.pais
        var municipio = fractionation.municipio
        var etapa = fractionation.etapa
        var nombre = fractionation.nombre

        holder.nombre.text = nombre
        holder.etapa.text = etapa
        holder.municipio.text = municipio
        holder.pais.text = pais
        holder.estado.text = estado

        holder.itemView.setOnClickListener {
            clickListener.clickedItem(fractionation)
        }
    }

    interface ClickListener{
        fun clickedItem(fraccionamiento: Fraccionamientos)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nombre = itemView.findViewById<TextView>(R.id.item_fracc)
        var pais = itemView.findViewById<TextView>(R.id.item_pais)
        var estado = itemView.findViewById<TextView>(R.id.item_estado)
        var municipio = itemView.findViewById<TextView>(R.id.item_municipio)
        var etapa = itemView.findViewById<TextView>(R.id.item_etapa)

        fun bindItems(fraccionamiento: Fraccionamientos){

            val txtFracc = itemView.findViewById<TextView>(R.id.item_fracc)
            val txtPais = itemView.findViewById<TextView>(R.id.item_pais)
            val txtMunicipio = itemView.findViewById<TextView>(R.id.item_municipio)
            val txtEstado = itemView.findViewById<TextView>(R.id.item_estado)
            val txtEtapa = itemView.findViewById<TextView>(R.id.item_etapa)
            val btnRvDelete = itemView.findViewById<Button>(R.id.btnRvDelete)
            var FRACCIONAMIENTO : String = fraccionamiento.nombre

            txtFracc.text=fraccionamiento.nombre
            txtPais.text=fraccionamiento.pais
            txtMunicipio.text=fraccionamiento.municipio
            txtEstado.text=fraccionamiento.estado
            txtEtapa.text=fraccionamiento.etapa

            btnRvDelete.setOnClickListener {
                eliminar(FRACCIONAMIENTO)
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

        private fun eliminar(FRACCIONAMIENTO: String) {
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: Int = stm.executeUpdate("DELETE FROM SP_FRACCIONAMIENTO WHERE FRACCIONAMIENTO = '$FRACCIONAMIENTO'")
            } catch (e: java.lang.Exception) {
            }
        }
    }
}