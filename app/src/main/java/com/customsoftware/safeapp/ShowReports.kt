package com.customsoftware.safeapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.TypedValue
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.sql.*
import java.text.SimpleDateFormat
import java.util.*

class ShowReports : AppCompatActivity(){
    private lateinit var txt_fechaIni_showreport : EditText
    private lateinit var txt_fechaFin_showreport : EditText
    private lateinit var txt_horaIni_showreport : EditText
    private lateinit var txt_horaFin_showreport : EditText
    private lateinit var table_layout : TableLayout

    private var ID: Int = 0
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val simpledateF = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var ready = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.show_report)

        val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
        ID = sharedPref.getInt("ID", 0)

        initData()
    }

    private fun initData(){

        txt_fechaIni_showreport = findViewById(R.id.txt_fechaIni_showreport)
        txt_fechaFin_showreport = findViewById(R.id.txt_fechaFin_showreport)
        txt_horaIni_showreport = findViewById(R.id.txt_horaIni_showreport)
        txt_horaFin_showreport = findViewById(R.id.txt_horaFin_showreport)
        table_layout = findViewById(R.id.table_layout)

        getData()
    }

    private fun getData(){
        txt_fechaIni_showreport.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)

                val formattedDate = simpledateF.format(calendar.time)
                    txt_fechaIni_showreport.setText(formattedDate)
                    LoadCheckins()

            }, year, month, day)

            datePickerDialog.show()
        }

        txt_fechaFin_showreport.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)


                val formattedDate = simpledateF.format(calendar.time)
                    txt_fechaFin_showreport.setText(formattedDate)
                    LoadCheckins()

            }, year, month, day)

            datePickerDialog.show()
        }



        txt_horaIni_showreport.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val formattedTime = timeFormat.format(calendar.time)
                txt_horaIni_showreport.setText(formattedTime)


                                LoadCheckins()


            }, hour, minute, true)

            timePickerDialog.show()
        }

        txt_horaFin_showreport.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val formattedTime = timeFormat.format(calendar.time)
                txt_horaFin_showreport.setText(formattedTime)


                            LoadCheckins()

            }, hour, minute, true)

            timePickerDialog.show()
        }

    }

    private fun LoadCheckins() {
        val fechaIni = txt_fechaIni_showreport.text.toString()
        val fechaFin = txt_fechaFin_showreport.text.toString()
        val horaIni = txt_horaIni_showreport.text.toString()
        val horaFin = txt_horaFin_showreport.text.toString()
        // Get the UI mode manager
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
// Check if the UI mode is in night mode
        val isNightMode = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES

        if (fechaIni.isNotEmpty() && fechaFin.isNotEmpty()){
            val timestampIni = Timestamp(simpledateF.parse("$fechaIni").time)
            val timestampFin = Timestamp(simpledateF.parse("$fechaFin").time)
            if(timestampFin < timestampIni){
                Toast.makeText(this, "La Fecha Inicial no puede ser mayor a la Fecha Final", Toast.LENGTH_SHORT).show()
                txt_fechaFin_showreport.setText("")
            }
        }
        if (fechaIni.isNotEmpty() && fechaFin.isNotEmpty() && horaIni.isNotEmpty() && horaFin.isNotEmpty()){
            val timestampIni = Timestamp(dateFormat.parse("$fechaIni $horaIni").time)
            val timestampFin = Timestamp(dateFormat.parse("$fechaFin $horaFin").time)
            if(timestampFin < timestampIni){
                Toast.makeText(this, "La Fecha Inicial no puede ser mayor a la Fecha Final", Toast.LENGTH_SHORT).show()
                txt_horaFin_showreport.setText("")
            }else{
                ready = true
            }

        }

        if (fechaIni.isNotEmpty() && fechaFin.isNotEmpty() && horaIni.isNotEmpty() && horaFin.isNotEmpty() && ready == true) {
            val timestampIni = Timestamp(dateFormat.parse("$fechaIni $horaIni").time)
            val timestampFin = Timestamp(dateFormat.parse("$fechaFin $horaFin").time)


            val stm: Statement = conexionDB()!!.createStatement()
           //val rs: ResultSet = stm.executeQuery("SELECT * FROM SP_CHECKIN WHERE IDFRACC=$ID AND HORA BETWEEN '$timestampIni' AND '$timestampFin'")
            val rs: ResultSet = stm.executeQuery("SELECT SP_CHECKIN.IDCHECK, SP_CHECKIN.HORA, SP_CHECKIN.NOMBRE, SP_CHECKIN.VEHICULO, SP_DOMICILIO.CALLE, SP_DOMICILIO.NUMERO, SP_CHECKIN.NOMBRERESI FROM SP_CHECKIN LEFT JOIN SP_RESIDENTE ON SP_CHECKIN.IDRESID = SP_RESIDENTE.IDRESIDENTE LEFT JOIN SP_DOMICILIO ON SP_RESIDENTE.IDDOM = SP_DOMICILIO.IDDOM WHERE SP_CHECKIN.IDFRACC=$ID AND SP_CHECKIN.HORA BETWEEN '$timestampIni' AND '$timestampFin' ")

            if (!rs.isBeforeFirst()) {
                Toast.makeText(this, "NO SE ENCONTRARON REGISTROS", Toast.LENGTH_SHORT).show()
            } else {
                // Create header row
                val headerRow = TableRow(this)

                val horaHeader = TextView(this)
                horaHeader.text = " HORA"
                horaHeader.setTypeface(null, Typeface.BOLD)
                horaHeader.setBackgroundResource(R.drawable.rounded_corner_right)
                headerRow.addView(horaHeader)

                val nombreHeader = TextView(this)
                nombreHeader.text = " NOMBRE VISITANTE"
                nombreHeader.setTypeface(null, Typeface.BOLD)
                nombreHeader.setBackgroundResource(R.drawable.table_header)
                headerRow.addView(nombreHeader)

                val vehiculoHeader = TextView(this)
                vehiculoHeader.text = " VEHICULO"
                vehiculoHeader.setTypeface(null, Typeface.BOLD)
                vehiculoHeader.setBackgroundResource(R.drawable.table_header)
                headerRow.addView(vehiculoHeader)

                val calleHeader = TextView(this)
                calleHeader.text = " CALLE  "
                calleHeader.setTypeface(null, Typeface.BOLD)
                calleHeader.setBackgroundResource(R.drawable.table_header)
                headerRow.addView(calleHeader)

                val numeroHeader = TextView(this)
                numeroHeader.text = " NUMERO  "
                numeroHeader.setTypeface(null, Typeface.BOLD)
                numeroHeader.setBackgroundResource(R.drawable.table_header)
                headerRow.addView(numeroHeader)

                val nombreResiHeader = TextView(this)
                nombreResiHeader.text = " NOMBRE RESIDENTE"
                nombreResiHeader.setTypeface(null, Typeface.BOLD)
                nombreResiHeader.setBackgroundResource(R.drawable.rounded_corner_left)
                headerRow.addView(nombreResiHeader)

                table_layout.addView(headerRow)

                while (rs.next()) {
                    val row = TableRow(this)

                    val params = TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(resources.getDimensionPixelSize(R.dimen.dp_5), resources.getDimensionPixelSize(R.dimen.dp_5), resources.getDimensionPixelSize(R.dimen.dp_5), resources.getDimensionPixelSize(R.dimen.dp_5))

                    val horaTextView = TextView(this)
                    horaTextView.text = rs.getTimestamp("HORA").toString()
                    // Set the row background colors based on the UI mode
                    when {
                        isNightMode -> {
                            horaTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_dark else R.color.light_darkgray)
                        }
                        else -> {
                            horaTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_gray else R.color.light_white)
                        }
                    }
                    horaTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                    horaTextView.layoutParams = params
                    row.addView(horaTextView)


                    val nombreTextView = TextView(this)
                    nombreTextView.text = rs.getString("NOMBRE")
                    // Set the row background colors based on the UI mode
                    when {
                        isNightMode -> {
                            nombreTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_dark else R.color.light_darkgray)
                        }
                        else -> {
                            nombreTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_gray else R.color.light_white)
                        }
                    }
                    nombreTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                    nombreTextView.layoutParams = params
                    row.addView(nombreTextView)

                    val vehiculoTextView = TextView(this)
                    vehiculoTextView.text = rs.getString("VEHICULO")
                    // Set the row background colors based on the UI mode
                    when {
                        isNightMode -> {
                            vehiculoTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_dark else R.color.light_darkgray)
                        }
                        else -> {
                            vehiculoTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_gray else R.color.light_white)
                        }
                    }
                    vehiculoTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                    vehiculoTextView.layoutParams = params
                    row.addView(vehiculoTextView)

                    val calleTextView = TextView(this)
                    calleTextView.text = rs.getString("CALLE")
                    // Set the row background colors based on the UI mode
                    when {
                        isNightMode -> {
                            calleTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_dark else R.color.light_darkgray)
                        }
                        else -> {
                            calleTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_gray else R.color.light_white)
                        }
                    }
                    calleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                    calleTextView.layoutParams = params
                    row.addView(calleTextView)

                    val numeroTextView = TextView(this)
                    numeroTextView.text = rs.getString("NUMERO")
                    // Set the row background colors based on the UI mode
                    when {
                        isNightMode -> {
                            numeroTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_dark else R.color.light_darkgray)
                        }
                        else -> {
                            numeroTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_gray else R.color.light_white)
                        }
                    }
                    numeroTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                    numeroTextView.layoutParams = params
                    row.addView(numeroTextView)

                    val nombreResiTextView = TextView(this)
                    nombreResiTextView.text = rs.getString("NOMBRERESI")
                    // Set the row background colors based on the UI mode
                    when {
                        isNightMode -> {
                            nombreResiTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_dark else R.color.light_darkgray)
                        }
                        else -> {
                            nombreResiTextView.setBackgroundResource(if (table_layout.childCount % 2 == 0) R.color.light_gray else R.color.light_white)
                        }
                    }
                    nombreResiTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
                    nombreResiTextView.layoutParams = params
                    row.addView(nombreResiTextView)



                    // Apply header style to the first row
                    if (table_layout.childCount == 1) {
                        val headerRow = table_layout.getChildAt(0) as TableRow
                        for (i in 0 until headerRow.childCount) {
                            val headerView = headerRow.getChildAt(i) as TextView
                            headerView.setTextAppearance(R.style.TableHeaderStyle)
                            headerView.setBackgroundResource(R.color.light_blue)
                            headerView.setTypeface(null, Typeface.BOLD)
                        }
                    }

                    // Alternate row colors between light white and light gray
                    if (table_layout.childCount % 2 == 0) {
                        when {
                            isNightMode -> {
                                row.setBackgroundResource(R.color.light_dark)
                            }
                            else -> {
                                row.setBackgroundResource(R.color.light_gray)
                            }
                        }

                    } else {
                        when {
                            isNightMode -> {
                                row.setBackgroundResource(R.color.light_darkgray)
                            }
                            else -> {
                                row.setBackgroundResource(R.color.light_white)
                            }
                        }

                    }

                    // Set the IDCHECK value as a tag on the row
                    row.tag = rs.getInt("IDCHECK")

                    // Set an OnClickListener on the row
                    row.setOnClickListener {
                        val idCheck = it.tag as Int
                        val originalColor = (it.background as ColorDrawable).color

                        it.setBackgroundColor(ContextCompat.getColor(this, R.color.light_blue))

                        // Change text color of TextView in the row
                        for (i in 0 until row.childCount) {
                            val view = row.getChildAt(i)
                            if (view is TextView) {
                                view.setBackgroundColor(ContextCompat.getColor(this, R.color.light_blue))
                            }
                        }

                        // Add a delay of 500ms before starting the new activity
                        Handler(Looper.getMainLooper()).postDelayed({
                            // Start a new activity with the IDCHECK value
                            val sharedPref = this.getSharedPreferences("id_data", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putInt("IDCHECK", idCheck)
                            editor.apply()

                            val intent = Intent(this, ShowCheckIn::class.java)
                            startActivity(intent)
                        }, 500)

                        Handler(Looper.getMainLooper()).postDelayed({
                        // Revert the row to its original color
                        it.setBackgroundColor(originalColor)
                        for (i in 0 until row.childCount) {
                            val view = row.getChildAt(i)
                            if (view is TextView) {
                                view.setBackgroundColor(originalColor)
                            }
                        }
                        }, 500)
                    }

                    table_layout.addView(row)
                }
            }
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
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
        return cnn
    }
}