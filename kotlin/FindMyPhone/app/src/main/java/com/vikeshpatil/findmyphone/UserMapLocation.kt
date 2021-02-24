package com.vikeshpatil.findmyphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import java.lang.Exception

class UserMapLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_map_location)

        val bundle: Bundle? = intent.extras
        val phoneNumber = bundle!!.getString("phoneNumber")
        databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference!!.child("Users").child(phoneNumber!!).child("location").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {


                val treeData = dataSnapshot!!.value as HashMap<String, Any>
                val lat = treeData["lat"].toString()
                val long = treeData["long"].toString()

                UserMapLocation.userLocation = LatLng(lat.toDouble(), long.toDouble())
                UserMapLocation.lastLocated = treeData["Last Located"].toString()

                loadMap()
                }catch (e: Exception){
                    println("error in loading user location" + e.message)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                println("value event listener on cancelled called")
            }
        })

    }

    fun loadMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

    companion object{
        var userLocation = LatLng(-34.0, 151.0)
        var lastLocated = "Unknown"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        mMap.addMarker(MarkerOptions().position(userLocation).title(lastLocated))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10f))
    }
}
