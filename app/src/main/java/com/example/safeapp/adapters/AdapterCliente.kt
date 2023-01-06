package com.example.safeapp.adapters

import android.content.Context
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.safeapp.R
import com.example.safeapp.modelos.Clientes
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement



class AdapterCliente (val clientes:ArrayList<Clientes>, clickListener: AdapterCliente.ClickListener):RecyclerView.Adapter<AdapterCliente.ViewHolder>(){
    var position= -1

    private var ClientesList: List<Clientes> = arrayListOf()
    private lateinit var context: Context
    private var clickListener: ClickListener = clickListener



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AdapterCliente.ViewHolder {
        var v= LayoutInflater.from(parent.context).inflate(R.layout.items_clientes_rv,parent, false)
      return ViewHolder(v)

    }
    override fun getItemCount(): Int {
        return clientes.size
    }

    override fun onBindViewHolder(holder: AdapterCliente.ViewHolder, position:Int) {
        this.position = position
        holder.bindItems(clientes[position])

        var clientes = clientes[position]
        var nombre = clientes.nombre
        var empresa = clientes.empresa
        var numerocel = clientes.numerocel
        var logo = clientes.logo
        var color = clientes.color

        holder.nombre.text =  nombre
        holder.empresa.text =  empresa
        holder.numerocel.text =  numerocel



        holder.itemView.setOnClickListener {
            clickListener.clickedItem(clientes)
        }
    }

    interface ClickListener{
        fun clickedItem(cliente: Clientes)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var nombre = itemView.findViewById<TextView>(R.id.item_cliente)
        var empresa = itemView.findViewById<TextView>(R.id.item_empresa)
        var numerocel = itemView.findViewById<TextView>(R.id.item_numerocel)




        fun bindItems(cliente: Clientes){
            val txtCliente = itemView.findViewById<TextView>(R.id.item_cliente)
            val txtEmpresa = itemView.findViewById<TextView>(R.id.item_empresa)
            val txtNumerocel = itemView.findViewById<TextView>(R.id.item_numerocel)

            val btnRvClntDelete = itemView.findViewById<Button>(R.id.btnRvClntDelete)
            var CLIENTE : String = cliente.nombre

            txtCliente.text=cliente.nombre
            txtEmpresa.text=cliente.empresa
            txtNumerocel.text=cliente.numerocel


            btnRvClntDelete.setOnClickListener{
                eliminar(CLIENTE)
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
        private fun eliminar(CLIENTE: String) {
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: Int = stm.executeUpdate("DELETE FROM SP_CLIENTE WHERE CLIENTE = '$CLIENTE'")
            } catch (e: java.lang.Exception) {

            }
        }
    }

}