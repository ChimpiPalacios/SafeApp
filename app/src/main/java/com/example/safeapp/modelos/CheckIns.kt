package com.example.safeapp.modelos

import java.sql.Date

data class CheckIns (val nombre:String, val vehiculo:String, val placa:String, val credencial:String, val h_entrada: Date, val idcheck:Int, val idresid:Int, val idusu:Int)