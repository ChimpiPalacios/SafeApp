package com.customsoftware.safeapp

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.R
import com.customsoftware.safeapp.adapters.AdapterFraccionamiento
import com.customsoftware.safeapp.adapters.AdarterReportes
import com.customsoftware.safeapp.modelos.Fraccionamientos
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement


class Menu_Reportes: AppCompatActivity(), AdarterReportes.ClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_reportes)

        val btn_back = findViewById<ImageButton>(R.id.backRepBtn)
        val addReportBtn = findViewById<ImageButton>(R.id.addReportBtn)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerVReports)

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

        val adapter = AdarterReportes(fraccionamientos,this)
        recyclerView.adapter = adapter



        addReportBtn.setOnClickListener{
            val Intent = Intent(this,AddFraccionamientos::class.java)
            startActivity(Intent)
        }
        btn_back.setOnClickListener{
            val Intent = Intent(this,Menu::class.java)
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