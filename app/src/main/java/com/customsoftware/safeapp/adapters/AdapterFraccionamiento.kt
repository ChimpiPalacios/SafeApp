package com.customsoftware.safeapp.adapters

import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.Check_in
import com.customsoftware.safeapp.Menu_Fraccionamientos
import com.customsoftware.safeapp.R
import com.customsoftware.safeapp.modelos.Fraccionamientos
import com.customsoftware.safeapp.modelos.Residentes
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class AdapterFraccionamiento (var data:ArrayList<Fraccionamientos>, val context: Context, clickListener: ClickListener):RecyclerView.Adapter<AdapterFraccionamiento.ViewHolder>(),
    Filterable {

    var position= -1
    private var originalData = ArrayList<Fraccionamientos>()
    private var dataFilter = DataFilter()
    private var FraccionamientoList: List<Fraccionamientos> = arrayListOf()

    private var clickListener: ClickListener = clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AdapterFraccionamiento.ViewHolder {
        var view= LayoutInflater.from(parent.context).inflate(R.layout.items_fracc_rv,parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AdapterFraccionamiento.ViewHolder, position:Int) {
        this.position = position
        holder.bindItems(data[position])

        var fractionation = data[position]
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
            clickListener.clickedItem(fractionation,context)
        }
    }

    fun updateList(newList: ArrayList<Fraccionamientos>) {
        originalData = ArrayList(newList)
        data = ArrayList(newList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return dataFilter
    }

    fun getOriginalData(): ArrayList<Fraccionamientos> {
        return originalData
    }

    interface ClickListener{
        fun clickedItem(fraccionamiento: Fraccionamientos, context: Context)
        fun conexionDB(): Connection? {
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
                //**//
            }
            return cnn
        }
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
                eliminar(FRACCIONAMIENTO, txtPais.text.toString(), txtEstado.text.toString(), txtMunicipio.text.toString(), txtEtapa.text.toString())
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

        private fun eliminar(FRACCIONAMIENTO: String, PAIS: String, ESTADO: String, MUNICIPIO: String, ETAPA:String) {
            try {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Confirmación")
                builder.setMessage("Está seguro de eliminar el fraccionamiento: Fraccionamiento $FRACCIONAMIENTO, Municipio $MUNICIPIO, Estado $ESTADO  ")
                builder.setPositiveButton("Sí") { dialog, which ->
                    val stm: Statement = conexionDB()!!.createStatement()
                    var IDFRACC: String = ""

                    val rs = stm.executeQuery("SELECT ID_FRACCIONAMIENTO FROM SP_FRACCIONAMIENTO WHERE PAIS='$PAIS' AND ESTADO='$ESTADO' AND MUNICIPIO='$MUNICIPIO' AND FRACCIONAMIENTO='$FRACCIONAMIENTO' AND ETAPA = '$ETAPA' ")
                    if (rs.next()) {
                        IDFRACC = rs.getInt("ID_FRACCIONAMIENTO").toString()
                    }
                    val rs2: Int = stm.executeUpdate("DELETE FROM SP_FRACCIONAMIENTO WHERE ID_FRACCIONAMIENTO = $IDFRACC")
                    if(rs2 > 0){
                        Toast.makeText(itemView.context, "Eliminando Fraccionamiento...", Toast.LENGTH_LONG).show()
                        val intent = Intent(itemView.context, Menu_Fraccionamientos::class.java)
                        itemView.context.startActivity(intent)
                    }
                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()
            } catch (e: java.lang.Exception) {
                Toast.makeText(itemView.context, e.message, Toast.LENGTH_SHORT).show()
            }
        }


        }

    inner class DataFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Fraccionamientos>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(originalData)
            } else {
                val filterPattern = constraint.toString().trim()
                for (item in originalData) {
                    if (item.nombre.toLowerCase().contains(filterPattern) ||
                        item.estado.toLowerCase().contains(filterPattern) ||
                        item.etapa.toLowerCase().contains(filterPattern) ||
                        item.municipio.toLowerCase().contains(filterPattern) ||
                        item.pais.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            data.clear()
            data.addAll(results?.values as ArrayList<Fraccionamientos>)
            notifyDataSetChanged()
        }
    }
}



