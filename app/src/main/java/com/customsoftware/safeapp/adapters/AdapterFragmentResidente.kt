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
import com.customsoftware.safeapp.CallesFragment
import com.customsoftware.safeapp.Check_in
import com.customsoftware.safeapp.R
import com.customsoftware.safeapp.modelos.Residentes

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class AdapterFragmentResidente(var data: ArrayList<Residentes>,val context: Context, clicklistener: ClickListener) : RecyclerView.Adapter<AdapterFragmentResidente.MyViewHolder>(),
    Filterable {

    var position= -1
    private var originalData = ArrayList<Residentes>()
    private var dataFilter = DataFilter()
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
        var tipo_res = residentes.tipo_res
        var iddom = residentes.iddom

        holder.nombre.text = nombre
        holder.numero_cel.text = numero_cel
        holder.ap.text = ap
        holder.am.text = am
        holder.perfil.text = perfil
        holder.tipo_res.text = tipo_res
        holder.iddom.text = iddom.toString()

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(residentes,context)
        }
    }

    fun updateList(newList: ArrayList<Residentes>) {
        originalData = ArrayList(newList)
        data = ArrayList(newList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return dataFilter
    }

    fun getOriginalData(): ArrayList<Residentes> {
        return originalData
    }

    interface ClickListener{
        fun clickedItem(residentes: Residentes, context: Context)
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

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nombre = itemView.findViewById<TextView>(R.id.item_nombre_residente)
        var numero_cel = itemView.findViewById<TextView>(R.id.item_numerocel_residente)
        var ap = itemView.findViewById<TextView>(R.id.item_ap_residente)
        var am = itemView.findViewById<TextView>(R.id.item_am_residente)
        var perfil = itemView.findViewById<TextView>(R.id.item_perfil_residente)
        var tipo_res = itemView.findViewById<TextView>(R.id.item_tipo_res_residente)
        var iddom = itemView.findViewById<TextView>(R.id.item_iddom_residente)

        fun bindItems(data: Residentes) {
            val txtNombre = itemView.findViewById<TextView>(R.id.item_nombre_residente)
            val txtNumeroCel = itemView.findViewById<TextView>(R.id.item_numerocel_residente)
            val txtAp = itemView.findViewById<TextView>(R.id.item_ap_residente)
            val txtAm = itemView.findViewById<TextView>(R.id.item_am_residente)
            val txtPerfil = itemView.findViewById<TextView>(R.id.item_perfil_residente)
            val txtTipoRes = itemView.findViewById<TextView>(R.id.item_tipo_res_residente)
            val txtIdDom = itemView.findViewById<TextView>(R.id.item_iddom_residente)
            val btnRvResidentesDelete = itemView.findViewById<Button>(R.id.btnRvResidentesDelete)

            txtNombre.text = data.nombre
            txtNumeroCel.text = data.numero_cel
            txtAp.text = data.ap
            txtAm.text = data.am
            txtPerfil.text = data.perfil
            txtTipoRes.text = data.tipo_res
            txtIdDom.text = data.iddom.toString()

            btnRvResidentesDelete.setOnClickListener{
                eliminar(txtNombre.text.toString(),txtNumeroCel.text.toString(),txtAp.text.toString(),txtAm.text.toString(),txtPerfil.text.toString(),txtTipoRes.text.toString(),txtIdDom.text.toString())
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

        private fun eliminar(RESIDENTE: String,NUMEROCEL: String, AP: String, AM: String, PERFIL: String, TIPORES: String, IDDOM: String) {
            try {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Confirmación")
                builder.setMessage("Está seguro de eliminar al residente: $RESIDENTE $AP $AM?")
                builder.setPositiveButton("Sí") { dialog, which ->
                val stm: Statement = conexionDB()!!.createStatement()
                var IDRESIDENTE: String = ""

                val rs = stm.executeQuery("SELECT IDRESIDENTE FROM SP_RESIDENTE WHERE NOMBRE='$RESIDENTE' AND NUMEROCEL='$NUMEROCEL' AND AP='$AP' AND AM='$AM' AND PERFIL='$PERFIL' AND TIPO_RES='$TIPORES' AND IDDOM=$IDDOM ")
                if (rs.next()) {
                    IDRESIDENTE = rs.getInt("IDRESIDENTE").toString()
                }
                val rs2: Int = stm.executeUpdate("DELETE FROM SP_RESIDENTE WHERE IDRESIDENTE = $IDRESIDENTE")
                if(rs2 > 0){
                    Toast.makeText(itemView.context, "Eliminando Residente...", Toast.LENGTH_LONG).show()
                    val intent = Intent(itemView.context, Check_in::class.java)
                    itemView.context.startActivity(intent)
                }
                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()
            } catch (e: java.lang.Exception) {
            }
        }
    }

    inner class DataFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Residentes>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(originalData)
            } else {
                val filterPattern = constraint.toString().trim()
                for (item in originalData) {
                    if (item.nombre.toLowerCase().contains(filterPattern) ||
                        item.numero_cel.toLowerCase().contains(filterPattern) ||
                        item.ap.toLowerCase().contains(filterPattern) ||
                        item.am.toLowerCase().contains(filterPattern) ||
                        item.perfil.toLowerCase().contains(filterPattern) ||
                        item.tipo_res.toLowerCase().contains(filterPattern)) {
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
            data.addAll(results?.values as ArrayList<Residentes>)
            notifyDataSetChanged()
        }
    }
}

