package com.vikeshpatil.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class myBrodcastReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals("com.vikeshpatil.alarmmanager")){
            var bundle = intent.extras
//            Toast.makeText(context, bundle!!.getString("message"), Toast.LENGTH_LONG).show()
            val notify = Notification()
            bundle!!.getString("message")?.let { notify.Notify(context!!, it, 10) }

        }else if (intent!!.action.equals("android.intent.action.BOOT_COMPLETED")){

            val saveData = SaveData(context!!)
            saveData.setAlarm()
        }
    }
}