package com.customsoftware.safeapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.adapters.AdapterFraccionamiento
import com.customsoftware.safeapp.modelos.Fraccionamientos
import kotlinx.android.synthetic.main.update_residentes.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class UpdateResidentes: AppCompatActivity() {

    private lateinit var btnbackupres: ImageButton

    private lateinit var txtupnombreres: EditText
    private lateinit var txtupapres: EditText
    private lateinit var txtupamres: EditText
    private lateinit var txtupnumerocelres: EditText
    private lateinit var search_up_perfil_res: EditText
    private lateinit var list_up_perfil_res: ListView
    private lateinit var search_up_calle_res: EditText
    private lateinit var list_up_calle_res: ListView
    private lateinit var search_up_numero_res: EditText
    private lateinit var list_up_numero_res: ListView
    private lateinit var search_up_tipo_res: EditText
    private lateinit var list_up_tipo_res: ListView
    private lateinit var btnupinsertres: Button
    private lateinit var list_ref_res: ListView

    private var text_calle: String = ""
    private var calles=mutableListOf<String>()
    private var text_numero: String = ""
    private var numeros=mutableListOf<String>()
    private var text_perfil: String = ""
    private var perfiles=mutableListOf<String>()
    private var text_tipo: String = ""
    private var tipos= mutableListOf<String>()

    private var IDRESIDENTE : Int =0
    private var OLDNOMBRE : String = ""
    private var OLDAP: String = ""
    private var OLDAM: String = ""
    private var OLDNUMEROCEL: String = ""
    private var OLDPERFIL: String = ""
    private var OLDTIPO_RES: String = ""
    private var OLDIDDOM: Int =0
    private var OLDCALLE: String = ""
    private var OLDNUMERO: String = ""
    private var REFNOMBRE: String = ""
    private var REFNUMEROCEL: String = ""
    private val REQUEST_CALL_PHONE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.update_residentes)
        initData()

        btnbackupres.setOnClickListener {
            val Intent = Intent(this,Check_in::class.java)
            startActivity(Intent)
        }

        btnupinsertres.setOnClickListener(){
            actualizar()
        }

        txtupnombreres.setOnClickListener{
            list_up_calle_res.visibility = View.GONE
            list_up_numero_res.visibility = View.GONE
            list_up_perfil_res.visibility = View.GONE
            list_up_tipo_res.visibility = View.GONE
        }
        txtupapres.setOnClickListener{
            list_up_calle_res.visibility = View.GONE
            list_up_numero_res.visibility = View.GONE
            list_up_perfil_res.visibility = View.GONE
            list_up_tipo_res.visibility = View.GONE
        }
        txtupamres.setOnClickListener{
            list_up_calle_res.visibility = View.GONE
            list_up_numero_res.visibility = View.GONE
            list_up_perfil_res.visibility = View.GONE
            list_up_tipo_res.visibility = View.GONE
        }
        txtupnumerocelres.setOnClickListener{
            list_up_calle_res.visibility = View.GONE
            list_up_numero_res.visibility = View.GONE
            list_up_perfil_res.visibility = View.GONE
            list_up_tipo_res.visibility = View.GONE
        }

        search_up_calle_res.setOnClickListener{
            list_up_calle_res.visibility = View.VISIBLE
            lista_up_calles()
            list_up_numero_res.visibility = View.GONE
            list_up_perfil_res.visibility = View.GONE
            list_up_tipo_res.visibility = View.GONE
        }
        search_up_numero_res.setOnClickListener{
            list_up_numero_res.visibility = View.VISIBLE
            lista_up_numeros()
            list_up_calle_res.visibility = View.GONE
            list_up_perfil_res.visibility = View.GONE
            list_up_tipo_res.visibility = View.GONE
        }
        search_up_perfil_res.setOnClickListener{
            list_up_perfil_res.visibility = View.VISIBLE
            lista_up_perfiles()
            list_up_numero_res.visibility = View.GONE
            list_up_calle_res.visibility = View.GONE
            list_up_tipo_res.visibility = View.GONE
        }
        search_up_tipo_res.setOnClickListener {
            list_up_tipo_res.visibility = View.VISIBLE
            lista_up_tipo()
            list_up_numero_res.visibility = View.GONE
            list_up_calle_res.visibility = View.GONE
            list_up_perfil_res.visibility = View.GONE
        }

        list_up_calle_res.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_up_calle_res.adapter.getItem(position) as String
                search_up_calle_res.setText(item);
                text_calle = item
                list_up_calle_res.visibility = View.GONE
            }
        }

        val adapter_calle: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,calles)

        list_up_calle_res.adapter = adapter_calle

        list_up_numero_res.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_up_numero_res.adapter.getItem(position) as String
                search_up_numero_res.setText(item);
                text_numero = item
                list_up_numero_res.visibility = View.GONE
            }
        }

        val adapter_numero: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,numeros)

        list_up_numero_res.adapter = adapter_numero

        list_up_perfil_res.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_up_perfil_res.adapter.getItem(position) as String
                search_up_perfil_res.setText(item);
                text_perfil = item
                list_up_perfil_res.visibility = View.GONE
            }
        }

        val adapter_perfil: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,perfiles)

        list_up_perfil_res.adapter = adapter_perfil

        list_up_tipo_res.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_up_tipo_res.adapter.getItem(position) as String
                search_up_tipo_res.setText(item);
                text_tipo = item
                list_up_tipo_res.visibility = View.GONE
            }
        }

        val adapter_tipo: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,tipos)

        list_up_tipo_res.adapter = adapter_tipo

        list_ref_res.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val numeroCel = selectedItem.split(" ")[1]
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.CALL_PHONE)) {
                } else {
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_CALL_PHONE)
                }
            } else {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$numeroCel")
                startActivity(callIntent)
            }
        }
    }

    private fun initData(){
        txtupnombreres = findViewById(R.id.txtupnombreres)
        txtupapres = findViewById(R.id.txtupapres)
        txtupamres = findViewById(R.id.txtupamres)
        txtupnumerocelres = findViewById(R.id.txtupnumerocelres)
        search_up_perfil_res = findViewById(R.id.search_up_perfil_res)
        list_up_perfil_res = findViewById(R.id.list_up_perfil_res)
        search_up_calle_res = findViewById(R.id.search_up_calle_res)
        list_up_calle_res = findViewById(R.id.list_up_calle_res)
        search_up_numero_res = findViewById(R.id.search_up_numero_res)
        list_up_numero_res = findViewById(R.id.list_up_numero_res)
        search_up_tipo_res = findViewById(R.id.search_up_tipo_res)
        list_up_tipo_res = findViewById(R.id.list_up_tipo_res)
        list_ref_res = findViewById(R.id.list_ref_res)

        btnbackupres = findViewById(R.id.btnbackupres)
        btnupinsertres = findViewById(R.id.btnupinsertres)

        getData()
    }

    private fun getData(){
        val list_ref = ArrayList<String>()
        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        IDRESIDENTE = sharedPref.getInt("IDRESIDENTE", 0)
        //IDRESIDENTE=ID_Data.IDRESIDENTE

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_RESIDENTE WHERE IDRESIDENTE=$IDRESIDENTE")
        if (!rs.isBeforeFirst()) {
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs.next()) {
            OLDNOMBRE = rs.getString("NOMBRE")
            OLDAP = rs.getString("AP")
            OLDAM = rs.getString("AM")
            OLDNUMEROCEL = rs.getString("NUMEROCEL")
            OLDPERFIL = rs.getString("PERFIL")
            OLDTIPO_RES = rs.getString("TIPO_RES")
            OLDIDDOM = rs.getInt("IDDOM")

        }
        val rs2: ResultSet = stm.executeQuery("SELECT * FROM SP_DOMICILIO WHERE IDDOM=$OLDIDDOM")
        if (!rs2.isBeforeFirst()) {
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs2.next()) {
            OLDCALLE = rs2.getString("CALLE")
            OLDNUMERO = rs2.getString("NUMERO")
        }
        val rs3: ResultSet = stm.executeQuery("SELECT NOMBRE, NUMEROCEL FROM SP_RESIDENTE WHERE IDDOM=$OLDIDDOM")
        if (!rs3.isBeforeFirst()) {
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs3.next()) {
            REFNOMBRE = rs3.getString("NOMBRE")
            REFNUMEROCEL = rs3.getString("NUMEROCEL")
            list_ref.add("$REFNOMBRE $REFNUMEROCEL")
        }
        val adapter = com.customsoftware.safeapp.adapters.ListView(this,list_ref)
        list_ref_res.adapter = adapter
        rs.close()
        rs2.close()
        rs3.close()

        txtupnombreres.setText(OLDNOMBRE)
        txtupapres.setText(OLDAP)
        txtupamres.setText(OLDAM)
        txtupnumerocelres.setText(OLDNUMEROCEL)

        search_up_perfil_res.setText(OLDPERFIL)
        search_up_calle_res.setText(OLDCALLE)
        search_up_tipo_res.setText(OLDTIPO_RES)

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
        var NOMBRE : String = txtupnombreres.text.toString().trim()
        var AP : String = txtupapres.text.toString().trim()
        var AM : String = txtupamres.text.toString().trim()
        var NUMEROCEL : String = txtupnumerocelres.text.trim().toString()
        var CALLE: String = search_up_calle_res.text.toString().trim()
        var NUMERO: String = search_up_numero_res.text.toString().trim()
        var PERFIL : String = search_up_perfil_res.text.toString().trim()
        var TIPO_RES : String = search_up_tipo_res.text.toString().trim()
        var IDDOM : Int = 0
        if(NOMBRE.isEmpty()){
            txtupnombreres.setError("El campo Nombre es necesario")
        }else if(NUMEROCEL.isEmpty()){
            txtupnumerocelres.setError("El campo Numero de Celular es necesario")
        }else if(NUMEROCEL.length != 10){
            txtupnumerocelres.setError("El campo Numero de Celular solo permite 10 digitos")
        }else if(AP.isEmpty()){
            txtupapres.setError("El campo Apellido Paterno es necesario")
        }else if(AM.isEmpty()){
            txtupamres.setError("El campo Apellido Materno es necesario")
        }else if(CALLE.isEmpty()){
            Toast.makeText(applicationContext,"El campo Calle es necesario", Toast.LENGTH_SHORT).show()
        }else if(NUMERO.isEmpty()){
            Toast.makeText(applicationContext,"El campo Numero es necesario", Toast.LENGTH_SHORT).show()
        }else if(TIPO_RES.isEmpty()){
            Toast.makeText(applicationContext,"El campo Tipo es necesario", Toast.LENGTH_SHORT).show()
        } else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("SELECT IDDOM FROM SP_DOMICILIO WHERE CALLE = '$CALLE' AND NUMERO = '$NUMERO'")
                if (!rs.isBeforeFirst()){
                    Toast.makeText(applicationContext,"NO EXISTE EL DOMICILIO ACTUAL, FAVOR DE REGISTRARLO", Toast.LENGTH_SHORT).show()
                }
                while (rs.next()){
                    IDDOM = rs.getInt("IDDOM")
                }
                val affectedRows: Int = stm.executeUpdate("UPDATE  SP_RESIDENTE SET NOMBRE = '$NOMBRE', AP = '$AP', AM = '$AM',NUMEROCEL='$NUMEROCEL',PERFIL='$PERFIL',TIPO_RES='$TIPO_RES',IDDOM='$IDDOM' WHERE IDRESIDENTE = $IDRESIDENTE ")
                if (affectedRows > 0) {
                    val intent = Intent(this, Check_in::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                stm.connection.close()
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun lista_up_calles(){
        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val IDFRACC = sharedPref.getInt("ID", 0)
        calles.clear()
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
        list_up_calle_res.visibility = View.VISIBLE
    }
    private fun lista_up_numeros(){
        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val IDFRACC = sharedPref.getInt("ID", 0)
        numeros.clear()
        val CALLE:String = search_up_calle_res.text.toString().trim()
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
        list_up_numero_res.visibility = View.VISIBLE
    }
    private fun lista_up_perfiles(){
        perfiles.clear()
        perfiles.add("")
        perfiles.add("PRESIDENTE")
        perfiles.add("TESORERO")
        perfiles.add("SECRETARIO")
        list_up_perfil_res.visibility = View.VISIBLE
    }
    private fun lista_up_tipo(){
        tipos.clear()
        tipos.add("PROPIETARIO")
        tipos.add("ARRENDATARIO")
        list_up_tipo_res.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CALL_PHONE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return
            }
        }
    }
}