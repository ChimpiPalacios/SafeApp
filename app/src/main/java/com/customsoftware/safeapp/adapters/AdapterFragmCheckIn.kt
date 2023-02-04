package com.customsoftware.safeapp.adapters

import android.content.Context
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.R
import com.customsoftware.safeapp.modelos.CheckIns
import kotlinx.android.synthetic.main.items_checkin_rv.view.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdapterFragmCheckIn(var data: ArrayList<CheckIns>,val context: Context, clicklistener: ClickListener) : RecyclerView.Adapter<AdapterFragmCheckIn.MyViewHolder>(),
Filterable{

    var position= -1
    private var originalData = ArrayList<CheckIns>()
    private var clickListener: ClickListener = clicklistener
    private var dataFilter = DataFilter()
    private var AdapterCheckInList : List<CheckIns> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_checkin_rv, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        this.position = position
        holder.bindItems(data[position])



        var checkins = data[position]
        var nombre =  checkins.nombre
        var vehiculo = checkins.vehiculo
        var nombreresi = checkins.nombreresi

        val simpleHourFormat = SimpleDateFormat("HH:mm")
        val formattedHora = simpleHourFormat.format(checkins.hora)
        var hora = formattedHora

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val fechaString = simpleDateFormat.format(checkins.hora)
        var fecha = fechaString

        var idresid = checkins.idresid

        // Retrieve CALLE and NUMERO using idresid
        var calle = ""
        var numero = ""
        var IDDOM = ""
        val cnn = conexionDB()
        if (cnn != null) {
            try {
                val stmt = cnn.createStatement()
                val rs = stmt.executeQuery("SELECT IDDOM FROM SP_RESIDENTE WHERE IDRESIDENTE=$idresid")
                if (rs.next()) {
                    IDDOM = rs.getInt("IDDOM").toString()
                }
                val rs2 = stmt.executeQuery("SELECT CALLE,NUMERO FROM SP_DOMICILIO WHERE IDDOM=$IDDOM")
                if (rs2.next()) {
                    calle = rs2.getString("CALLE")
                    numero = rs2.getString("NUMERO")
                }
                rs.close()
                rs2.close()
                stmt.close()
            } catch (ex: SQLException) {
                ex.printStackTrace()
            } finally {
                try {
                    cnn.close()
                } catch (ex: SQLException) {
                    ex.printStackTrace()
                }
            }
        }

        holder.nombre.text = nombre
        holder.vehiculo.text = vehiculo
        holder.nombreresi.text = nombreresi
        holder.hora.text = hora
        holder.fecha.text = fecha
        holder.calle.text = calle
        holder.numero.text = numero

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(checkins,context)
        }
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nombre = itemView.findViewById<TextView>(R.id.item_nombre_checkin)
        var vehiculo = itemView.findViewById<TextView>(R.id.item_vehiculo_checkin)
        var nombreresi = itemView.findViewById<TextView>(R.id.item_visitante_checkin)
        var hora = itemView.findViewById<TextView>(R.id.item_hora_checkin)
        var fecha = itemView.findViewById<TextView>(R.id.item_fecha_checkin)
        var calle = itemView.findViewById<TextView>(R.id.item_calle_checkin)
        var numero = itemView.findViewById<TextView>(R.id.item_numero_checkin)

        fun bindItems(data: CheckIns) {
            val txtNombre = itemView.findViewById<TextView>(R.id.item_nombre_checkin)
            val txtVehiculo = itemView.findViewById<TextView>(R.id.item_vehiculo_checkin)
            val txtNombreResi = itemView.findViewById<TextView>(R.id.item_visitante_checkin)
            val txtHora = itemView.findViewById<TextView>(R.id.item_hora_checkin)
            val txtFecha = itemView.findViewById<TextView>(R.id.item_fecha_checkin)
            val txtCalle = itemView.findViewById<TextView>(R.id.item_calle_checkin)
            val txtNumero = itemView.findViewById<TextView>(R.id.item_numero_checkin)

            txtNombre.text = data.nombre
            txtVehiculo.text = data.vehiculo
            txtNombreResi.text = data.nombreresi
            txtFecha.text = data.hora.date.toString()
            txtHora.text = data.hora.time.toString()
            var IDDOM: String= ""

            val cnn = conexionDB()
            if (cnn != null) {
                try {
                    val query = "SELECT IDDOM FROM SP_RESIDENTE WHERE IDRESIDENTE =" + data.idresid
                    val rs = cnn.createStatement().executeQuery(query)
                    if (rs.next()) {
                        IDDOM = rs.getInt("IDDOM").toString()
                    }
                    val query2 = "SELECT CALLE,NUMERO FROM SP_DOMICILIO WHERE IDDOM =" + IDDOM
                    val rs2 = cnn.createStatement().executeQuery(query2)
                    if (rs2.next()) {
                        txtCalle.text = rs2.getString("CALLE")
                        txtNumero.text = rs2.getString("NUMERO")
                    }
                    rs.close()
                    rs2.close()
                    cnn.close()
                } catch (ex: SQLException) {
                    ex.printStackTrace()
                } finally {
                    try {
                        cnn.close()
                    } catch (ex: SQLException) {
                        ex.printStackTrace()
                    }
                }
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
    }

    fun updateList(newList: ArrayList<CheckIns>) {
        originalData = ArrayList(newList)
        data = ArrayList(newList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return dataFilter
    }

    fun getOriginalData(): ArrayList<CheckIns> {
        return originalData
    }

    interface ClickListener{
        fun clickedItem(checkIns: CheckIns, context: Context)
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
            }
            return cnn
        }
    }


    inner class DataFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<CheckIns>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(originalData)
            } else {
                val filterPattern = constraint.toString().trim()
                for (item in originalData) {
                    if (item.nombre.toLowerCase().contains(filterPattern) ||
                        item.vehiculo.toLowerCase().contains(filterPattern) ||
                        item.nombreresi.toLowerCase().contains(filterPattern) ||
                        item.hora.time.toString().toLowerCase().contains(filterPattern) ||
                        item.hora.date.toString().toLowerCase().contains(filterPattern)) {
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
            data.addAll(results?.values as ArrayList<CheckIns>)
            notifyDataSetChanged()
        }
    }

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
        }
        return cnn
    }


}

