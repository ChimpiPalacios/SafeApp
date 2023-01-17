package com.customsoftware.safeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.*
import androidx.core.view.contains
import com.customsoftware.safeapp.modelos.Fraccionamientos
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class AddDomicilio : AppCompatActivity() {
    //private lateinit var txtcalledom: TextView
    private lateinit var txtnumerodom: TextView
    private lateinit var txtextenciondom: TextView
    private lateinit var btninsertdom: Button
    private lateinit var btnbackdomicilio: ImageButton
    private lateinit var search_calle_dom: SearchView
    private lateinit var list_dom : ListView
    private var text_calle: String = ""
    private var calles=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_domicilio)


        search_calle_dom = findViewById<SearchView>(R.id.search_calle_dom)
        list_dom = findViewById<ListView>(R.id.list_dom)
        txtnumerodom = findViewById<TextView>(R.id.txtnumerodom)
        txtextenciondom = findViewById<TextView>(R.id.txtextenciondom)
        btninsertdom = findViewById<Button>(R.id.btninsertdom)
        btnbackdomicilio = findViewById<ImageButton>(R.id.btnbackdomicilio)



        search_calle_dom.setOnSearchClickListener{
            list_dom.visibility = View.VISIBLE
            lista_calles()

        }
        btninsertdom.setOnClickListener{
            insertar()
        }

        txtnumerodom.setOnClickListener{
            list_dom.visibility = View.GONE
        }

        txtextenciondom.setOnClickListener{
            list_dom.visibility = View.GONE
        }

        btnbackdomicilio.setOnClickListener {
            val Intent = Intent(this,Check_in::class.java)
            startActivity(Intent)
        }

        list_dom.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val item = list_dom.adapter.getItem(position) as String
                search_calle_dom.setQuery(item,true)
                list_dom.visibility = View.GONE
            }
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,calles)

        list_dom.adapter = adapter


        search_calle_dom.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                search_calle_dom.clearFocus()
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
        var CALLE : String = text_calle.trim()
        var NUMERO : String = txtnumerodom.text.toString().trim();
        var NoEXTENSION : String = txtextenciondom.text.toString().trim();
        if(text_calle == ""){
            Toast.makeText(applicationContext,"El campo Calle es necesario", Toast.LENGTH_SHORT).show()
        }else if(NUMERO.isEmpty()){
            txtnumerodom.setError("El campo Numero es necesario")
        } else{
            try {
                val stm: Statement = conexionDB()!!.createStatement()
                val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_DOMICILIO WHERE CALLE = '$CALLE' AND NUMERO = '$NUMERO$NoEXTENSION'")
                if (!rs.isBeforeFirst()){
                    val affectedRows: Int = stm.executeUpdate("INSERT INTO SP_DOMICILIO (CALLE,NUMERO,IDFRACC) VALUES ('$CALLE','$NUMERO$NoEXTENSION',1)")
                    if (affectedRows > 0) {
                        Toast.makeText(applicationContext,"INSERTADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"NO SE PERMITEN DUPLICADOS", Toast.LENGTH_SHORT).show()
                }
                stm.connection.close()
            } catch (e: java.lang.Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun lista_calles(){

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT DISTINCT CALLE FROM SP_DOMICILIO")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }

        while (rs.next()){
            var CALLE : String = rs.getString("CALLE")
                calles.add(CALLE)
        }
        list_dom.visibility = View.VISIBLE


    }
}


