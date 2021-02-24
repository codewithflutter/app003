package com.vikeshpatil.findmyphone

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_trackers.*
import kotlinx.android.synthetic.main.contact_ticket.view.*
import com.google.firebase.database.DataSnapshot
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    var contactList =  ArrayList<UserContact>()
    var adapter: ContactAdapter? = null
    var databaseRef:DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userData = UserData(this)
        userData.FirstTimeLoadNumber()

        databaseRef = FirebaseDatabase.getInstance().reference

        //        TestingData()
        adapter = ContactAdapter(this, contactList)
        lvContacts.adapter = adapter

        lvContacts.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val userInfo = contactList[position]


//        get Date Time
                val dateFormat = SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
                var date = Date()

                databaseRef!!.child("Users").child(userInfo.phoneNumber!!).child("request").setValue(dateFormat.format(date))

                val intent = Intent(applicationContext, UserMapLocation::class.java)
                intent.putExtra("phoneNumber", userInfo.phoneNumber)
                startActivity(intent)

            }
    }

    override fun onResume() {
        super.onResume()

        val userData = UserData(this)
        if(userData.LoadPhoneNumber() == "empty"){
            return
        }

        refreshUsers()
        if (ApplicationService.isServiceRunning) return
        CheckContactPermission()
        CheckLocationPermission()
    }

    fun refreshUsers(){
        val userData = UserData(this)

        databaseRef!!.child("Users").child(userData.LoadPhoneNumber()).child("Finders").addValueEventListener(object : ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.value is HashMap<*, *>){

                val treeData  = dataSnapshot.value as HashMap<String, Any>

                contactList.clear()

                if (dataSnapshot == null){
                    contactList.add(UserContact("NO_USERS", "NOTHING"))
                    adapter!!.notifyDataSetChanged()
                    return
                }

                for (key in treeData.keys){
                    var name = loadContactList[key]
                    contactList.add(UserContact(name.toString(), key))
                }

//                if (dataSnapshot.exists()){
//                    var name = loadContactList[dataSnapshot.value]
//                    contactList.add(UserContact(name.toString(), dataSnapshot.value as String))
//                }

                adapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                println("On Loading Finders cancelled")
            }

        })
    }
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {

            var inflater = menuInflater
            inflater.inflate(R.menu.menu, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {

            when (item!!.itemId) {
                R.id.addTracker -> {
                    val intent = Intent(this, Trackers::class.java)
                    startActivity(intent)
                }
                R.id.help -> {
//                TODO::
                }

                else -> {
                    return super.onOptionsItemSelected(item)
                }
            }
            return true
        }

    val CONTACT_CODE = 123
    fun CheckContactPermission(){

        if (Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), CONTACT_CODE)
                return
            }
        }

        loadContact()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CONTACT_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContact()
                } else {
                    Toast.makeText(this, "Can not access to contacts", Toast.LENGTH_LONG).show()
                }
            }
            LOCATION_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    Toast.makeText(this, "Can not access to contacts", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    var loadContactList = HashMap<String, String>()
    private fun loadContact() {

        try {

            loadContactList.clear()
            val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
            cursor!!.moveToFirst()

            do {
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                loadContactList[UserData.formatPhoneNumber(phoneNumber)] = name
            }while (cursor.moveToNext())
        }catch (e : Exception){
            println("Error in loading contacts" + e.message)
        }
    }

//    Adapter
        class ContactAdapter : BaseAdapter {
            var contactList = ArrayList<UserContact>()
            var context: Context? = null

            constructor(context: Context, contactList: ArrayList<UserContact>) {
                this.contactList = contactList
                this.context = context
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val userContact = contactList[position]

                if (userContact.name.equals("NO_USERS")){

                    val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val contactView = inflater.inflate(R.layout.no_user, null)

                    return contactView
                }else{

                    val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val contactView = inflater.inflate(R.layout.contact_ticket, null)
                    contactView.tvName.text = userContact.name
                    contactView.tvPhoneNumber.text = userContact.phoneNumber

                    return contactView
                }
            }

            override fun getItem(position: Int): Any {
                return contactList[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getCount(): Int {
                return contactList.size
            }
        }

    val LOCATION_REQUEST_CODE = 124
    fun CheckLocationPermission(){
        try {
            if (Build.VERSION.SDK_INT >= 23){
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
                    return
                }
            }
        }catch (e: Exception){
            println("Error getting location permission " + e.message)
        }

        getUserLocation()
    }

    fun getUserLocation(){

//        Start background service
        if (!ApplicationService.isServiceRunning){
            var intent = Intent(baseContext, ApplicationService::class.java)
            startService(intent)
        }
    }

    }
