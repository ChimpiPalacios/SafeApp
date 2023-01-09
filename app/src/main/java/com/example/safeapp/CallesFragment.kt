package com.example.safeapp

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safeapp.adapters.AdapterFragmCheckIn
import com.example.safeapp.adapters.AdapterFragmentDomicilio
import com.example.safeapp.modelos.CheckIns
import com.example.safeapp.modelos.Domicilios
import java.sql.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CallesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CallesFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_calles, container, false)

        recyclerView = view.findViewById(R.id.recyclerVCalles)

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_DOMICILIO")
        if (!rs.isBeforeFirst()) {
            //Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        // Inflate the layout for this fragment
        var domicilios = ArrayList<Domicilios>()

        while (rs.next()) {
            var CALLE: String = rs.getString("CALLE")
            var NUMERO: String = rs.getString("NUMERO")
            var IDFRACC: Int = rs.getInt("IDFRACC")


            domicilios.add(Domicilios(CALLE, NUMERO, IDFRACC))

        }
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.layoutManager = layoutManager
        val adapter = AdapterFragmentDomicilio(domicilios, CallesFragment.MyClickListener())
        recyclerView.adapter = adapter



        view.findViewById<ImageButton>(R.id.addDomBtn)?.let {
            it.setOnClickListener {
                val Intent = Intent(requireActivity(), AddDomicilio::class.java)
                startActivity(Intent)
            }
        }

        return view
    }
    class MyClickListener : AdapterFragmentDomicilio.ClickListener {

        override fun clickedItem(domicilios: Domicilios) {
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
         * @return A new instance of fragment CallesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CallesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}