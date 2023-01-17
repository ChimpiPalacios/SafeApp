package com.customsoftware.safeapp.adapters

import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safeapp.R
import com.example.safeapp.modelos.Domicilios

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement



class AdapterFragmentDomicilio(val data: ArrayList<Domicilios>, clicklistener: ClickListener) : RecyclerView.Adapter<AdapterFragmentDomicilio.MyViewHolder>() {
    var position= -1
    private var AdapterDomicilioList : List<Domicilios> = arrayListOf()
    private var clickListener: ClickListener = clicklistener

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
            clickListener.clickedItem(domicilios)
        }


    }

    interface ClickListener{
        fun clickedItem(domicilios: Domicilios)
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
                val rs: Int = stm.executeUpdate("DELETE FROM SP_DOMICILIO WHERE IDDOMICILIO = $DOMICILIO")
            } catch (e: java.lang.Exception) {

            }
        }
    }

}

