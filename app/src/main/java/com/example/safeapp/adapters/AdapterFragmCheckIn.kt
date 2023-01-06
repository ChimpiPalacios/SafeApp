package com.example.safeapp.adapters

import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safeapp.R
import com.example.safeapp.modelos.CheckIns
import com.example.safeapp.modelos.Clientes
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement


class AdapterFragmCheckIn(val data: ArrayList<CheckIns>, clicklistener: ClickListener) : RecyclerView.Adapter<AdapterFragmCheckIn.MyViewHolder>() {
    var position= -1
    private var AdapterCheckInList : List<CheckIns> = arrayListOf()
    private var clickListener: ClickListener = clicklistener

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
        var hora = checkins.h_entrada

        holder.nombre.text = nombre
        holder.hora.text = hora.toString()

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(checkins)
        }


    }

    interface ClickListener{
        fun clickedItem(checkIns: CheckIns)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var nombre = itemView.findViewById<TextView>(R.id.item_nombre_checkin)
        var hora = itemView.findViewById<TextView>(R.id.item_hora_checkin)



        fun bindItems(data: CheckIns) {
            val txtNombre = itemView.findViewById<TextView>(R.id.item_nombre_checkin)
            val txtHora = itemView.findViewById<TextView>(R.id.item_hora_checkin)
            val btnRvCheckInDelete = itemView.findViewById<Button>(R.id.btnRvCheckInDelete)
            var CHECKIN : String = data.idcheck.toString()
            txtNombre.text = data.nombre
            txtHora.text = data.h_entrada.toString()

            btnRvCheckInDelete.setOnClickListener{
                eliminar(CHECKIN)
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

        private fun eliminar(CHECKIN: String) {
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: Int = stm.executeUpdate("DELETE FROM SP_CHECKIN WHERE IDCHECK = $CHECKIN")
            } catch (e: java.lang.Exception) {

            }
        }
    }

}

