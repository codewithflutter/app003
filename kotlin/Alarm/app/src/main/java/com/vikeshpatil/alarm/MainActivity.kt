package com.vikeshpatil.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveData = SaveData(applicationContext)
        tvTime.text = saveData.getHour().toString() + ":" + saveData.getMinute().toString()

    }


    fun SetTimeEvent(view: View){

        val popTime = Pop_Time()
        val transaction = supportFragmentManager.beginTransaction()
        popTime.show(transaction, "Select Time")
//        val fragmentManager = fragmentManager
//        popTime.show(fragmentManager, "Select Time")


    }

    fun SetTime(hours: Int, minutes: Int){
        tvTime.text = hours.toString() + ":" + minutes.toString()

        val saveData = SaveData(applicationContext)
        saveData.SaveData(hours, minutes)
        saveData.setAlarm()
    }
}
