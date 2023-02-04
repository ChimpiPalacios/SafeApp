package com.customsoftware.safeapp


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
//import dev.sasikanth.colorsheet.ColorSheet
//import dev.sasikanth.colorsheet.utils.ColorSheetUtils


class AddClientes: AppCompatActivity() {

    private lateinit var txtnombre: TextView
    private lateinit var txtempresa: TextView
    private lateinit var txtnumerocel: TextView
    private lateinit var txtcolor: TextView
    private lateinit var btninsertcl: Button
    private lateinit var btnbackclnt: ImageButton

    var pickedPhoto : Uri? = null
    var pickedBitMap : Bitmap? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_clientes)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        txtnombre = findViewById<TextView>(R.id.txtnombre)
        txtempresa = findViewById<TextView>(R.id.txtempresa)
        txtnumerocel = findViewById<TextView>(R.id.txtnumerocel)
        txtcolor = findViewById<TextView>(R.id.txtcolor)
        btninsertcl = findViewById<Button>(R.id.btninsertcl)
        btnbackclnt = findViewById<ImageButton>(R.id.btnbackclnt)

        btninsertcl.setOnClickListener(){
            insertar()
        }

        btnbackclnt.setOnClickListener {
            val Intent = Intent(this,Menu_Clientes::class.java)
            startActivity(Intent)

        }

        txtcolor.setOnClickListener{

            /*ColorSheet().cornerRadius(8)
                .colorPicker(
                    colors = colors,
                    noColorOption = noColorOption,
                    selectedColor = selectedColor,
                    listener = { color ->
                        selectedColor = color
                        setColor(selectedColor)
                    })
                .show(supportFragmentManager)*/
        }


    }
    fun pickPhoto(view: View){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) { // izin alınmadıysa
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1)
        } else {
            val galeriIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntext,2)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data
            if (pickedPhoto != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver,pickedPhoto!!)
                    pickedBitMap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(pickedBitMap)
                }
                else {
                    pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedPhoto)
                    imageView.setImageBitmap(pickedBitMap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

/*
    private fun setColor(@ColorInt color: Int) {
        if (color != ColorSheet.NO_COLOR) {
            colorBackground.setBackgroundColor(color)
            colorSelectedText.text = ColorSheetUtils.colorToHex(color)
        } else {
            val primaryColor = ContextCompat.getColor(this, R.color.colorPrimary)
            colorBackground.setBackgroundColor(primaryColor)
            colorSelectedText.text = getString(R.string.no_color)
        }
    }*/

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
        var NOMBRE : String = txtnombre.text.toString().trim();
        var EMPRESA : String = txtempresa.text.toString().trim();
        var NUMEROCEL : String = txtnumerocel.text.toString().trim();
        var LOGO : String = "";
        var COLOR : String = txtcolor.text.toString().trim();

        if(NOMBRE.isEmpty()){
            txtnombre.setError("El campo Nombre es necesario")
        }else if(EMPRESA.isEmpty()){
            txtempresa.setError("El campo Empresa es necesario")
        }else if(NUMEROCEL.isEmpty()){
            txtnumerocel.setError("El campo Numero Cel es necesario")
        }
        /*else if(LOGO.isEmpty()){
            txtlogo.setError("El campo Logo es necesario")
        }*/
        else if(COLOR.isEmpty()){

            txtcolor.setError("El campo Color es necesario")
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                //imageView.setImageBitmap(pickedBitMap)
                val rs: Int = stm.executeUpdate("INSERT INTO SP_CLIENTE (NOMBRE,EMPRESA,NUMEROCEL,LOGO,COLOR) VALUES ('$NOMBRE','$EMPRESA','$NUMEROCEL','$LOGO','$COLOR')")
                if (rs > 0) {
                    Toast.makeText(applicationContext, "Insertado correctamente.", Toast.LENGTH_LONG).show()
                }

            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

        }

    }




}

