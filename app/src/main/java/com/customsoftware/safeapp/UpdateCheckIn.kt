package com.customsoftware.safeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class UpdateCheckIn : AppCompatActivity() {

    private lateinit var txtupcheckin : TextView
    private lateinit var txtupvehiculo: TextView
    private lateinit var txtupplaca: TextView
    private lateinit var txtupcredencial: TextView
    private lateinit var txtuphora: TextView
    private lateinit var btnupcheckininsert: Button
    private lateinit var btnbackupcheckin: ImageButton

    private lateinit var textupvehiculo: TextView
    private lateinit var textupplaca: TextView
    private lateinit var textupcredencial: TextView
    private lateinit var textuphora: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_check_in)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        btnbackupcheckin.setOnClickListener {
            val Intent = Intent(this,Menu_Fraccionamientos::class.java)
            startActivity(Intent)
        }

        btnupcheckininsert.setOnClickListener(){
            actualizar()
        }
    }
    private fun initData(){
        txtupcheckin = findViewById(R.id.txtupcheckin)
        txtupvehiculo = findViewById(R.id.txtupvehiculo)
        txtupplaca = findViewById(R.id.txtupplaca)
        txtupcredencial = findViewById(R.id.txtupcredencial)
        txtuphora = findViewById(R.id.txtuphora)
        btnupcheckininsert = findViewById(R.id.btnupcheckininsert)
        btnbackupcheckin = findViewById(R.id.btnbackupcheckin)

        textupvehiculo = findViewById(R.id.textupvehiculo)
        textupplaca = findViewById(R.id.textupplaca)
        textupcredencial = findViewById(R.id.textupcredencial)
        textuphora = findViewById(R.id.textuphora)



        getData()
    }

    private fun getData(){
        val intent = intent.extras
        var nombre = intent!!.getString("nombre")
        var vehiculo = intent!!.getString("vehiculo")
        var placa = intent!!.getString("placa")
        var credencial = intent!!.getString("credencial")
        var hora = intent!!.getString("hora")

        txtupcheckin.text = nombre
        textupvehiculo.text = vehiculo
        textupplaca.text = placa
        textupcredencial.text = credencial
        textuphora.text = hora

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
        var NOMBRE : String = txtupcheckin.text.toString().trim();
        var VEHICULO : String = txtupvehiculo.text.toString().trim();
        var PLACA : String = txtupplaca.text.toString().trim();
        var CREDENCAL : String = txtupcredencial.text.toString().trim();
        var HORA : String = txtuphora.text.toString().trim();

        if(NOMBRE.isEmpty()){
            txtupcheckin.setError("El campo Nombre es necesario")
        }else if(VEHICULO.isEmpty()){
            txtupvehiculo.setError("El campo Vehiculo es necesario")
        }else if(PLACA.isEmpty()){
            txtupplaca.setError("El campo Placa es necesario")
        }else if(CREDENCAL.isEmpty()){
            txtupcredencial.setError("El campo Credencial es necesario")
        }else if(HORA.isEmpty()){
            txtuphora.setError("El campo Hora es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: Int = stm.executeUpdate("UPDATE  SP_CHECKIN SET VEHICULO = '$VEHICULO', PLACA = '$PLACA',CREDENCAL='$CREDENCAL',HORA= '$HORA' WHERE NOMBRE = '$NOMBRE' ")
                if (rs >0) {
                    Toast.makeText(this, "ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }


}