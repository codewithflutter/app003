package com.vikeshpatil.findmyage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        btnGetAge.setOnClickListener{
//
//        }
    }

    fun btnFindAgeClick(view:View){

////            Fire when clicked on GetMyAge
        val inputAge:Int = inputAge.text.toString().toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val userAge = currentYear - inputAge

        if(userAge > 150){
            tvShowAge.text = "Your age is $userAge. Now a days humans don't live that long."
        }else{
            tvShowAge.text = "Your age is $userAge"
        }
    }
}
