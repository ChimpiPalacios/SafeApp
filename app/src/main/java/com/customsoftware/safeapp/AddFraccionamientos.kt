package com.customsoftware.safeapp


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.*


class AddFraccionamientos: AppCompatActivity() {

    private lateinit var txtpais: TextView
    private lateinit var txtestado: TextView
    private lateinit var txtmunicipio: TextView
    private lateinit var txtfracc: TextView
    private lateinit var txtetapa: TextView
    private lateinit var btninsert: Button
    private lateinit var btnbackfracc: ImageButton
    /*
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)

     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.addfraccionamientos)

         txtpais = findViewById<TextView>(R.id.txtpais)
         txtestado = findViewById<TextView>(R.id.txtestado)
         txtmunicipio = findViewById<TextView>(R.id.txtmunicipio)
         txtfracc = findViewById<TextView>(R.id.txtfracc)
         txtetapa = findViewById<TextView>(R.id.txtetapa)
         btninsert = findViewById<Button>(R.id.btninsert)
        btnbackfracc= findViewById<ImageButton>(R.id.btnbackfracc)

        btninsert.setOnClickListener(){
            insertar()
        }

        btnbackfracc.setOnClickListener {
            val Intent = Intent(this,Menu_Fraccionamientos::class.java)
            startActivity(Intent)
        }
        /*
        txtfracc.setOnClickListener{
            DatePickerDialog(this, this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        */

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

    private fun insertar() {
        var PAIS : String = txtpais.text.toString().trim();
        var ESTADO : String = txtestado.text.toString().trim();
        var MUNICIPIO : String = txtmunicipio.text.toString().trim();
        var FRACCIONAMIENTO : String = txtfracc.text.toString().trim();
        var ETAPA : String = txtetapa.text.toString().trim();

        if(PAIS.isEmpty()){
            txtpais.setError("El campo Pais es necesario")
        }else if(ESTADO.isEmpty()){
            txtestado.setError("El campo Estado es necesario")
        }else if(MUNICIPIO.isEmpty()){
            txtmunicipio.setError("El campo Municipio es necesario")
        }else if(FRACCIONAMIENTO.isEmpty()){
            txtfracc.setError("El campo Fraccionamiento es necesario")
        }else if(ETAPA.isEmpty()){
            txtetapa.setError("El campo Etapa es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: Int = stm.executeUpdate("INSERT INTO SP_FRACCIONAMIENTO (PAIS, ESTADO, MUNICIPIO, FRACCIONAMIENTO, ETAPA) VALUES ('"+PAIS+"','"+ESTADO+"','"+MUNICIPIO+"','"+FRACCIONAMIENTO+"','"+ETAPA+"')")
                if (rs>0) {
                    Toast.makeText(this, "INSERTADO CORRECTAMENTE", Toast.LENGTH_LONG).show()
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }
/*
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.e("Calendar","$year -- $month -- $dayOfMonth")
        calendar.set(year, month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun displayFormattedDate (timestamp: Long){
        findViewById<TextView>(R.id.txtfracc).text = formatter.format(timestamp)
        Log.i("Fomatting", timestamp.toString())
    }

 */
/*




    private fun select() {

        var FRACCIONAMIENTO : String = txtfracc.text.toString().trim();

        if(FRACCIONAMIENTO.isEmpty()){
            txtfracc.setError("El campo Fraccionamiento es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_FRACCIONAMIENTO")
                if (!rs.isBeforeFirst()){
                    Toast.makeText(applicationContext, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
                    return
                }
                val list = rs.use {
                    generateSequence {
                        if (rs.next()) rs.getInt(1) else null
                    }.toList()  // must be inside the use() block
                }


// if not, later remember to resultSet.close()

            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }
  */

}

