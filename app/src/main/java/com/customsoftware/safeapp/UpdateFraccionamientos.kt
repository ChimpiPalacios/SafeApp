package com.customsoftware.safeapp

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.adapters.AdapterFraccionamiento
import com.customsoftware.safeapp.modelos.Fraccionamientos
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class UpdateFraccionamientos: AppCompatActivity() {

    private lateinit var txtupfracc : TextView
    private lateinit var txtuppais: TextView
    private lateinit var txtupestado: TextView
    private lateinit var txtupmunicipio: TextView
    private lateinit var txtupetapa: TextView
    private lateinit var btnupinsert: Button
    private lateinit var btnbackupfracc: ImageButton

    private lateinit var textupestado: TextView
    private lateinit var textuppais: TextView
    private lateinit var textupmunicipio: TextView
    private lateinit var textupetapa: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updatefraccionamientos)
        initData()

        btnbackupfracc.setOnClickListener {
            val Intent = Intent(this,Menu_Fraccionamientos::class.java)
            startActivity(Intent)
        }

        btnupinsert.setOnClickListener(){
            actualizar()
        }
    }

    private fun initData(){
        txtupfracc = findViewById(R.id.txtupfracc)
        txtuppais = findViewById(R.id.txtuppais)
        txtupestado = findViewById(R.id.txtupestado)
        txtupmunicipio = findViewById(R.id.txtupmunicipio)
        txtupetapa = findViewById(R.id.txtupetapa)
        btnupinsert = findViewById(R.id.btnupinsert)
        btnbackupfracc = findViewById(R.id.btnbackupfracc)

        textupestado = findViewById(R.id.textupestado)
        textuppais = findViewById(R.id.textuppais)
        textupmunicipio = findViewById(R.id.textupmunicipio)
        textupetapa = findViewById(R.id.textupetapa)



        getData()
    }

    private fun getData(){
        val intent = intent.extras
        var fraccionamiento = intent!!.getString("fraccionamiento")
        var estado = intent!!.getString("estado")
        var etapa = intent!!.getString("etapa")
        var pais = intent!!.getString("pais")
        var municipio = intent!!.getString("municipio")

        txtupfracc.text = fraccionamiento
        textupestado.text = estado
        textupetapa.text = etapa
        textuppais.text = pais
        textupmunicipio.text = municipio

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

    private fun actualizar() {
        var PAIS : String = txtuppais.text.toString().trim();
        var ESTADO : String = txtupestado.text.toString().trim();
        var MUNICIPIO : String = txtupmunicipio.text.toString().trim();
        var FRACCIONAMIENTO : String = txtupfracc.text.toString().trim();
        var ETAPA : String = txtupetapa.text.toString().trim();

        if(PAIS.isEmpty()){
            txtuppais.setError("El campo Pais es necesario")
        }else if(ESTADO.isEmpty()){
            txtupestado.setError("El campo Estado es necesario")
        }else if(MUNICIPIO.isEmpty()){
            txtupmunicipio.setError("El campo Municipio es necesario")
        }else if(ETAPA.isEmpty()){
            txtupetapa.setError("El campo Etapa es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: Int = stm.executeUpdate("UPDATE  SP_FRACCIONAMIENTO SET PAIS = '$PAIS', ESTADO = '$ESTADO', MUNICIPIO = '$MUNICIPIO',ETAPA='$ETAPA' WHERE FRACCIONAMIENTO = '$FRACCIONAMIENTO' ")
                if (rs >0) {
                    Toast.makeText(this, "ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}