package com.vikeshpatil.findmyphone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class UserData {

    var context: Context? = null
    var sharedPreferences: SharedPreferences? = null

    constructor(context: Context){
        this.context = context
        this.sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE)
    }

    fun savePhone(phoneNumber: String){
        var editor = sharedPreferences!!.edit()
        editor.putString("phoneNumber", phoneNumber)
        editor.commit()
    }

    fun LoadPhoneNumber():String{
        val phoneNumber = sharedPreferences!!.getString("phoneNumber", "empty")

        return phoneNumber!!
    }

    fun FirstTimeLoadNumber():String{
        val phoneNumber = sharedPreferences!!.getString("phoneNumber", "empty")
        if (phoneNumber.equals("empty")){
            val intent = Intent(context, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(intent)
        }

        return phoneNumber!!
    }

    companion object{
        var trackers: MutableMap<String, String> = HashMap()

        fun formatPhoneNumber(phoneNumber:String): String{
            var onlyNumber = phoneNumber.replace("[^0-9]".toRegex(), "")
            if(phoneNumber[0] == '+'){
                onlyNumber = "+" + onlyNumber
            }
            return onlyNumber
        }
    }

    fun SaveContactInfo(){
        var trackersList = ""

        for ((key, value) in trackers){
            if(trackersList.length == 0){
                trackersList = key + "%" + value
            }else{
                trackersList += "%$key%$value"
            }
        }

        if (trackersList.length == 0){
            trackersList = "empty"
        }

        var editor = sharedPreferences!!.edit()
        editor.putString("trackersList", trackersList)
        editor.commit()
    }

    fun LoadContactInfo(){
        trackers.clear()
        val trackersList = sharedPreferences!!.getString("trackersList", "empty")

        if(!trackersList.equals("empty")){
            val userInfo = trackersList!!.split("%").toTypedArray()
            var i = 0
            while (i < userInfo.size){
                trackers.put(userInfo[i], userInfo[i+1])
                i += 2
            }
        }

    }

}