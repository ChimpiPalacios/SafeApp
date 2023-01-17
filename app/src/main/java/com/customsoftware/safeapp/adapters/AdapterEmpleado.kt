package com.customsoftware.safeapp.adapters

import android.content.Context
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safeapp.R
import com.example.safeapp.modelos.Empleados
import com.example.safeapp.modelos.Fraccionamientos
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class AdapterEmpleado (val empleados:ArrayList<Empleados>, clickListener: ClickListener): RecyclerView.Adapter<AdapterEmpleado.ViewHolder>() {
    var position= -1

    private var EmpleadosList: List<Empleados> = arrayListOf()
    private lateinit var context: Context
    private var clickListener: AdapterEmpleado.ClickListener = clickListener

    public fun setData(empleados: List<Empleados>){
        this.EmpleadosList = empleados
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AdapterEmpleado.ViewHolder {
        var v= LayoutInflater.from(parent.context).inflate(R.layout.items_empleados_rv,parent, false)
        return ViewHolder(v)

    }
    override fun getItemCount(): Int {
        return empleados.size
    }

    override fun onBindViewHolder(holder: AdapterEmpleado.ViewHolder, position: Int) {
        this.position = position
        holder.bindItems(empleados[position])


        var empleadatiion = empleados[position]
        var nombre = empleadatiion.nombre
        var ap = empleadatiion.ap
        var am = empleadatiion.am
        var birthday = empleadatiion.birthday
        var turno = empleadatiion.turno
        var id_usuario = empleadatiion.ID_USUARIO

        holder.nombre.text = nombre
        holder.ap.text = ap
        holder.am.text = am
        holder.birthday.text = birthday.toString()
        holder.turno.text = turno
        holder.id_usuario.text = id_usuario.toString()



        holder.itemView.setOnClickListener {
            clickListener.clickedItem(empleadatiion)
        }
    }
    interface ClickListener{
        fun clickedItem(empleados: Empleados)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nombre = itemView.findViewById<TextView>(R.id.item_nombre)
        var ap = itemView.findViewById<TextView>(R.id.item_ap)
        var am = itemView.findViewById<TextView>(R.id.item_am)
        var birthday = itemView.findViewById<TextView>(R.id.item_birthday)
        var turno = itemView.findViewById<TextView>(R.id.item_turno)
        var id_usuario = itemView.findViewById<TextView>(R.id.item_id_usuario)


        fun bindItems(empleados: Empleados){

            val txtEmp = itemView.findViewById<TextView>(R.id.item_nombre)
            val txtAp = itemView.findViewById<TextView>(R.id.item_ap)
            val txtAm = itemView.findViewById<TextView>(R.id.item_am)
            val txtBd = itemView.findViewById<TextView>(R.id.item_birthday)
            val txtTurno = itemView.findViewById<TextView>(R.id.item_turno)
            val txtIdUs = itemView.findViewById<TextView>(R.id.item_id_usuario)
            val btnRvDeleteEmp = itemView.findViewById<Button>(R.id.btnRvDeleteEmp)
            var EMPLEADO : String = empleados.nombre


            txtEmp.text=empleados.nombre
            txtAp.text=empleados.ap
            txtAm.text=empleados.am
            txtBd.text=empleados.birthday.toString()
            txtTurno.text=empleados.turno
            txtIdUs.text=empleados.ID_USUARIO.toString()


            btnRvDeleteEmp.setOnClickListener { eliminar(EMPLEADO) }


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

        private fun eliminar(EMPLEADO: String) {
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: Int = stm.executeUpdate("DELETE FROM SP_EMPLEADOS WHERE NOMBRE = '$EMPLEADO'")
            } catch (e: java.lang.Exception) {

            }
        }




    }



}


