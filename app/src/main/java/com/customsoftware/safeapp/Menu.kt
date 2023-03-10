package com.customsoftware.safeapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity


class Menu: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        val btn_clientes = findViewById<ImageButton>(R.id.btn_clientes)
        val btn_empleados = findViewById<ImageButton>(R.id.btn_empleados)
        val btn_fraccionamientos = findViewById<ImageButton>(R.id.btn_fraccionamientos)
        val btn_reportes = findViewById<ImageButton>(R.id.btn_reportes)
        val btn_access = findViewById<ImageButton>(R.id.btn_access)


        btn_clientes.setOnClickListener{
            val Intent = Intent(this,Menu_Clientes::class.java)
            startActivity(Intent)
        }
        btn_empleados.setOnClickListener{
            val Intent = Intent(this,Menu_Empleados::class.java)
            startActivity(Intent)
        }
        btn_fraccionamientos.setOnClickListener{
            val Intent = Intent(this,Menu_Fraccionamientos::class.java)
            startActivity(Intent)
        }
        btn_reportes.setOnClickListener{
            val Intent = Intent(this,Menu_Reportes::class.java)
            startActivity(Intent)
        }
        btn_access.setOnClickListener{
            val Intent = Intent(this,Access::class.java)
            startActivity(Intent)
        }
    }






}