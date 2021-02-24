package com.vikeshpatil.findmyphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ApplicationBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals("android.intent.action.BOOT_COMPLETED")){
            val intent = Intent(context, ApplicationService::class.java)
            context!!.startService(intent)
        }
    }
}