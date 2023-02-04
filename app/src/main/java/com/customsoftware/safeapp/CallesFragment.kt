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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.customsoftware.safeapp.adapters.AdapterFragmCheckIn
import com.customsoftware.safeapp.adapters.AdapterFragmentDomicilio
import com.customsoftware.safeapp.modelos.CheckIns
import com.customsoftware.safeapp.modelos.Domicilios
import java.sql.*
import androidx.core.content.ContextCompat.startActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CallesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var search_dom :SearchView

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
        val view = inflater.inflate(R.layout.fragment_calles, container, false)

        recyclerView = view.findViewById(R.id.recyclerVCalles)
        search_dom = view.findViewById(R.id.search_dom) as SearchView

        val sharedPref = context?.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val IDFRACC = sharedPref?.getInt("ID", 0)

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_DOMICILIO WHERE IDFRACC =$IDFRACC")
        if (!rs.isBeforeFirst()) {
            Toast.makeText(activity, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        var domicilios = ArrayList<Domicilios>()

        while (rs.next()) {
            var CALLE: String = rs.getString("CALLE")
            var NUMERO: String = rs.getString("NUMERO")
            var IDFRACC: Int = rs.getInt("IDFRACC")

            domicilios.add(Domicilios(CALLE, NUMERO, IDFRACC))
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        val adapter = AdapterFragmentDomicilio(domicilios,requireActivity(), CallesFragment.MyClickListener())
        adapter.updateList(domicilios)
        recyclerView.adapter = adapter
        adapter.getFilter().filter("")

        search_dom.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        view.findViewById<ImageButton>(R.id.addDomBtn)?.let {
            it.setOnClickListener {
                val Intent = Intent(requireActivity(), AddDomicilio::class.java)
                startActivity(Intent)
            }
        }

        return view
    }
    class MyClickListener : AdapterFragmentDomicilio.ClickListener {

        override fun clickedItem(domicilios: Domicilios, context: Context) {

            var CALLE : String = domicilios.calle
            var NUMERO : String = domicilios.numero
            var IDFRACC : Int = domicilios.idfracc
            var IDDOM: Int = 0

            val stm: Statement = conexionDB()!!.createStatement()
            val rs: ResultSet = stm.executeQuery("SELECT IDDOM FROM SP_DOMICILIO WHERE IDFRACC =$IDFRACC AND CALLE='$CALLE' AND NUMERO='$NUMERO'")
            if (!rs.isBeforeFirst()) {
                Toast.makeText(context, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            }
            while (rs.next()) {
                IDDOM = rs.getInt("IDDOM")
            }
            rs.close()

            val sharedPref = context.getSharedPreferences("id_data", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt("IDDOM", IDDOM)
            editor.commit()

            val intent = Intent(context, UpdateDomicilio::class.java)
            context.startActivity(intent)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CallesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
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
}