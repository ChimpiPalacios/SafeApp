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
import com.customsoftware.safeapp.adapters.AdapterFraccionamiento
import com.customsoftware.safeapp.adapters.AdapterFragmCheckIn
import com.customsoftware.safeapp.modelos.CheckIns
import com.customsoftware.safeapp.modelos.Fraccionamientos
import kotlinx.android.synthetic.main.fragment_check_in.*
import java.sql.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ChackInFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var search_checkin : SearchView

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_check_in, container, false)
        val sharedPref = context?.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val IDFRACC = sharedPref?.getInt("ID", 0)
        recyclerView = view.findViewById(R.id.recyclerVCheckIn)
        search_checkin = view.findViewById(R.id.search_checkin)

        val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val yesterdayString = format.format(yesterday)
        val today = java.sql.Date(System.currentTimeMillis())
        val todayString = format.format(today)

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_CHECKIN WHERE DATE(HORA) BETWEEN date('$yesterdayString') AND date('$todayString') AND IDFRACC=$IDFRACC ORDER BY HORA DESC" )
        if (!rs.isBeforeFirst()){
            Toast.makeText(requireActivity(), "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }


        var checkins=ArrayList<CheckIns>()

        while (rs.next()){
            var NOMBRE : String = rs.getString("NOMBRE")
            var VEHICULO : String = rs.getString("VEHICULO")
            var NOMBRERESI : String = rs.getString("NOMBRERESI")
            var HORA : Timestamp = rs.getTimestamp("HORA")
            var IDRESID : Int = rs.getInt("IDRESID")


            checkins.add(CheckIns(NOMBRE,VEHICULO,NOMBRERESI,HORA,IDRESID))

        }
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        val adapter = AdapterFragmCheckIn(checkins,requireActivity(), MyClickListener())
        recyclerView.adapter = adapter

        adapter.updateList(checkins)
        adapter.getFilter().filter("")

        search_checkin.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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



        view.findViewById<ImageButton>(R.id.addChekInBtn)?.let {
            it.setOnClickListener {
                val Intent = Intent(requireActivity(), com.customsoftware.safeapp.AddCheckIn::class.java)
                startActivity(Intent)
            }
        }

        return view
    }

    class MyClickListener : AdapterFragmCheckIn.ClickListener {

        override fun clickedItem(checkIns: CheckIns,context: Context) {
            var NOMBRE : String = checkIns.nombre
            var VEHICULO : String = checkIns.vehiculo
            var HORA : Timestamp = checkIns.hora
            var IDRESID : Int = checkIns.idresid
            var IDCHECK: Int = 0

            val stm: Statement = conexionDB()!!.createStatement()
            val rs: ResultSet = stm.executeQuery("SELECT IDCHECK FROM SP_CHECKIN WHERE NOMBRE ='$NOMBRE' AND VEHICULO='$VEHICULO' AND HORA='$HORA' AND IDRESID=$IDRESID")
            if (!rs.isBeforeFirst()) {
                Toast.makeText(context, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            }
            while (rs.next()) {
                IDCHECK = rs.getInt("IDCHECK")
            }
            rs.close()

            val sharedPref = context.getSharedPreferences("id_data", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putInt("IDCHECK", IDCHECK)
            editor.commit()

            val intent = Intent(context, ShowCheckIn::class.java)
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
            //Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
        return cnn
    }



    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChackInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}