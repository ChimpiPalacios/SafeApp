package com.customsoftware.safeapp.modelos

import java.sql.Date
import java.sql.Timestamp

data class CheckIns (
    val nombre:String,
    val vehiculo:String,
    val nombreresi:String,
    val hora: Timestamp,
    val idresid:Int
    )