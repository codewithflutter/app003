package com.vikeshpatil.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import java.util.*

class SaveData {

    var context:Context? = null
    var sharedPreferences:SharedPreferences? = null

    constructor(context: Context){
        this.context = context
        this.sharedPreferences = context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
    }

    fun SaveData(hour: Int, minute: Int){

        var editor = sharedPreferences!!.edit()
        editor.putInt("hour", hour)
        editor.putInt("minute", minute)
        editor.commit()
    }

    fun getHour():Int {
        return sharedPreferences!!.getInt("hour", 0)
    }

    fun getMinute():Int{
        return sharedPreferences!!.getInt("minute", 0)
    }

    fun setAlarm(){
        val hour:Int = getHour()
        val minute:Int = getMinute()

        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, minute)
        calender.set(Calendar.SECOND, 0)

        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intent = Intent(context, myBrodcastReceiver::class.java)
        intent.putExtra("message", "alarm time")
        intent.action = "com.vikeshpatil.alarmmanager"

        val pendingIntent = PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calender.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

    }
}