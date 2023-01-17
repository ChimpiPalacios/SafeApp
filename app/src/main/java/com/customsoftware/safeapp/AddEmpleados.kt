package com.customsoftware.safeapp


import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.customsoftware.safeapp.R.layout.add_empleados
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement


class AddEmpleados: AppCompatActivity() {

    private lateinit var txtnombreemp: TextView
    private lateinit var txtap: TextView
    private lateinit var txtam: TextView
    private lateinit var txtbdt: TextView
    private lateinit var txtturno: TextView
    private lateinit var txtidusuario: TextView
    private lateinit var btninsertemp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_empleados)

        txtnombreemp = findViewById<TextView>(R.id.txtnombreemp)
        txtap = findViewById<TextView>(R.id.txtap)
        txtam = findViewById<TextView>(R.id.txtam)
        txtbdt = findViewById<TextView>(R.id.txtbdt)
        txtturno = findViewById<TextView>(R.id.txtturno)
        txtidusuario = findViewById<TextView>(R.id.txtidusuario)
        btninsertemp = findViewById<Button>(R.id.btninsertemp)

        btninsertemp.setOnClickListener(){
            insertar()
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
        var NOMBRE : String = txtnombreemp.text.toString().trim();
        var AP : String = txtap.text.toString().trim();
        var AM : String = txtam.text.toString().trim();
        var BDT : String = txtbdt.text.toString().trim();
        var TURNO : String = txtturno.text.toString().trim();
        var IDUSUARIO : String = txtidusuario.text.toString().trim();

        if(NOMBRE.isEmpty()){
            txtnombreemp.setError("El campo Nombre es necesario")
        }else if(AP.isEmpty()){
            txtap.setError("El campo Apellido paterno es necesario")
        }else if(AM.isEmpty()){
            txtam.setError("El campo Apellido materno es necesario")
        }else if(BDT.isEmpty()){
            txtbdt.setError("El campo Fecha de Cumpleaños es necesario")
        }else if(TURNO.isEmpty()){
            txtturno.setError("El campo Turno es necesario")
        }else if(IDUSUARIO.isEmpty()){
            txtidusuario.setError("El campo ID Empleado es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("INSERT INTO SP_EMPLEADO (NOMBRE,AP,AM,BDT,TURNO,IDUSUARIO) VALUES ('$NOMBRE','$AP','$AM','$BDT','$TURNO','$IDUSUARIO'")
                if (rs.next()) {
                    Toast.makeText(applicationContext, rs.getString(1), Toast.LENGTH_SHORT).show()
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun actualizar() {
        var NOMBRE : String = txtnombreemp.text.toString().trim();
        var AP : String = txtap.text.toString().trim();
        var AM : String = txtam.text.toString().trim();
        var BDT : String = txtbdt.text.toString().trim();
        var TURNO : String = txtturno.text.toString().trim();
        var IDUSUARIO : String = txtidusuario.text.toString().trim();

        if(NOMBRE.isEmpty()){
            txtnombreemp.setError("El campo es necesario")
        }else if(AP.isEmpty()){
            txtap.setError("El campo es necesario")
        }else if(AM.isEmpty()){
            txtam.setError("El campo es necesario")
        }else if(BDT.isEmpty()){
            txtbdt.setError("El campo es necesario")
        }else if(TURNO.isEmpty()){
            txtturno.setError("El campo es necesario")
        }else if(IDUSUARIO.isEmpty()){
            txtidusuario.setError("El campo es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("UPDATE  SP_EMPLEADO SET NOMBRE = '$NOMBRE', AP = '$AP', AM = '$AM',BDT = '$BDT',TURNO='$TURNO',IDUSUARIO='$IDUSUARIO' WHERE NOMBRE = '$NOMBRE' ")
                if (rs.next()) {
                    Toast.makeText(applicationContext, rs.getString(1), Toast.LENGTH_SHORT).show()
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun eliminar() {
        var NOMBRE : String = txtnombreemp.text.toString().trim();
        if(NOMBRE.isEmpty()){
            txtnombreemp.setError("El campo es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("DELETE FROM SP_EMPLEADO WHERE NOMBRE = '$NOMBRE' ")
                if (rs.next()) {
                    Toast.makeText(applicationContext, rs.getString(1), Toast.LENGTH_SHORT).show()
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

