package com.example.safeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.safeapp.adapters.AdapterCheckIn
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.check_in.*

class Check_in : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapterCheckIn: AdapterCheckIn
    private lateinit var textFracTab: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        initData()
    }

    private fun initData(){
        textFracTab = findViewById(R.id.textFracTab)
        getData()
    }

    private fun getData(){
        val fraccionamiento = intent.getStringExtra("fraccionamiento")


        //textFracTab.text = fraccionamiento

    }
}