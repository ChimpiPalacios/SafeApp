package com.customsoftware.safeapp.adapters

import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.CallesFragment
import com.customsoftware.safeapp.R
import com.customsoftware.safeapp.modelos.Domicilios

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.*
import kotlin.collections.ArrayList

class AdapterFragmentDomicilio(var data: ArrayList<Domicilios>,val context: Context, clicklistener: ClickListener) : RecyclerView.Adapter<AdapterFragmentDomicilio.MyViewHolder>(),
    Filterable {

    var position= -1
    private var originalData = ArrayList<Domicilios>()
    private var clickListener: ClickListener = clicklistener
    private var dataFilter = DataFilter()
    private var AdapterDomicilioList : List<Domicilios> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_domicilios_rv, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        this.position = position
        holder.bindItems(data[position])

        var domicilios = data[position]
        var calle =  domicilios.calle
        var numero = domicilios.numero

        holder.calle.text = calle
        holder.numero.text = numero.toString()

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(domicilios,context)
        }
    }

    fun updateList(newList: ArrayList<Domicilios>) {
        originalData = ArrayList(newList)
        data = ArrayList(newList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return dataFilter
    }

    fun getOriginalData(): ArrayList<Domicilios> {
        return originalData
    }

    interface ClickListener{
        fun clickedItem(domicilios: Domicilios, context: Context)
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

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var calle = itemView.findViewById<TextView>(R.id.item_calle_domicilio)
        var numero = itemView.findViewById<TextView>(R.id.item_numero_domicilio)

        fun bindItems(data: Domicilios) {
            val txtCalle = itemView.findViewById<TextView>(R.id.item_calle_domicilio)
            val txtNumero = itemView.findViewById<TextView>(R.id.item_numero_domicilio)
            val btnRvDomicilioDelete = itemView.findViewById<Button>(R.id.btnRvDomicilioDelete)
            var DOMICILIO : String = data.idfracc.toString()
            txtCalle.text = data.calle
            txtNumero.text = data.numero.toString()

            btnRvDomicilioDelete.setOnClickListener{
                eliminar(DOMICILIO)
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
        private fun eliminar(DOMICILIO: String) {
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                stm.executeUpdate("DELETE FROM SP_DOMICILIO WHERE IDFRACC = $DOMICILIO")
                Toast.makeText(itemView.context, "Domicilio eliminado", Toast.LENGTH_SHORT).show()
                val intent = Intent(itemView.context, CallesFragment::class.java)
                itemView.context.startActivity(intent)
            } catch (e: java.lang.Exception) {
                Toast.makeText(itemView.context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class DataFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList = ArrayList<Domicilios>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(originalData)
            } else {
                val filterPattern = constraint.toString().trim()
                for (item in originalData) {
                    if (item.calle.toLowerCase().contains(filterPattern) || item.numero.toLowerCase().contains(filterPattern)) {
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
            data.addAll(results?.values as ArrayList<Domicilios>)
            notifyDataSetChanged()
        }
    }

}

