package com.vikeshpatil.alarm

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class Pop_Time: DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.pop_time, container, false)
        var btnTimeSelected = view.findViewById(R.id.btnTimeSelected) as Button
        var timePicker = view.findViewById(R.id.inputTimePicker) as TimePicker

        btnTimeSelected.setOnClickListener({

            val mainActivity = activity as MainActivity

            if (Build.VERSION.SDK_INT >= 23){
                mainActivity.SetTime(timePicker.hour, timePicker.minute)
            }else{
                mainActivity.SetTime(timePicker.currentHour, timePicker.currentMinute)
            }
            this.dismiss()
        })

        return view
    }
}