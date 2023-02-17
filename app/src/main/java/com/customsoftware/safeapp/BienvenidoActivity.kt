package com.customsoftware.safeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class BienvenidoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)

        val inicio = findViewById<Button>(R.id.iniciobtn)

        inicio.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}