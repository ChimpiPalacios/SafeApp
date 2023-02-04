package com.customsoftware.safeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.adapters.AdapterFraccionamiento
import com.customsoftware.safeapp.modelos.Fraccionamientos
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class UpdateDomicilio: AppCompatActivity() {

    private lateinit var search_up_calle_dom : SearchView
    private lateinit var txtupnumeroextdom: EditText
    private lateinit var txtupnumerointdom: EditText

    private lateinit var btnupinsertdom: Button
    private lateinit var btnbackupdom: ImageButton



    private lateinit var list_up_dom : ListView
    private var IDDOM : Int =0
    private var text_calle: String = ""
    private var calles=ArrayList<String>()

    private var OLDCALLE : String = ""
    private var OLDNUMERO: String = ""
    private var OLDNUMEROEXT : String = ""
    private var OLDNUMEROINT : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.update_domicilio)
        initData()

        btnbackupdom.setOnClickListener {
            val Intent = Intent(this,Check_in::class.java)
            startActivity(Intent)
        }

        btnupinsertdom.setOnClickListener(){
            actualizar()
        }
        search_up_calle_dom.setOnSearchClickListener{
            list_up_dom.visibility = View.VISIBLE
            lista_calles()
        }

        list_up_dom.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_up_dom.adapter.getItem(position) as String
                search_up_calle_dom.setQuery(item,true)
                list_up_dom.visibility = View.GONE
            }
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,calles)

        list_up_dom.adapter = adapter


        search_up_calle_dom.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                search_up_calle_dom.clearFocus()
                if(calles.contains(query)){
                    adapter.filter.filter(query)
                    if (query != null) {
                        text_calle = query
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                if (newText != null) {
                    text_calle = newText
                }
                return false
            }

        })
    }

    private fun initData(){
        search_up_calle_dom = findViewById(R.id.search_up_calle_dom)

        btnupinsertdom = findViewById(R.id.btnupinsertdom)
        btnbackupdom = findViewById(R.id.btnbackupdom)

        txtupnumeroextdom = findViewById(R.id.txtupnumeroextdom)
        txtupnumerointdom = findViewById(R.id.txtupnumerointdom)

        list_up_dom = findViewById(R.id.list_up_dom)

        getData()
    }

    private fun getData(){

        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        IDDOM = sharedPref.getInt("IDDOM", 0)
        //IDDOM=ID_Data.IDDOM

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_DOMICILIO WHERE IDDOM=$IDDOM")
        if (!rs.isBeforeFirst()) {
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs.next()) {
            OLDCALLE = rs.getString("CALLE")
            OLDNUMERO = rs.getString("NUMERO")

        }
        rs.close()

        search_up_calle_dom.setQuery(OLDCALLE,false)
        text_calle = OLDCALLE

        val parts = OLDNUMERO.split("-")
        OLDNUMEROEXT = parts[0]
        OLDNUMEROINT = parts[1]

        if (OLDNUMEROINT != OLDNUMEROEXT ){
            txtupnumeroextdom.setText(OLDNUMEROEXT)
            txtupnumerointdom.setText(OLDNUMEROINT)
        }else{
            txtupnumeroextdom.setText(OLDNUMEROEXT)
            txtupnumerointdom.setText("")
        }

    }

    private fun actualizar() {
        var CALLE : String = text_calle.trim()
        var NUMEROEXT : String = txtupnumeroextdom.text.toString().trim();
        var NUMEROINT : String = txtupnumerointdom.text.toString().trim();

        if(CALLE.isEmpty()){
            Toast.makeText(applicationContext,"El campo Calle es necesario", Toast.LENGTH_SHORT).show()
        }else if(NUMEROEXT.isEmpty()){
            txtupnumeroextdom.setError("El campo Numero es necesario")
        }else if(NUMEROINT.isEmpty()){
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_DOMICILIO WHERE CALLE = '$CALLE' AND NUMERO = '$NUMEROEXT'")
                if (!rs.isBeforeFirst()){
                    val affectedRows: Int = stm.executeUpdate("UPDATE  SP_DOMICILIO SET CALLE = '$CALLE', NUMERO = '$NUMEROEXT' WHERE IDDOM = $IDDOM")
                    if (affectedRows > 0) {
                        val intent = Intent(this, Check_in::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }else{
                    Toast.makeText(applicationContext,"NO SE PERMITEN DUPLICADOS", Toast.LENGTH_SHORT).show()
                }
                stm.connection.close()
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_DOMICILIO WHERE CALLE = '$CALLE' AND NUMERO = '$NUMEROEXT-$NUMEROINT'")
                if (!rs.isBeforeFirst()){
                    val affectedRows: Int = stm.executeUpdate("UPDATE  SP_DOMICILIO SET CALLE = '$CALLE', NUMERO = '$NUMEROEXT-$NUMEROINT' WHERE IDDOM = $IDDOM")
                    if (affectedRows > 0) {
                        val intent = Intent(this, Check_in::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }else{
                    Toast.makeText(applicationContext,"NO SE PERMITEN DUPLICADOS", Toast.LENGTH_SHORT).show()
                }
                stm.connection.close()
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun lista_calles(){
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
        list_up_dom.visibility = View.VISIBLE


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