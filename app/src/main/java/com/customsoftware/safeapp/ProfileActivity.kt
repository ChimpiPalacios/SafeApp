package com.customsoftware.safeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.bumptech.glide.Glide
import com.customsoftware.safeapp.databinding.ActivityProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this,gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null){
            val correo = acct.email
            val profielphoto = acct.photoUrl
            binding.emailTv.text = correo
            Glide.with(this).load(profielphoto).into(binding.imvprofile)
            //binding.imvprofile.getur setImageBitmap(acct.photoUrl)
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            //val email = gsc.email
            //binding.emailTv.text = email
        }

        /*firebaseAuth = FirebaseAuth.getInstance()

        checkUser()*/

        binding.logoutBtn.setOnClickListener{
            gsc.signOut().addOnCompleteListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        //firebaseAuth.signOut()
            //checkUser()
        }

        binding.MenuBtn.setOnClickListener{
            startActivity(Intent(this, Menu::class.java))
        }

    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser == null){
            startActivity(Intent(this, Menu::class.java))
            finish()
        }else{
            val email = firebaseUser.email
            binding.emailTv.text = email
        }

    }
}