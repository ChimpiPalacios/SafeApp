package com.customsoftware.safeapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.sql.*
import java.text.SimpleDateFormat
import java.util.*

class AddCheckIn : AppCompatActivity() {

    private lateinit var search_calle_checkin: EditText
    private lateinit var list_calle_checkin: ListView
    private lateinit var search_numero_checkin: EditText
    private lateinit var list_numero_checkin: ListView
    private lateinit var search_res_checkin: SearchView
    private lateinit var list_res_checkin: ListView
    private lateinit var txtnombrecheckin: EditText
    private lateinit var imgcredencialcheckin: ImageView
    private lateinit var txtvehiculocheckin: EditText
    private lateinit var imgplacacheckin: ImageView

    private lateinit var btninsertcheckin: Button
    private lateinit var btnbackcheckin: ImageButton

    private lateinit var progressBar: ProgressBar

    private var text_calle: String = ""
    private var calles=mutableListOf<String>()
    private var text_numero: String = ""
    private var numeros=mutableListOf<String>()
    private var text_res: String = ""
    private var resi = ArrayList<String>()
    private var IDDOM: String =""

    private var currentImageView: ImageView? = null
    private var currentEditText: EditText? = null

    private val REQUEST_IMAGE_CAPTURE = 1
    private val SPEECH_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.add_check_in)

        progressBar = findViewById<ProgressBar>(R.id.progressBarAddCheckin)

        progressBar.visibility = View.GONE
        if (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            val drawable = progressBar.indeterminateDrawable.mutate()
            val tint = PorterDuffColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN)
            drawable.colorFilter = tint
            progressBar.indeterminateDrawable = drawable
        } else {
            val drawable = progressBar.indeterminateDrawable.mutate()
            val tint = PorterDuffColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN)
            drawable.colorFilter = tint
            progressBar.indeterminateDrawable = drawable
        }

        search_calle_checkin = findViewById<EditText>(R.id.search_calle_checkin)
        list_calle_checkin = findViewById<ListView>(R.id.list_calle_checkin)
        search_numero_checkin = findViewById<EditText>(R.id.search_numero_checkin)
        list_numero_checkin = findViewById<ListView>(R.id.list_numero_checkin)
        search_res_checkin = findViewById<SearchView>(R.id.search_res_checkin)
        list_res_checkin = findViewById<ListView>(R.id.list_res_checkin)
        txtnombrecheckin = findViewById<EditText>(R.id.txtnombrecheckin)
        txtvehiculocheckin = findViewById<EditText>(R.id.txtvehiculocheckin)
        imgcredencialcheckin = findViewById<ImageView>(R.id.imgcredencialcheckin)
        imgplacacheckin = findViewById<ImageView>(R.id.imgplacacheckin)

        btninsertcheckin = findViewById<Button>(R.id.btninsertcheckin)
        btnbackcheckin = findViewById<ImageButton>(R.id.btnbackcheckin)

        btninsertcheckin.setOnClickListener{
            insertar()
        }

        btnbackcheckin.setOnClickListener {
            val Intent = Intent(this, Check_in::class.java)
            startActivity(Intent)
        }

        imgcredencialcheckin.setOnClickListener {
            currentImageView = imgcredencialcheckin
            dispatchTakePictureIntent()
        }

        imgplacacheckin.setOnClickListener {
            currentImageView = imgplacacheckin
            dispatchTakePictureIntent()
        }

        search_res_checkin.setOnSearchClickListener{
            list_res_checkin.visibility = View.VISIBLE
            list_calle_checkin.visibility = View.GONE
            list_numero_checkin.visibility = View.GONE
            lista_res()
        }
        search_res_checkin.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                list_res_checkin.visibility = View.VISIBLE
                lista_res()
            }
        }

        search_res_checkin.setOnClickListener{
            list_res_checkin.visibility = View.VISIBLE
            list_calle_checkin.visibility = View.GONE
            list_numero_checkin.visibility = View.GONE
            lista_res()
        }

        search_calle_checkin.setOnClickListener {
            if(text_res == ""){search_res_checkin.onActionViewCollapsed()}
            list_res_checkin.visibility = View.GONE
            list_calle_checkin.visibility = View.VISIBLE
            list_numero_checkin.visibility = View.GONE
            lista_calles()
        }
        search_numero_checkin.setOnClickListener {
            if(text_res == ""){search_res_checkin.onActionViewCollapsed()}
            list_res_checkin.visibility = View.GONE
            list_calle_checkin.visibility = View.GONE
            list_numero_checkin.visibility = View.VISIBLE
            lista_numeros()
        }
        txtnombrecheckin.setOnClickListener {
            if(text_res == ""){search_res_checkin.onActionViewCollapsed()}
            list_res_checkin.visibility = View.GONE
            list_calle_checkin.visibility = View.GONE
            list_numero_checkin.visibility = View.GONE

            currentEditText = txtnombrecheckin

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        }
        txtvehiculocheckin.setOnClickListener {
            if(text_res == ""){search_res_checkin.onActionViewCollapsed()}
            list_res_checkin.visibility = View.GONE
            list_calle_checkin.visibility = View.GONE
            list_numero_checkin.visibility = View.GONE

            currentEditText = txtvehiculocheckin

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        }

        txtnombrecheckin.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if(text_res == ""){search_res_checkin.onActionViewCollapsed()}
                list_res_checkin.visibility = View.GONE
                list_calle_checkin.visibility = View.GONE
                list_numero_checkin.visibility = View.GONE
            }
        }
        txtvehiculocheckin.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if(text_res == ""){search_res_checkin.onActionViewCollapsed()}
                list_res_checkin.visibility = View.GONE
                list_calle_checkin.visibility = View.GONE
                list_numero_checkin.visibility = View.GONE
            }
        }

        list_calle_checkin.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_calle_checkin.adapter.getItem(position) as String
                search_calle_checkin.setText(item);
                text_calle = item
                list_calle_checkin.visibility = View.GONE
            }
        }

        val adapter_calle: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,calles)

        list_calle_checkin.adapter = adapter_calle



        list_numero_checkin.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_numero_checkin.adapter.getItem(position) as String
                search_numero_checkin.setText(item);
                text_numero = item
                list_numero_checkin.visibility = View.GONE
            }
        }

        val adapter_numero: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,numeros)

        list_numero_checkin.adapter = adapter_numero


        list_res_checkin.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_res_checkin.adapter.getItem(position) as String
                search_res_checkin.setQuery(item,true)
                list_res_checkin.visibility = View.GONE
            }
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,resi)

        list_res_checkin.adapter = adapter

        search_res_checkin.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                search_res_checkin.clearFocus()
                if(resi.contains(query)){
                    adapter.filter.filter(query)
                    if (query != null) {
                        text_res = query
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                if (newText != null) {
                    text_res = newText
                }
                return false
            }


        })
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

    private fun lista_calles(){

        calles.clear()
        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val IDFRACC = sharedPref.getInt("ID", 0)
        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT DISTINCT CALLE FROM SP_DOMICILIO WHERE IDFRACC=$IDFRACC ORDER BY CALLE ASC")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }

        while (rs.next()){
            var CALLE : String = rs.getString("CALLE")
            calles.add(CALLE)
        }
        list_calle_checkin.visibility = View.VISIBLE


    }

    private fun lista_numeros(){
        numeros.clear()
        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val IDFRACC = sharedPref.getInt("ID", 0)
        val CALLE:String = text_calle
        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT NUMERO FROM SP_DOMICILIO WHERE CALLE = '$CALLE' AND IDFRACC=$IDFRACC ORDER BY NUMERO ASC")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }

        while (rs.next()){
            var NUMERO : String = rs.getString("NUMERO")
            numeros.add(NUMERO)
        }
        list_numero_checkin.visibility = View.VISIBLE


    }

    private fun lista_res(){
        resi.clear()

        var CALLE: String= search_calle_checkin.text.toString().trim()
        var NUMERO: String= search_numero_checkin.text.toString().trim()

        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val IDFRACC = sharedPref.getInt("ID", 0)
        val stm: Statement = conexionDB()!!.createStatement()

        val rsDOM: ResultSet = stm.executeQuery("SELECT IDDOM FROM SP_DOMICILIO WHERE CALLE='$CALLE' AND NUMERO='$NUMERO' AND IDFRACC =$IDFRACC ")
        if (!rsDOM.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }
        while (rsDOM.next()){
             IDDOM = rsDOM.getInt("IDDOM").toString()
        }

        val rs: ResultSet = stm.executeQuery("SELECT NOMBRE, AP FROM SP_RESIDENTE LEFT JOIN SP_DOMICILIO ON SP_RESIDENTE.IDDOM = SP_DOMICILIO.IDDOM WHERE SP_RESIDENTE.IDDOM =$IDDOM AND SP_DOMICILIO.IDFRACC =$IDFRACC ")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }
        while (rs.next()){
            var NOMBRE : String = rs.getString("NOMBRE")
            var AP : String = rs.getString("AP")
            resi.add("$NOMBRE $AP")

        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resi)
        list_res_checkin.adapter = adapter

        list_res_checkin.visibility = View.VISIBLE
    }

    private fun insertar() {
        Toast.makeText(applicationContext, "Agregando registro...", Toast.LENGTH_LONG).show()
        progressBar.visibility = View.VISIBLE
        btninsertcheckin.isClickable = false
        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val IDFRACC = sharedPref.getInt("ID", 0)
        var NOMBRE : String = txtnombrecheckin.text.toString().trim();
        var VEHICULO : String = txtvehiculocheckin.text.toString().trim();
        var IDRESIDENTE : String =""
        var NOMBRERESI : String = text_res.trim()
        var SOLONOMBRERESI :String = ""
        var SOLOAPRESI :String = ""

        val parts = NOMBRERESI.split(" ")

        if(parts.size == 1 || parts.size > 2){
            SOLONOMBRERESI = parts[0].trim()
        }else{
            SOLONOMBRERESI = parts[0].trim()
            SOLOAPRESI = parts[1].trim()
        }

        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val FECHAHORA = dateFormat.format(currentTime)


        val img1 = (imgplacacheckin.drawable as BitmapDrawable).bitmap
        val img2 = (imgcredencialcheckin.drawable as BitmapDrawable).bitmap


        val stream1 = ByteArrayOutputStream()
        val stream2 = ByteArrayOutputStream()

        img1.compress(Bitmap.CompressFormat.JPEG, 80, stream1)
        img2.compress(Bitmap.CompressFormat.JPEG, 80, stream2)

        val img1ByteArray = stream1.toByteArray()
        val img2ByteArray = stream2.toByteArray()

        // Perform the task in the background and update the UI on the main thread
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                // Perform background task here

        if(NOMBRE.isEmpty()){
            txtnombrecheckin.setError("El campo Nombre es necesario")
            btninsertcheckin.isClickable = true
        }else if(VEHICULO.isEmpty()){
            txtvehiculocheckin.setError("El campo Vehiculo es necesario")
            btninsertcheckin.isClickable = true
        }else if(text_calle == ""){
            Toast.makeText(applicationContext,"El campo Calle es necesario", Toast.LENGTH_SHORT).show()
            btninsertcheckin.isClickable = true
        }else if(text_numero == ""){
            Toast.makeText(applicationContext,"El campo Numero es necesario", Toast.LENGTH_SHORT).show()
            btninsertcheckin.isClickable = true
        }else if(text_res == ""){
            Toast.makeText(applicationContext,"El campo Residente es necesario", Toast.LENGTH_SHORT).show()
            btninsertcheckin.isClickable = true
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("SELECT IDRESIDENTE FROM SP_RESIDENTE LEFT JOIN SP_DOMICILIO ON SP_RESIDENTE.IDDOM = SP_DOMICILIO.IDDOM WHERE SP_RESIDENTE.IDDOM =$IDDOM AND IDFRACC =$IDFRACC AND SP_RESIDENTE.NOMBRE='$SOLONOMBRERESI' AND SP_RESIDENTE.AP='$SOLOAPRESI' ")
                if (!rs.isBeforeFirst()){
                    IDRESIDENTE = "0"
                    val query = "INSERT INTO SP_CHECKIN (NOMBRE,VEHICULO,PLACA,CREDENCIAL,HORA,IDRESID,NOMBRERESI,IDUSU,IDFRACC) VALUES ('$NOMBRE','$VEHICULO',?,?,'$FECHAHORA',$IDRESIDENTE,'$NOMBRERESI',1,$IDFRACC)"
                    val preparedStatement = conexionDB()!!.prepareStatement(query)
                    preparedStatement.setBytes(1, img1ByteArray)
                    preparedStatement.setBytes(2, img2ByteArray)
                    val result = preparedStatement.executeUpdate()
                    preparedStatement.close()
                    conexionDB()!!.close()

                    if (result > 0) {

                        val intent = Intent(this@AddCheckIn, Check_in::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    while (rs.next()){
                        IDRESIDENTE  = rs.getInt("IDRESIDENTE").toString()
                    }
                    val query = "INSERT INTO SP_CHECKIN (NOMBRE,VEHICULO,PLACA,CREDENCIAL,HORA,IDRESID,NOMBRERESI,IDUSU,IDFRACC) VALUES ('$NOMBRE','$VEHICULO',?,?,'$FECHAHORA',$IDRESIDENTE,'$NOMBRERESI',1,$IDFRACC)"
                    val preparedStatement = conexionDB()!!.prepareStatement(query)
                    preparedStatement.setBytes(1, img1ByteArray)
                    preparedStatement.setBytes(2, img2ByteArray)
                    val result = preparedStatement.executeUpdate()
                    preparedStatement.close()
                    conexionDB()!!.close()

                    if (result > 0) {

                        val intent = Intent(this@AddCheckIn, Check_in::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }




            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                btninsertcheckin.isClickable = true
            }
        }
        }}

    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            currentImageView?.setImageBitmap(imageBitmap)
            currentImageView?.layoutParams?.width = 500
            currentImageView?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT

        }


        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            currentEditText?.setText(result!![0])
        }
    }





}