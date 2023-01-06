package com.example.safeapp

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safeapp.adapters.AdapterFraccionamiento
import com.example.safeapp.adapters.AdapterFragmCheckIn
import com.example.safeapp.modelos.CheckIns
import com.example.safeapp.modelos.Fraccionamientos
import kotlinx.android.synthetic.main.fragment_check_in.*
import java.sql.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChackInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChackInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView

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

        recyclerView = view.findViewById(R.id.recyclerVCheckIn)

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_CHECKIN")
        if (!rs.isBeforeFirst()){
            //Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()

        }

        var checkins=ArrayList<CheckIns>()

        while (rs.next()){
            var NOMBRE : String = rs.getString("NOMBRE")
            var VEHICULO : String = rs.getString("VEHICULO")
            var PLACA : String = rs.getString("PLACA")
            var CREDENCIAL : String = rs.getString("CREDENCIAL")
            var HORA : Date = rs.getDate("HORA")
            var IDCHECK : Int = rs.getInt("IDCHECK")
            var IDRESID : Int = rs.getInt("IDRESID")
            var IDUSU : Int = rs.getInt("IDUSU")

            checkins.add(CheckIns(NOMBRE,VEHICULO,PLACA,CREDENCIAL,HORA,IDCHECK,IDRESID,IDUSU))

        }
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        val adapter = AdapterFragmCheckIn(checkins, MyClickListener())
        recyclerView.adapter = adapter



        view.findViewById<ImageButton>(R.id.addChekInBtn)?.let {
            it.setOnClickListener {
                val Intent = Intent(requireActivity(),AddCheckIn::class.java)
                startActivity(Intent)
            }
        }

        return view



    }

    class MyClickListener : AdapterFragmCheckIn.ClickListener {

        override fun clickedItem(checkIns: CheckIns) {
            TODO("Not yet implemented")
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChackInFragment.
         */
        // TODO: Rename and change types and number of parameters
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