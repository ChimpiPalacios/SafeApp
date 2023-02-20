package com.customsoftware.safeapp

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.customsoftware.safeapp.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement
import java.util.*


class MainActivity : AppCompatActivity() {
//view binding
    private lateinit var binding: ActivityMainBinding

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager


    private lateinit var firebaseAuth: FirebaseAuth
//constants
    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG ="GOOGLE_SIGN_IN_TAG"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(binding.root)

        //configure google sign in
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestEmail()
           .build()
        gsc = GoogleSignIn.getClient(this,gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if(acct != null){
            //checkUser()
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }

        /*firebaseAuth = FirebaseAuth.getInstance()
        checkUser()*/

        binding.googleSignInBtn.setOnClickListener{
            Log.d(TAG, "onCreate: begin Google SignIn")
            val intent = gsc.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)

        }

        callbackManager = CallbackManager.Factory.create();

       LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult) {
                TODO("Not yet implemented")
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                finish()
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException) {
                TODO("Not yet implemented")
            }
        }
       )
        binding.fbSignInBtn.setOnClickListener{
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser != null){
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            finish()
        }
        startActivity(Intent(this@MainActivity, Menu::class.java))



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== RC_SIGN_IN){
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                if(account != null){
                    //firebaseAuthWithGoogleAccount(account)
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                }
            }catch (e: Exception){
                Log.d(TAG, "onActivityResult: ${e.message}")

            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val firebaseUser = firebaseAuth.currentUser
                //val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email
                val nombre = firebaseUser!!.displayName

                if (authResult.additionalUserInfo!!.isNewUser){
                    //AGREGAR NUEVO USUARIO A LA BASE DE DATOS
                    addnewUser(account)

                    startActivity(Intent(this@MainActivity, BienvenidoActivity::class.java))

                }else{
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                }
            }
            .addOnFailureListener{e ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Loggin Failed due to ${e.message}")
                Toast.makeText(this@MainActivity, "Loggin Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
        finish()
    }

    private fun addnewUser(account: GoogleSignInAccount?) {
        try {
            val stm: Statement = conexionDB()!!.createStatement()
            val rs: Int = stm.executeUpdate("INSERT INTO SP_USUARIOS (CORREO,NOMBRE) VALUES ('" + account!!.email + "', '"+account!!.displayName +"')")
            if (rs > 0) {
                Toast.makeText(applicationContext, "Insertado correctamente.", Toast.LENGTH_LONG).show()
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun conexionDB(): Connection? {
        var cnn: Connection? = null

        try {
            val politica = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(politica)
            Class.forName("org.gjt.mm.mysql.Driver").newInstance()
            //remotemysql.com:
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
