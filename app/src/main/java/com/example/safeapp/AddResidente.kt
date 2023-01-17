package com.example.safeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.StrictMode
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.add_residente.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class AddResidente : AppCompatActivity() {
    private lateinit var txtnombreres: TextView
    private lateinit var txtnumerocelres: TextView
    private lateinit var txtapres: TextView
    private lateinit var txtamres: TextView
    private lateinit var search_calle_res: EditText
    private lateinit var list_calle_res : ListView
    private lateinit var search_numero_res: EditText
    private lateinit var list_numero_res : ListView
    private lateinit var search_perfil_res: EditText
    private lateinit var list_perfil_res : ListView
    private lateinit var search_tipo_res: EditText
    private lateinit var list_tipo_res : ListView
    private lateinit var btninsertres: Button
    private lateinit var btnbackresidente: ImageButton
    private var text_calle: String = ""
    private var calles=mutableListOf<String>()
    private var text_numero: String = ""
    private var numeros=mutableListOf<String>()
    private var text_perfil: String = ""
    private var perfiles=mutableListOf<String>()
    private var text_tipo: String = ""
    private var tipos= mutableListOf<String>()
    private var text_iddom: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_residente)

        //final MenuIteam searchItem = menu.findItem()
        txtnombreres = findViewById<TextView>(R.id.txtnombreres)
        txtapres = findViewById<TextView>(R.id.txtapres)
        txtamres = findViewById<TextView>(R.id.txtamres)
        txtnumerocelres = findViewById<TextView>(R.id.txtnumerocelres)
        search_calle_res = findViewById(R.id.search_calle_res)
        list_calle_res = findViewById<ListView>(R.id.list_calle_res)
        search_numero_res = findViewById(R.id.search_numero_res)
        list_numero_res = findViewById<ListView>(R.id.list_numero_res)
        search_perfil_res = findViewById(R.id.search_perfil_res)
        list_perfil_res = findViewById<ListView>(R.id.list_perfil_res)
        search_tipo_res = findViewById(R.id.search_tipo_res)
        list_tipo_res = findViewById<ListView>(R.id.list_tipo_res)

        btninsertres = findViewById<Button>(R.id.btninsertres)
        btnbackresidente = findViewById<ImageButton>(R.id.btnbackresidente)


        txtnombreres.setOnClickListener{
            list_calle_res.visibility = View.GONE
            list_numero_res.visibility = View.GONE
            list_perfil_res.visibility = View.GONE
            list_tipo_res.visibility = View.GONE
        }
        txtapres.setOnClickListener{
            list_calle_res.visibility = View.GONE
            list_numero_res.visibility = View.GONE
            list_perfil_res.visibility = View.GONE
            list_tipo_res.visibility = View.GONE
        }
        txtamres.setOnClickListener{
            list_calle_res.visibility = View.GONE
            list_numero_res.visibility = View.GONE
            list_perfil_res.visibility = View.GONE
            list_tipo_res.visibility = View.GONE
        }
        txtnumerocelres.setOnClickListener{
            list_calle_res.visibility = View.GONE
            list_numero_res.visibility = View.GONE
            list_perfil_res.visibility = View.GONE
            list_tipo_res.visibility = View.GONE
        }
        search_calle_res.setOnClickListener{
            list_calle_res.visibility = View.VISIBLE
            lista_calles()
            list_numero_res.visibility = View.GONE
            list_perfil_res.visibility = View.GONE
            list_tipo_res.visibility = View.GONE
        }
        search_numero_res.setOnClickListener{
            list_numero_res.visibility = View.VISIBLE
            lista_numeros()
            list_calle_res.visibility = View.GONE
            list_perfil_res.visibility = View.GONE
            list_tipo_res.visibility = View.GONE
        }
        search_perfil_res.setOnClickListener{
            list_perfil_res.visibility = View.VISIBLE
            lista_perfiles()
            list_numero_res.visibility = View.GONE
            list_calle_res.visibility = View.GONE
            list_tipo_res.visibility = View.GONE
        }
        search_tipo_res.setOnClickListener {
            list_tipo_res.visibility = View.VISIBLE
            lista_tipo()
            list_numero_res.visibility = View.GONE
            list_calle_res.visibility = View.GONE
            list_perfil_res.visibility = View.GONE
        }


        btninsertres.setOnClickListener{
            insertar()
        }
        btnbackresidente.setOnClickListener {
            val intent = Intent(this,Check_in::class.java)
            startActivity(intent)
        }

        list_calle_res.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_calle_res.adapter.getItem(position) as String
                search_calle_res.setText(item);
                text_calle = item
                list_calle_res.visibility = View.GONE
            }
        }

        val adapter_calle: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,calles)

        list_calle_res.adapter = adapter_calle

        /*search_calle_res.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                search_calle_res.clearFocus()
                if(calles.contains(query)){
                    adapter_calle.filter.filter(query)
                    if (query != null) {
                        text_calle = query
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter_calle.filter.filter(newText)
                if (newText != null) {
                    text_calle = newText
                }
                return false
            }

        })*/

        list_numero_res.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_numero_res.adapter.getItem(position) as String
                search_numero_res.setText(item);
                text_numero = item
                list_numero_res.visibility = View.GONE
            }
        }

        val adapter_numero: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,numeros)

        list_numero_res.adapter = adapter_numero

       /* search_numero_res.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                search_numero_res.clearFocus()
                if(numeros.contains(query)){
                    adapter_numero.filter.filter(query)
                    if (query != null) {
                        text_numero = query
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter_numero.filter.filter(newText)
                if (newText != null) {
                    text_numero = newText
                }
                return false
            }

        })*/

        list_perfil_res.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_perfil_res.adapter.getItem(position) as String
                search_perfil_res.setText(item);
                text_perfil = item
                list_perfil_res.visibility = View.GONE
            }
        }

        val adapter_perfil: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,perfiles)

        list_perfil_res.adapter = adapter_perfil

       /* search_perfil_res.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                search_perfil_res.clearFocus()
                if(perfiles.contains(query)){
                    adapter_perfil.filter.filter(query)
                    if (query != null) {
                        text_perfil = query
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter_perfil.filter.filter(newText)
                if (newText != null) {
                    text_perfil = newText
                }
                return false
            }

        })*/

        list_tipo_res.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_tipo_res.adapter.getItem(position) as String
                search_tipo_res.setText(item);
                text_tipo = item
                list_tipo_res.visibility = View.GONE
            }
        }

        val adapter_tipo: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,tipos)

        list_tipo_res.adapter = adapter_tipo

       /* search_tipo_res.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                search_tipo_res.clearFocus()
                if(tipos.contains(query)){
                    adapter_tipo.filter.filter(query)
                    if (query != null) {
                        text_tipo = query
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter_tipo.filter.filter(newText)
                if (newText != null) {
                    text_tipo = newText
                }
                return false
            }

        })*/
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
        var NOMBRE : String = txtnombreres.text.toString().trim()
        var AP : String = txtapres.text.toString().trim()
        var AM : String = txtamres.text.toString().trim()
        var NUMEROCEL : CharSequence = txtnumerocelres.text.trim()
        var CALLE: String = search_calle_res.text.toString().trim()
        var NUMERO: String = search_numero_res.text.toString().trim()
        var PERFIL : String = search_perfil_res.text.toString().trim()
        var TIPO_RES : String = search_tipo_res.text.toString().trim()

        if(NOMBRE.isEmpty()){
            txtnombreres.setError("El campo Nombre es necesario")
        }else if(NUMEROCEL.isEmpty()){
            txtnumerocelres.setError("El campo Numero de Celular es necesario")
        }else if(NUMEROCEL.length != 10){
            txtnumerocelres.setError("El campo Numero de Celular solo permite 10 digitos")
        }else if(AP.isEmpty()){
            txtapres.setError("El campo Apellido Paterno es necesario")
        }else if(AM.isEmpty()){
            txtamres.setError("El campo Apellido Materno es necesario")
        }else if(text_calle == ""){
            Toast.makeText(applicationContext,"El campo Calle es necesario", Toast.LENGTH_SHORT).show()
        }else if(text_numero == ""){
            Toast.makeText(applicationContext,"El campo Numero es necesario", Toast.LENGTH_SHORT).show()

        }else if(text_tipo == ""){
            Toast.makeText(applicationContext,"El campo Tipo es necesario", Toast.LENGTH_SHORT).show()
        } else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("SELECT IDDOM FROM SP_DOMICILIO WHERE CALLE = '$CALLE' AND NUMERO = '$NUMERO'")
                if (!rs.isBeforeFirst()){
                    var IDDOM : String = rs.getString("IDDOM")
                    val affectedRows: Int = stm.executeUpdate("INSERT INTO SP_RESIDENTE (NOMBRE,NUMEROCEL,AP,AM,PERFIL,IDDOM,TIPO_RES) VALUES ('$NOMBRE',$NUMEROCEL,'$AP','$AM','$PERFIL',$IDDOM,'$TIPO_RES')")
                    if (affectedRows > 0) {
                        Toast.makeText(applicationContext,"INSERTADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"NO EXISTE EL DOMICILIO ACTUAL, FAVOR DE REGISTRARLO", Toast.LENGTH_SHORT).show()
                }

                stm.connection.close()
            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun lista_calles(){

        calles.clear()
        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT DISTINCT CALLE FROM SP_DOMICILIO ORDER BY CALLE ASC")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }

        while (rs.next()){
            var CALLE : String = rs.getString("CALLE")
            calles.add(CALLE)
        }
        list_calle_res.visibility = View.VISIBLE


    }

    private fun lista_numeros(){
        numeros.clear()
        val CALLE:String = text_calle
        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT NUMERO FROM SP_DOMICILIO WHERE CALLE = '$CALLE' ORDER BY NUMERO ASC")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }

        while (rs.next()){
            var NUMERO : String = rs.getString("NUMERO")
            numeros.add(NUMERO)
        }
        list_numero_res.visibility = View.VISIBLE


    }

    private fun lista_perfiles(){
        perfiles.clear()
        perfiles.add("")
        perfiles.add("Presidente")
        perfiles.add("Tesorero")
        perfiles.add("Secretario")
        list_perfil_res.visibility = View.VISIBLE


    }

    private fun lista_tipo(){
        tipos.clear()
        tipos.add("Propietario")
        tipos.add("Arrendatario")
        list_tipo_res.visibility = View.VISIBLE


    }
}