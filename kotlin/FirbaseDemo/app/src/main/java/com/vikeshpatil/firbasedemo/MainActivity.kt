package com.vikeshpatil.firbasedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

//    Authentication
        mAuth = FirebaseAuth.getInstance()

    }

    fun loginEvent(view: View){
//      Fire when clicked login
        var email = inputEmail.text.toString()
        var password = inputPassword.text.toString()

        FirebaseLogin(email, password)
    }

    fun FirebaseLogin(email: String, password:String){
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){   task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()
                    var currenUser = mAuth!!.currentUser
                    Log.d("Login: ", currenUser!!.uid)
                }else{
                    Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()
                }
            }
    }


    override fun onStart() {
        super.onStart()
        var currentUser = mAuth!!.currentUser
        if (currentUser != null){
            val intent = Intent(this, Control::class.java)
            startActivity(intent)
        }

    }
}
