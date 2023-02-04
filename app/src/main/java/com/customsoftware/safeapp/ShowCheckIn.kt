package com.customsoftware.safeapp

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.sql.*
import java.text.SimpleDateFormat
import java.sql.Blob
import java.sql.ResultSet
import java.sql.Statement
import java.sql.Timestamp

class ShowCheckIn : AppCompatActivity() {

    private lateinit var txt_fechahora_showcheckin : EditText
    private lateinit var txt_calle_showcheckin : EditText
    private lateinit var txt_numero_showcheckin : EditText
    private lateinit var txt_res_showcheckin : EditText
    private lateinit var txt_nombre_showcheckin : EditText
    private lateinit var txt_vehiculo_showcheckin : EditText
    private lateinit var imgcredencialshowcheckin : ImageView
    private lateinit var imgplacashowcheckin : ImageView

    private var IDCHECK: Int = 0
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scale = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.show_check_in)


        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        IDCHECK = sharedPref.getInt("IDCHECK", 0)

        initData()
    }

    private fun initData(){
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        txt_fechahora_showcheckin = findViewById(R.id.txt_fechahora_showcheckin)
        txt_calle_showcheckin = findViewById(R.id.txt_calle_showcheckin)
        txt_numero_showcheckin = findViewById(R.id.txt_numero_showcheckin)
        txt_res_showcheckin = findViewById(R.id.txt_res_showcheckin)
        txt_nombre_showcheckin = findViewById(R.id.txt_nombre_showcheckin)
        txt_vehiculo_showcheckin = findViewById(R.id.txt_vehiculo_showcheckin)

        imgcredencialshowcheckin = findViewById(R.id.imgcredencialshowcheckin)
        imgplacashowcheckin = findViewById(R.id.imgplacashowcheckin)

        getData()




    }

    private fun getData(){

        var CALLE: String =""
        var NUMERO: String=""
        var NOMBRE: String = ""
        var VEHICULO: String = ""
        var HORA: Timestamp = Timestamp.valueOf("2023-01-26 12:34:56")
        var IDRESID: Int = 0
        var NOMBRERESI: String = ""
        var placaBlob: Blob? = null
        var credencialBlob: Blob? = null

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_CHECKIN WHERE IDCHECK=$IDCHECK")
        if (!rs.isBeforeFirst()) {
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs.next()) {

            NOMBRE = rs.getString("NOMBRE")
            VEHICULO = rs.getString("VEHICULO")
            placaBlob = rs.getBlob("PLACA")
            credencialBlob = rs.getBlob("CREDENCIAL")
            HORA = rs.getTimestamp("HORA")
            IDRESID = rs.getInt("IDRESID")
            NOMBRERESI = rs.getString("NOMBRERESI")

        }

        val rs2: ResultSet = stm.executeQuery("SELECT * FROM SP_RESIDENTE LEFT JOIN SP_DOMICILIO ON SP_RESIDENTE.IDDOM = SP_DOMICILIO.IDDOM WHERE IDRESIDENTE=$IDRESID")
        if (!rs2.isBeforeFirst()) {
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs2.next()) {
            CALLE = rs2.getString("CALLE")
            NUMERO = rs2.getString("NUMERO")

        }
        rs.close()
        rs.close()
        stm.close()

        val imageBytesPlaca: ByteArray = placaBlob!!.getBytes(1, placaBlob.length().toInt())
        val optionsPlaca = BitmapFactory.Options()
        optionsPlaca.inSampleSize = 1
        val imageBitmapPlaca = BitmapFactory.decodeByteArray(imageBytesPlaca, 0, imageBytesPlaca.size, optionsPlaca)

        val imageBytesCred: ByteArray = credencialBlob!!.getBytes(1, credencialBlob.length().toInt())
        val optionsCred = BitmapFactory.Options()
        optionsCred.inSampleSize = 1
        val imageBitmapCred = BitmapFactory.decodeByteArray(imageBytesCred, 0, imageBytesCred.size, optionsCred)

        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val formattedHora = sdf.format(HORA)
        txt_fechahora_showcheckin.setText(formattedHora)
        txt_nombre_showcheckin.setText(NOMBRE)
        txt_vehiculo_showcheckin.setText(VEHICULO)
        imgplacashowcheckin.setImageBitmap(imageBitmapPlaca)
        imgcredencialshowcheckin.setImageBitmap(imageBitmapCred)
        txt_calle_showcheckin.setText(CALLE)
        txt_numero_showcheckin.setText(NUMERO)
        txt_res_showcheckin.setText(NOMBRERESI)

        /*
        imgcredencialshowcheckin.setOnClickListener{
            val intent = Intent(this, ImageZoomActivity::class.java)
            intent.putExtra("image", credencialBlob)
            startActivity(intent)
        }

        imgplacashowcheckin.setOnClickListener{
            val intent = Intent(this, ImageZoomActivity::class.java)
            intent.putExtra("image", placaBlob)
            startActivity(intent)
        }


        */



    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
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

    private inner class ScaleListener : ScaleGestureDetector.OnScaleGestureListener {

        override fun onScale(p0: ScaleGestureDetector): Boolean {
            scale *= p0!!.scaleFactor
            imgcredencialshowcheckin.setScaleX(scale)
            imgcredencialshowcheckin.setScaleY(scale)
            imgplacashowcheckin.setScaleX(scale)
            imgplacashowcheckin.setScaleY(scale)
            return true
        }

        override fun onScaleBegin(p0: ScaleGestureDetector): Boolean {
            return true
        }

        override fun onScaleEnd(p0: ScaleGestureDetector) {
            return
        }
    }

}