package com.vikeshpatil.tictactoy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var database = FirebaseDatabase.getInstance()
    private var databaseRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

    }

    fun LoginEvent(view: View){

        LoginToFirebase(inputEmail.text.toString(), inputPassword.text.toString())
    }

    fun LoginToFirebase(email:String, password:String){

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG)
                    var currentUser = mAuth!!.currentUser
            //            Save data to database

                    if(currentUser != null){
                        databaseRef.child("Users").child(SplitEmail(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                    }

                    LoadMain()
                }else{
                    Toast.makeText(applicationContext, "Login Failed", Toast.LENGTH_LONG)
                }
        }
    }

    override fun onStart() {
        super.onStart()
        LoadMain()
    }

    fun LoadMain(){
        var currentUser = mAuth!!.currentUser

        if(currentUser != null){

            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)

            startActivity(intent)
        }
    }

    fun SplitEmail(str: String): String{
        return str.split("@")[0]
    }
}
