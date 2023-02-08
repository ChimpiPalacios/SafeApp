package com.customsoftware.safeapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.customsoftware.safeapp.R
import java.sql.*

class ImageZoom : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scale = 1f
    private var IDCHECK: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.image_zoom)

        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        IDCHECK = sharedPref.getInt("IDCHECK", 0)

        imageView = findViewById(R.id.image_view)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        var placaBlob: Blob? = null
        var credencialBlob: Blob? = null

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT PLACA, CREDENCIAL FROM SP_CHECKIN WHERE IDCHECK=$IDCHECK")
        if (!rs.isBeforeFirst()) {
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs.next()) {
            placaBlob = rs.getBlob("PLACA")
            credencialBlob = rs.getBlob("CREDENCIAL")
        }

        val img12 = intent.getStringExtra("image")

        if (img12 == "1"){
            val imageBytesPlaca: ByteArray = placaBlob!!.getBytes(1, placaBlob.length().toInt())
            val optionsPlaca = BitmapFactory.Options()
            optionsPlaca.inSampleSize = 1
            val imageBitmapPlaca = BitmapFactory.decodeByteArray(imageBytesPlaca, 0, imageBytesPlaca.size, optionsPlaca)
            imageView.setImageBitmap(imageBitmapPlaca)


        }else if (img12 == "2"){
            val imageBytesCred: ByteArray = credencialBlob!!.getBytes(1, credencialBlob.length().toInt())
            val optionsCred = BitmapFactory.Options()
            optionsCred.inSampleSize = 1
            val imageBitmapCred = BitmapFactory.decodeByteArray(imageBytesCred, 0, imageBytesCred.size, optionsCred)
            imageView.setImageBitmap(imageBitmapCred)
        }



    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale *= detector.scaleFactor
            imageView.scaleX = scale
            imageView.scaleY = scale
            return true
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
}