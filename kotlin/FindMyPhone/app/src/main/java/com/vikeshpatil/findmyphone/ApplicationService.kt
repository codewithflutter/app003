package com.vikeshpatil.findmyphone

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ApplicationService: Service(){

    var databaseRef: DatabaseReference? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null!!
    }

    override fun onCreate() {
        super.onCreate()
        databaseRef = FirebaseDatabase.getInstance().reference
        isServiceRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//      Run this code in background for longer time

        var userLocation = locationListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, userLocation)

//        Listen to request
        var userData = UserData(this)
        val userPhoneNumber = userData.LoadPhoneNumber()
        databaseRef!!.child("Users").child(userPhoneNumber).child("request").addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                if (ApplicationService.userLocation == null) return
                val dateFormat = SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
                val date = Date()
                databaseRef!!.child("Users").child(userPhoneNumber).child("location").child("lat").setValue(ApplicationService.userLocation!!.latitude)
                databaseRef!!.child("Users").child(userPhoneNumber).child("location").child("long").setValue(ApplicationService.userLocation!!.longitude)
                databaseRef!!.child("Users").child(userPhoneNumber).child("location").child("Last Located").setValue(dateFormat.format(date).toString())
            }

            override fun onCancelled(p0: DatabaseError) {
                println("On Cancelled")
            }
        })

        return Service.START_NOT_STICKY
    }


    companion object{
        var userLocation: Location? = null
        var isServiceRunning = false

    }
    inner class locationListener: LocationListener {

        constructor():super(){
            userLocation = Location("Me")
            userLocation!!.latitude = 0.0
            userLocation!!.longitude = 0.0
        }
        override fun onLocationChanged(location: Location?) {

            userLocation = location

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            println("Location Listener Status changed")
        }

        override fun onProviderEnabled(provider: String?) {

            println("Location Listener on provider enabled")
        }

        override fun onProviderDisabled(provider: String?) {

            println("Location Listener on provider disabled")
        }
    }
}