package com.example.safeapp

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class UpdateClientes: AppCompatActivity() {

    private lateinit var txtupclnt: TextView
    private lateinit var textupempresa: TextView
    private lateinit var textupnumcel: TextView
    private lateinit var textuplogo: TextView
    private lateinit var textupcolor: TextView
    private lateinit var btnupinsertclnt: Button
    private lateinit var btnbackupclnt: ImageButton

    private lateinit var txtupempresa: TextView
    private lateinit var txtupnumcel: TextView
    private lateinit var txtuplogo: TextView
    private lateinit var txtupcolor: TextView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updateclientes)
        initData()

        btnbackupclnt.setOnClickListener {
            val Intent = Intent(this, Menu_Clientes::class.java)
            startActivity(Intent)
        }

        btnupinsertclnt.setOnClickListener(){
            actualizar()
        }
    }

    private fun initData(){
        txtupclnt = findViewById(R.id.txtupclnt)
        textupempresa = findViewById(R.id.textupempresa)
        textupnumcel = findViewById(R.id.textupnumcel)
        textuplogo = findViewById(R.id.textuplogo)
        textupcolor = findViewById(R.id.textupcolor)

        btnupinsertclnt = findViewById(R.id.btnupinsertclnt)
        btnbackupclnt = findViewById(R.id.btnbackupclnt)

        txtupempresa = findViewById(R.id.txtupempresa)
        txtupnumcel = findViewById(R.id.txtupnumcel)
        txtuplogo = findViewById(R.id.txtuplogo)
        txtupcolor = findViewById(R.id.txtupcolor)

        getData()

    }

    private fun getData(){
        val intent = intent.extras
        var cliente = intent!!.getString("nombre")
        var empresa = intent!!.getString("empresa")
        var numerocel = intent!!.getString("numerocel")
        var color = intent!!.getString("color")
        var logo = intent!!.getString("logo")

        txtupclnt.text = cliente
        textupempresa.text = empresa
        textupnumcel.text = numerocel
        textupcolor.text = color
        textuplogo.text = logo

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
        var NOMBRE : String = txtupclnt.text.toString().trim();
        var EMPRESA : String = textupempresa.text.toString().trim();
        var NUMEROCEL : String = textupnumcel.text.toString().trim();
        var LOGO : String = textupcolor.text.toString().trim();
        var COLOR : String = textuplogo.text.toString().trim();

        if(NOMBRE.isEmpty()){
            txtupclnt.setError("El campo Nombre es necesario")
        }else if(EMPRESA.isEmpty()){
            textupempresa.setError("El campo Empresa es necesario")
        }else if(NUMEROCEL.isEmpty()){
            textupnumcel.setError("El campo Numero Cel es necesario")
        }else if(LOGO.isEmpty()){
            textupcolor.setError("El campo Logo es necesario")
        }else if(COLOR.isEmpty()){
            textuplogo.setError("El campo Color es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("UPDATE  SP_CLIENTE SET NOMBRE = '$NOMBRE', EMPRESA = '$EMPRESA', NUMEROCEL = '$NUMEROCEL',LOGO = '$LOGO',COLOR='$COLOR' WHERE NOMBRE = '$NOMBRE' ")
                if (rs.next()) {
                    Toast.makeText(applicationContext, rs.getString(1), Toast.LENGTH_SHORT).show()
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }







}