package com.customsoftware.safeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.adapters.AdapterFragmentDomicilio
import com.customsoftware.safeapp.adapters.AdapterFragmentResidente
import com.customsoftware.safeapp.modelos.Domicilios
import com.customsoftware.safeapp.modelos.Residentes
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ResidFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var search_res : SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_resid, container, false)
        recyclerView = view.findViewById(R.id.recyclerVResid)
        search_res= view.findViewById(R.id.search_res) as SearchView

        val sharedPref = context?.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val IDFRACC = sharedPref?.getInt("ID", 0)

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_RESIDENTE LEFT JOIN SP_DOMICILIO ON SP_RESIDENTE.IDDOM = SP_DOMICILIO.IDDOM WHERE SP_DOMICILIO.IDFRACC =$IDFRACC")
        if (!rs.isBeforeFirst()) {
            Toast.makeText(activity, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        var residentes = ArrayList<Residentes>()
        while (rs.next()) {
            var NOMBRE: String = rs.getString("NOMBRE")
            var NUMEROCEL: String = rs.getString("NUMEROCEL")
            var AP: String = rs.getString("AP")
            var AM: String = rs.getString("AM")
            var PERFL: String = rs.getString("PERFIL")
            var TIPO_RES: String = rs.getString("TIPO_RES")
            var IDDOM: Int = rs.getInt("IDDOM")
            residentes.add(Residentes(NOMBRE,NUMEROCEL,AP,AM,PERFL,TIPO_RES,IDDOM))
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        val adapter = AdapterFragmentResidente(residentes,requireActivity(), ResidFragment.MyClickListener())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.updateList(residentes)
        adapter.getFilter().filter("")

        search_res.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    adapter.updateList(adapter.getOriginalData())
                } else {
                    adapter.getFilter().filter(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    adapter.updateList(adapter.getOriginalData())
                } else {
                    adapter.getFilter().filter(newText)
                }
                return true
            }
        })

        view.findViewById<ImageButton>(R.id.addResidBtn)?.let {
            it.setOnClickListener {
                val Intent = Intent(requireActivity(), AddResidente::class.java)
                startActivity(Intent)
            }
        }

        return view
    }

    class MyClickListener : AdapterFragmentResidente.ClickListener {

        override fun clickedItem(residentes: Residentes,context: Context) {

            var NOMBRE : String = residentes.nombre
            var AP : String = residentes.ap
            var AM : String = residentes.am
            var NUMEROCEL : String = residentes.numero_cel
            var PERFIL : String = residentes.perfil
            var TIPORES : String = residentes.tipo_res
            var IDDOM: Int = residentes.iddom
            var IDRESIDENTE: Int = 0

            val stm: Statement = conexionDB()!!.createStatement()
            val rs: ResultSet = stm.executeQuery("SELECT IDRESIDENTE FROM SP_RESIDENTE WHERE NOMBRE ='$NOMBRE' AND AP='$AP' AND AM='$AM' AND NUMEROCEL=$NUMEROCEL AND PERFIL='$PERFIL' AND TIPO_RES='$TIPORES' AND IDDOM=$IDDOM ")
            if (!rs.isBeforeFirst()) {
                Toast.makeText(context, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            }
            while (rs.next()) {
                IDRESIDENTE = rs.getInt("IDRESIDENTE")
            }

            rs.close()
            stm.close()

            val sharedPref = context.getSharedPreferences("id_data", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt("IDRESIDENTE", IDRESIDENTE)
            editor.commit()

            val intent = Intent(context, UpdateResidentes::class.java)
            context.startActivity(intent)
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
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
        }
        return cnn
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResidFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}