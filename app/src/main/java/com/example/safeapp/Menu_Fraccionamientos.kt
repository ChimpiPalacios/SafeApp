package com.example.safeapp

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safeapp.adapters.AdapterFraccionamiento
import com.example.safeapp.modelos.Fraccionamientos
import com.google.android.material.tabs.TabLayout
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class Menu_Fraccionamientos: AppCompatActivity(), AdapterFraccionamiento.ClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_fraccionamiento)

        val btn_back = findViewById<ImageButton>(R.id.backFracBtn)
        val addFracBtn = findViewById<ImageButton>(R.id.addFracBtn)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerVFracc)
        /*se va a cambiar el tab layout de lugar al dar click en items_fracc_rv, mandar a llamr en AdapterFraccionamiento.kt*/
        /*verificar si git funciono*/
        val tabLayout = findViewById<TabLayout>(R.id.tabLayFracc)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_FRACCIONAMIENTO")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }

        var fraccionamientos=ArrayList<Fraccionamientos>()

        while (rs.next()){
            var PAIS : String = rs.getString("PAIS")
            var ESTADO : String = rs.getString("ESTADO")
            var MUNICIPIO : String = rs.getString("MUNICIPIO")
            var FRACCIONAMIENTO : String = rs.getString("FRACCIONAMIENTO")
            var ETAPA : String = rs.getString("ETAPA")

            fraccionamientos.add(Fraccionamientos(FRACCIONAMIENTO,"cliente1.png",PAIS,ESTADO,MUNICIPIO,ETAPA))

        }

        val adapter = AdapterFraccionamiento(fraccionamientos,this)
        recyclerView.adapter = adapter

        btn_back.setOnClickListener{
            val Intent = Intent(this,Menu::class.java)
            startActivity(Intent)
        }

        addFracBtn.setOnClickListener{
            val Intent = Intent(this,AddFraccionamientos::class.java)
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

    override fun clickedItem(Fraccionamientos: Fraccionamientos) {
        Intent(this,UpdateFraccionamientos::class.java).putExtra("fraccionamiento",Fraccionamientos.nombre)
        Intent(this,UpdateFraccionamientos::class.java).putExtra("estado",Fraccionamientos.estado)
        Intent(this,UpdateFraccionamientos::class.java).putExtra("etapa",Fraccionamientos.etapa)
        Intent(this,UpdateFraccionamientos::class.java).putExtra("pais",Fraccionamientos.pais)
        Intent(this,UpdateFraccionamientos::class.java).putExtra("municipio",Fraccionamientos.municipio)
        startActivity(Intent(this,UpdateFraccionamientos::class.java))
    }
}

