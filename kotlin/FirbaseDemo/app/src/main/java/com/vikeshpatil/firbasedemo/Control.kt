package com.vikeshpatil.firbasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class Control : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)


    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(applicationContext, "Automatic Login Successfull", Toast.LENGTH_LONG)

    }
}
