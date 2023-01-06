package com.example.safeapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.sql.*
import java.text.SimpleDateFormat
import java.util.*

class AddCheckIn : AppCompatActivity() {

    private lateinit var txtnombrecheckin: TextView
    private lateinit var txtvehiculocheckin: TextView
    private lateinit var txtplacacheckin: TextView
    private lateinit var txtcredencialcheckin: TextView
    private lateinit var txthoracheckin: TextView
    private lateinit var btninsertcheckin: Button
    private lateinit var btnbackcheckin: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_check_in)
        txtnombrecheckin = findViewById<TextView>(R.id.txtnombrecheckin)
        txtvehiculocheckin = findViewById<TextView>(R.id.txtvehiculocheckin)
        txtplacacheckin = findViewById<TextView>(R.id.txtplacacheckin)
        txtcredencialcheckin = findViewById<TextView>(R.id.txtcredencialcheckin)
        txthoracheckin = findViewById<TextView>(R.id.txthoracheckin)
        btninsertcheckin = findViewById<Button>(R.id.btninsertcheckin)
        btnbackcheckin = findViewById<ImageButton>(R.id.btnbackcheckin)

        btninsertcheckin.setOnClickListener{
            insertar()
        }

        btnbackcheckin.setOnClickListener {
            val Intent = Intent(this,Check_in::class.java)
            startActivity(Intent)
        }

        txthoracheckin.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "MM/dd/yy" // format of your choice
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                txthoracheckin.setText(sdf.format(cal.time))
            }
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
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
        var NOMBRE : String = txtnombrecheckin.text.toString().trim();
        var VEHICULO : String = txtvehiculocheckin.text.toString().trim();
        var PLACA : String = txtplacacheckin.text.toString().trim();
        var CREDENCIAL : String = txtcredencialcheckin.text.toString().trim();
        var HORA : String = txthoracheckin.text.toString().trim();


        if(NOMBRE.isEmpty()){
            txtnombrecheckin.setError("El campo Nombre es necesario")
        }else if(VEHICULO.isEmpty()){
            txtvehiculocheckin.setError("El campo Vehiculo es necesario")
        }else if(PLACA.isEmpty()){
            txtplacacheckin.setError("El campo Placa es necesario")
        }else if(CREDENCIAL.isEmpty()){
            txtcredencialcheckin.setError("El campo Credencial es necesario")
        }else if(HORA.isEmpty()){
            txthoracheckin.setError("El campo Hora es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()

                val affectedRows: Int = stm.executeUpdate("INSERT INTO SP_CHECKIN (NOMBRE,VEHICULO,PLACA,CREDENCIAL,HORA,IDRESID,IDUSU) VALUES ('$NOMBRE','$VEHICULO','$PLACA','$CREDENCIAL','$HORA',1,1)")
                if (affectedRows > 0) {
                    Toast.makeText(applicationContext,"INSERTADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }
}