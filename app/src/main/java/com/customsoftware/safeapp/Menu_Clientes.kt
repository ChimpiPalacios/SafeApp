package com.customsoftware.safeapp

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.adapters.AdapterCliente
import com.customsoftware.safeapp.databinding.MenuClientesBinding

import com.customsoftware.safeapp.modelos.Clientes
import java.sql.*


class Menu_Clientes: AppCompatActivity(),AdapterCliente.ClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_clientes)

        val addClientesBtn = findViewById<ImageButton>(R.id.addClientesBtn)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerVClientes)
        val btn_back = findViewById<ImageButton>(R.id.backClientBtn)

        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_CLIENTE")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }

        var clientes=ArrayList<Clientes>()

        while (rs.next()){
            var NOMBRE : String = rs.getString("NOMBRE")
            var EMPRESA : String = rs.getString("EMPRESA")
            var NUMEROCEL : String = rs.getString("NUMEROCEL")
            /*revisar conversion de imagen*/
            var LOGO : String = ""
            var COLOR : String = rs.getString("COLOR")

            clientes.add(Clientes(NOMBRE,"cliente1.png",EMPRESA,NUMEROCEL,LOGO,COLOR))

        }


        val adapter = AdapterCliente(clientes,this)
        recyclerView.adapter = adapter


        btn_back.setOnClickListener{
            val Intent = Intent(this,Menu::class.java)
            startActivity(Intent)
        }

        addClientesBtn.setOnClickListener{
            val Intent = Intent(this,AddClientes::class.java)
            startActivity(Intent)
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
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
        return cnn
    }

    override fun clickedItem(cliente: Clientes) {
        Intent(this,UpdateClientes::class.java).putExtra("nombre",cliente.nombre)
        Intent(this,UpdateClientes::class.java).putExtra("empresa",cliente.empresa)
        Intent(this,UpdateClientes::class.java).putExtra("numerocel",cliente.numerocel)
        Intent(this,UpdateClientes::class.java).putExtra("color",cliente.color)
        Intent(this,UpdateClientes::class.java).putExtra("logo",cliente.logo)
        startActivity(Intent(this,UpdateClientes::class.java))

    }

}