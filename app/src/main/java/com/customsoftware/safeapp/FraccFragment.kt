package com.customsoftware.safeapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.customsoftware.safeapp.modelos.Residentes
import kotlinx.android.synthetic.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class fraccFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val REQUEST_CALL_PHONE = 1


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

        val sharedPref = context?.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        val ID_FRACCIONAMIENTO = sharedPref?.getInt("ID", 0)
        var PAIS : String = ""
        var ESTADO : String = ""
        var MUNICIPIO : String = ""
        var FRACCIONAMIENTO : String = ""
        var ETAPA : String = ""

        var NOMBRE : String = ""
        var AP : String = ""
        var NUMEROCEL : String = ""
        var CALLE : String = ""
        var NUMERO : String = ""

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fracc, container, false)

        // Get the TextViews from the layout and set their text to the values of x and y
        val textfraccinfo = view.findViewById<TextView>(R.id.textfraccinfo)
        val textestadoinfo = view.findViewById<TextView>(R.id.textestadoinfo)
        val textetapainfo = view.findViewById<TextView>(R.id.textetapainfo)
        val textpaisinfo = view.findViewById<TextView>(R.id.textpaisinfo)
        val textmunicipioinfo = view.findViewById<TextView>(R.id.textmunicipioinfo)

        val list_pres_fracc = view.findViewById<ListView>(R.id.list_pres_fracc)
        val list_pres = ArrayList<String>()
        val list_tes_fracc = view.findViewById<ListView>(R.id.list_tes_fracc)
        val list_tes = ArrayList<String>()
        val list_sec_fracc = view.findViewById<ListView>(R.id.list_sec_fracc)
        val list_sec = ArrayList<String>()

        val stm: Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_FRACCIONAMIENTO WHERE ID_FRACCIONAMIENTO=$ID_FRACCIONAMIENTO")
        if (!rs.isBeforeFirst()) {
            Toast.makeText(activity, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs.next()) {
            PAIS = rs.getString("PAIS")
            ESTADO  = rs.getString("ESTADO")
            MUNICIPIO = rs.getString("MUNICIPIO")
            FRACCIONAMIENTO = rs.getString("FRACCIONAMIENTO")
            ETAPA = rs.getString("ETAPA")
        }
        //rs.close()

        textfraccinfo.text = FRACCIONAMIENTO
        textestadoinfo.text = ESTADO
        textetapainfo.text = ETAPA
        textpaisinfo.text = PAIS
        textmunicipioinfo.text = MUNICIPIO

        val rs2: ResultSet = stm.executeQuery("SELECT NOMBRE, AP, NUMEROCEL, SP_DOMICILIO.CALLE, SP_DOMICILIO.NUMERO FROM SP_RESIDENTE LEFT JOIN SP_DOMICILIO ON SP_RESIDENTE.IDDOM = SP_DOMICILIO.IDDOM WHERE SP_DOMICILIO.IDFRACC=$ID_FRACCIONAMIENTO AND SP_RESIDENTE.PERFIL='PRESIDENTE' ORDER BY NOMBRE ASC")
        if (!rs2.isBeforeFirst()) {
            Toast.makeText(activity, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs2.next()) {
            NOMBRE = rs2.getString("NOMBRE")
            AP  = rs2.getString("AP")
            NUMEROCEL = rs2.getString("NUMEROCEL")
            CALLE = rs2.getString("CALLE")
            NUMERO = rs2.getString("NUMERO")
            list_pres.add("$NOMBRE $AP $NUMEROCEL $CALLE $NUMERO")

        }

        val adapter = com.customsoftware.safeapp.adapters.ListView(requireActivity(),list_pres)
        list_pres_fracc.adapter = adapter



        val rs3: ResultSet = stm.executeQuery("SELECT NOMBRE, AP, NUMEROCEL, SP_DOMICILIO.CALLE, SP_DOMICILIO.NUMERO FROM SP_RESIDENTE LEFT JOIN SP_DOMICILIO ON SP_RESIDENTE.IDDOM = SP_DOMICILIO.IDDOM WHERE SP_DOMICILIO.IDFRACC=$ID_FRACCIONAMIENTO AND SP_RESIDENTE.PERFIL='TESORERO' ORDER BY NOMBRE ASC")
        if (!rs3.isBeforeFirst()) {
            Toast.makeText(activity, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs3.next()) {
            NOMBRE = rs3.getString("NOMBRE")
            AP  = rs3.getString("AP")
            NUMEROCEL = rs3.getString("NUMEROCEL")
            CALLE = rs3.getString("CALLE")
            NUMERO = rs3.getString("NUMERO")
            list_tes.add("$NOMBRE $AP $NUMEROCEL $CALLE $NUMERO")

        }

        val adapter2 = com.customsoftware.safeapp.adapters.ListView(requireActivity(),list_tes)
        list_tes_fracc.adapter = adapter2


        val rs4: ResultSet = stm.executeQuery("SELECT NOMBRE, AP, NUMEROCEL, SP_DOMICILIO.CALLE, SP_DOMICILIO.NUMERO FROM SP_RESIDENTE LEFT JOIN SP_DOMICILIO ON SP_RESIDENTE.IDDOM = SP_DOMICILIO.IDDOM WHERE SP_DOMICILIO.IDFRACC=$ID_FRACCIONAMIENTO AND SP_RESIDENTE.PERFIL='SECRETARIO' ORDER BY NOMBRE ASC")
        if (!rs4.isBeforeFirst()) {
            Toast.makeText(activity, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
        }
        while (rs4.next()) {
            NOMBRE = rs4.getString("NOMBRE")
            AP  = rs4.getString("AP")
            NUMEROCEL = rs4.getString("NUMEROCEL")
            CALLE = rs4.getString("CALLE")
            NUMERO = rs4.getString("NUMERO")
            list_sec.add("$NOMBRE $AP $NUMEROCEL $CALLE $NUMERO")

        }

        val adapter3 = com.customsoftware.safeapp.adapters.ListView(requireActivity(),list_sec)
        list_sec_fracc.adapter = adapter3

        list_pres_fracc.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val numeroCel = selectedItem.split(" ")[2] // assuming NUMEROCEL is the 3rd element in the string
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.CALL_PHONE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_CALL_PHONE)

                    // REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$numeroCel")
                startActivity(callIntent)
            }
        }

        list_tes_fracc.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val numeroCel = selectedItem.split(" ")[2] // assuming NUMEROCEL is the 3rd element in the string
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.CALL_PHONE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_CALL_PHONE)

                    // REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$numeroCel")
                startActivity(callIntent)
            }
        }

        list_sec_fracc.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val numeroCel = selectedItem.split(" ")[2] // assuming NUMEROCEL is the 3rd element in the string
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.CALL_PHONE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_CALL_PHONE)

                    // REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$numeroCel")
                startActivity(callIntent)
            }
        }


        return view
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
         * @return A new instance of fragment fraccFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fraccFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CALL_PHONE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    // you can now make the phone call
                } else {
                    // permission was denied
                    // you can show a message to the user or do nothing
                }
                return
            }
        }
    }
}