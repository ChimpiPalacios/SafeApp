package com.customsoftware.safeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.R
import com.customsoftware.safeapp.adapters.AdapterFraccionamiento
import com.customsoftware.safeapp.adapters.AdarterReportes
import com.customsoftware.safeapp.modelos.Fraccionamientos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement


class Menu_Reportes: AppCompatActivity() {

    private lateinit var search_fracc_report : SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.menu_reportes)

        search_fracc_report = findViewById(R.id.search_fracc_report)
        val btn_back = findViewById<ImageButton>(R.id.backRepBtn)
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

        val adapter = AdarterReportes(fraccionamientos,this,Menu_Reportes.MyClickListener())
        recyclerView.adapter = adapter
        adapter.updateList(fraccionamientos)
        adapter.getFilter().filter("")

        search_fracc_report.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    adapter.updateList(adapter.getOriginalData())
                } else {
                    adapter.getFilter().filter(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    adapter.updateList(adapter.getOriginalData())
                } else {
                    adapter.getFilter().filter(newText)
                }
                return true
            }
        })



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

    class MyClickListener : AdarterReportes.ClickListener {

        override fun clickedItem(Fraccionamientos: Fraccionamientos,context: Context) {





            var PAIS : String = Fraccionamientos.pais
            var ESTADO : String = Fraccionamientos.estado
            var MUNICIPIO : String = Fraccionamientos.municipio
            var FRACCIONAMIENTO : String = Fraccionamientos.nombre
            var ETAPA : String = Fraccionamientos.etapa
            var ID_FRACCIONAMIENTO : Int = 0

            // Perform the task in the background and update the UI on the main thread
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    // Perform background task here
                    val stm: Statement = conexionDB()!!.createStatement()
                    val rs: ResultSet = stm.executeQuery("SELECT ID_FRACCIONAMIENTO FROM SP_FRACCIONAMIENTO WHERE PAIS='$PAIS' AND ESTADO='$ESTADO' AND MUNICIPIO='$MUNICIPIO' AND FRACCIONAMIENTO='$FRACCIONAMIENTO' AND ETAPA='$ETAPA'")
                    if (!rs.isBeforeFirst()){
                        Toast.makeText(context, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
                        return@withContext
                    }
                    while (rs.next()) {
                        ID_FRACCIONAMIENTO = rs.getString("ID_FRACCIONAMIENTO").toInt()

                    }
                    rs.close()
                    // ...
                }

                // Update the UI

                val sharedPref = context.getSharedPreferences("id_data", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putInt("ID", ID_FRACCIONAMIENTO)
                editor.commit()

                val intent = Intent(context, ShowReports::class.java)
                context.startActivity(intent)

            }
        }
    }

}