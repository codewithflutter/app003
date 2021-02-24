package com.vikeshpatil.findmyphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
import java.util.*

class Login : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        signInAnonymously()
    }

    private fun signInAnonymously() {
        mAuth!!.signInAnonymously().addOnCompleteListener(this) {task ->
            if (task.isSuccessful){
//                Sign in successful
                Toast.makeText(applicationContext, "Authentication Success.", Toast.LENGTH_LONG).show()
                val user = mAuth!!.currentUser

            }else{
                Toast.makeText(applicationContext, "Authentication Failed.", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun RegisterEvent(view: View){

        val userData = UserData(this)
        userData.savePhone(inputPhone.text.toString())

//        get Date Time
        val dateFormat = SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
        var date = Date()

//        Save to Firebase
        val databaseInstance = FirebaseDatabase.getInstance()

        val databaseReference = databaseInstance.reference
        databaseReference.child("Users").child(inputPhone.text.toString()).child("request").setValue(dateFormat.format(date))
        databaseReference.child("Users").child(inputPhone.text.toString()).child("Finders").setValue(dateFormat.format(date))
        finish()

    }
}
