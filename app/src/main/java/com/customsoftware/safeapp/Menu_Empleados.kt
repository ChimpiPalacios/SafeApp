package com.customsoftware.safeapp

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.adapters.AdapterCliente
import com.customsoftware.safeapp.adapters.AdapterEmpleado
import com.customsoftware.safeapp.modelos.Clientes
import com.customsoftware.safeapp.modelos.Empleados
import com.customsoftware.safeapp.modelos.Fraccionamientos
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*
import kotlin.collections.ArrayList


class Menu_Empleados: AppCompatActivity(), AdapterEmpleado.ClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_empleados)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerVEmp)
        val btn_back = findViewById<ImageButton>(R.id.backEmpBtn)
        val addEmpBtn = findViewById<ImageButton>(R.id.addEmpBtn)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_EMPLEADOS")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }

        var empleados=ArrayList<Empleados>()

        while (rs.next()){

            var NOMBRE : String = rs.getString("NOMBRE")
            var AP : String = rs.getString("AP")
            var AM : String = rs.getString("AM")
            var BIRTHDAY : Date = rs.getDate("BIRTHDAY")
            var TURNO : String = rs.getString("TURNO")
            var ID_USUARIO : Int = rs.getInt("ID_USUARIO")

            empleados.add(Empleados(NOMBRE,"cliente1.png",AP,AM,BIRTHDAY,TURNO,ID_USUARIO))

        }

        val adapter = AdapterEmpleado(empleados,this)
        recyclerView.adapter = adapter

        btn_back.setOnClickListener{
            val Intent = Intent(this,Menu::class.java)
            startActivity(Intent)
        }

        addEmpBtn.setOnClickListener{
            val Intent = Intent(this,AddEmpleados::class.java)
            startActivity(Intent)
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

    override fun clickedItem(Empleados: Empleados) {
        Intent(this,UpdateEmpleados::class.java).putExtra("nombre",Empleados.nombre)
        Intent(this,UpdateEmpleados::class.java).putExtra("ap",Empleados.ap)
        Intent(this,UpdateEmpleados::class.java).putExtra("am",Empleados.am)
        Intent(this,UpdateEmpleados::class.java).putExtra("birthday",Empleados.birthday)
        Intent(this,UpdateEmpleados::class.java).putExtra("turno",Empleados.turno)
        Intent(this,UpdateEmpleados::class.java).putExtra("ID_USUARIO",Empleados.ID_USUARIO)

        startActivity(Intent(this,UpdateEmpleados::class.java))
    }
}