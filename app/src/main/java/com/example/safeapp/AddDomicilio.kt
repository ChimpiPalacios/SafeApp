package com.example.safeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class AddDomicilio : AppCompatActivity() {

    private lateinit var txtcalledom: TextView
    private lateinit var txtnombredom: TextView
    private lateinit var btninsertdom: Button
    private lateinit var btnbackdomicilio: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_domicilio)

        txtcalledom = findViewById<TextView>(R.id.txtcalledom)
        txtnombredom = findViewById<TextView>(R.id.txtnombredom)
        btninsertdom = findViewById<Button>(R.id.btninsertdom)
        btnbackdomicilio = findViewById<ImageButton>(R.id.btnbackdomicilio)

        btninsertdom.setOnClickListener{
            insertar()
        }

        btnbackdomicilio.setOnClickListener {
            val Intent = Intent(this,Check_in::class.java)
            startActivity(Intent)
        }

    }

    private fun conexionDB(): Connection? {
        var cnn: Connection? = null

        try {
            val politica = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(politica)
            Class.forName("org.gjt.mm.mysql.Driver").newInstance()
            //remotemysql.com:
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
    private fun insertar() {
        var CALLE : String = txtcalledom.text.toString().trim();
        var NUMERO : String = txtnombredom.text.toString().trim();



        if(CALLE.isEmpty()){
            txtcalledom.setError("El campo CALLE es necesario")
        }else if(NUMERO.isEmpty()){
            txtnombredom.setError("El campo NUMERO es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()

                val affectedRows: Int = stm.executeUpdate("INSERT INTO SP_DOMICILIO (CALLE,NUMERO,IDFRACC) VALUES ('$CALLE','$NUMERO',1)")
                if (affectedRows > 0) {
                    Toast.makeText(applicationContext,"INSERTADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }
}