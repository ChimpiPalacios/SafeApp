package com.customsoftware.safeapp

import android.content.Intent.getIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fraccFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class fraccFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


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

        // Get the values of x and y from the arguments bundle
        val fraccionamiento = arguments?.getString("fraccionamiento")
        val estado = arguments?.getString("estado")
        val etapa = arguments?.getString("etapa")
        val pais = arguments?.getString("pais")
        val municipio = arguments?.getString("municipio")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fracc, container, false)

        // Get the TextViews from the layout and set their text to the values of x and y
        val textfraccinfo = view.findViewById<TextView>(R.id.textfraccinfo)
        val textestadoinfo = view.findViewById<TextView>(R.id.textestadoinfo)
        val textetapainfo = view.findViewById<TextView>(R.id.textetapainfo)
        val textpaisinfo = view.findViewById<TextView>(R.id.textpaisinfo)
        val textmunicipioinfo = view.findViewById<TextView>(R.id.textmunicipioinfo)

       /* textfraccinfo.text = fraccionamiento
        textestadoinfo.text = estado
        textetapainfo.text = etapa
        textpaisinfo.text = pais
        textmunicipioinfo.text = municipio */

        return view


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
}