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
import com.example.safeapp.adapters.AdapterFragmentDomicilio
import com.example.safeapp.adapters.AdapterFragmentResidente
import com.example.safeapp.modelos.Domicilios
import com.example.safeapp.modelos.Residentes
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResidFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResidFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_resid, container, false)

        recyclerView = view.findViewById(R.id.recyclerVResid)

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_RESIDENTE")
        if (!rs.isBeforeFirst()) {
            Toast.makeText(activity, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        // Inflate the layout for this fragment
        var residentes = ArrayList<Residentes>()

        while (rs.next()) {
            var NOMBRE: String = rs.getString("NOMBRE")
            var NUMEROCEL: Int = rs.getInt("NUMEROCEL")
            var AP: String = rs.getString("AP")
            var AM: String = rs.getString("AM")
            var PERFL: String = rs.getString("PERFIL")
            var IDDOM: Int = rs.getInt("IDDOM")




            residentes.add(Residentes(NOMBRE,NUMEROCEL,AP,AM,PERFL,IDDOM))

        }
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        val adapter = AdapterFragmentResidente(residentes, ResidFragment.MyClickListener())
        recyclerView.adapter = adapter



        view.findViewById<ImageButton>(R.id.addResidBtn)?.let {
            it.setOnClickListener {
                val Intent = Intent(requireActivity(), AddResidente::class.java)
                startActivity(Intent)
            }
        }



        // Inflate the layout for this fragment
        return view
    }
    class MyClickListener : AdapterFragmentResidente.ClickListener {

        override fun clickedItem(residentes: Residentes) {
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
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
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
         * @return A new instance of fragment ResidFragment.
         */
        // TODO: Rename and change types and number of parameters
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