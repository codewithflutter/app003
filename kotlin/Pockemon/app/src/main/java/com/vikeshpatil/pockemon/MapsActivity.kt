package com.vikeshpatil.pockemon

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        LoadPockemon()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


    }

    var ACCESSLOCATION = 123  //location access code returned after granting permission
    fun checkPermission(){

        if(Build.VERSION.SDK_INT >= 23){

            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESSLOCATION)
                return
            }
        }

        GetUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun GetUserLocation(){
        Toast.makeText(this, "Location is on", Toast.LENGTH_LONG).show()

        var userLocation = UserLocationListener()

        var locationManger = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, userLocation) // updates location every 3 ms user moves 3 meter

        var locationThread = LocationThread()
        locationThread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
            ACCESSLOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                }else{
                    Toast.makeText(this, "Can not access location", Toast.LENGTH_LONG).show()

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    var location:Location? = null

    inner class UserLocationListener:LocationListener{

        constructor(){
            location = Location("Start")
            location!!.latitude = 19.4402
            location!!.longitude = 73.311

        }

        override fun onLocationChanged(loc: Location?) {
            location = loc
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(provider: String?) {
            try {
                println("Running onPrividerDisabled")
            }catch (e:Exception){
                println("onPrividerDisabled Exception: " + e.message)
            }
        }
    }

    var oldLocation:Location? = null
    inner class LocationThread:Thread{

        constructor():super(){
            oldLocation = Location("Start")
            oldLocation!!.latitude = 0.0
            oldLocation!!.longitude = 0.0
        }

        override fun run() {
            while (true){
                try {
                    if (oldLocation!!.distanceTo(location) == 0f){
                        continue
                    }

                    oldLocation = location
                        runOnUiThread(){
//                            Show my location
                            mMap.clear()
                            // Add a marker in Sydney and move the camera
                            val sydney = LatLng(location!!.latitude, location!!.longitude)
                            mMap.addMarker(MarkerOptions()
                                .position(sydney)
                                .title("Me")
                                .snippet("Here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))


//                            Show Pockemons

                            for (i in 0..pockemonList.size - 1){
                                var newPockemon = pockemonList[i]
                                if(newPockemon.isCaught == false){
                                    val pockemonLoc = LatLng(newPockemon.location!!.latitude, newPockemon.location!!.longitude)
                                    mMap.addMarker(MarkerOptions()
                                        .position(pockemonLoc)
                                        .title(newPockemon.name)
                                        .snippet(newPockemon.description + ". \nPower = " + newPockemon.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!)))

                                    if(location!!.distanceTo(newPockemon.location) < 2){

                                        newPockemon.isCaught = true
                                        pockemonList[i] = newPockemon
                                        playerPower += newPockemon.power!!
                                        Toast.makeText(applicationContext,
                                            "You catch new pockemon " + newPockemon.name + ". Your power is " + playerPower,
                                            Toast.LENGTH_LONG).show()
                                    }

                                }
                            }
                        }

                    Thread.sleep(1000)
                }catch (ex:Exception){
//                    Toast.makeText(this@MapsActivity, "Applicaiton Crashed", Toast.LENGTH_LONG).show()
                      println("Application Crashed")
                }
            }
        }
    }
    var playerPower = 0.0
    var pockemonList = ArrayList<Pockemon>()

    fun LoadPockemon(){
        pockemonList.add(Pockemon( "charmander", "charmander living in japan", R.drawable.charmander, 55.0, 19.4302, 73.301))
        pockemonList.add(Pockemon( "bulbasaur", "bulbasaur living in usa", R.drawable.bulbasaur, 90.0, 19.433, 73.320))
        pockemonList.add(Pockemon( "squirtle", "squirtle living in Iraq", R.drawable.squirtle, 33.5, 19.4502, 73.321))
    }
}
