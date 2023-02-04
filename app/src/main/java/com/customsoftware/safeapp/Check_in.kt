package com.customsoftware.safeapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.customsoftware.safeapp.adapters.AdapterCheckIn
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.check_in.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class Check_in : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapterCheckIn: AdapterCheckIn
    private lateinit var textFracTab: TextView
    var ID : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.check_in)
        tabLayout = findViewById(R.id.tabLayCheckIn)
        viewPager2 = findViewById(R.id.viewP2)
        adapterCheckIn = AdapterCheckIn(supportFragmentManager,lifecycle)
        viewPager2.adapter = adapterCheckIn

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })


        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        ID = sharedPref.getInt("ID", 0)




        initData()
    }
    private fun initData(){
        textFracTab = findViewById(R.id.textFracTab)
        getData()
    }
    private fun getData(){
        var PAIS : String = ""
        var ESTADO : String = ""
        var MUNICIPIO : String = ""
        var FRACCIONAMIENTO : String = ""
        var ETAPA : String = ""
        val stm:Statement = conexionDB()!!.createStatement()
        val rs: ResultSet = stm.executeQuery("SELECT *  FROM SP_FRACCIONAMIENTO WHERE ID_FRACCIONAMIENTO=$ID")
        if (!rs.isBeforeFirst()){
            Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            return
        }
        while (rs.next()) {
            PAIS = rs.getString("PAIS")
            ESTADO  = rs.getString("ESTADO")
            MUNICIPIO = rs.getString("MUNICIPIO")
            FRACCIONAMIENTO = rs.getString("FRACCIONAMIENTO")
            ETAPA = rs.getString("ETAPA")
        }
        rs.close()
        textFracTab.text = FRACCIONAMIENTO
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

}