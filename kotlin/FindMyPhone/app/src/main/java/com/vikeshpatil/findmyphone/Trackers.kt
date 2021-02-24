package com.vikeshpatil.findmyphone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.provider.ContactsContract
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_trackers.*
import kotlinx.android.synthetic.main.contact_ticket.view.*
import java.sql.Ref

class Trackers : AppCompatActivity() {

    var contactList =  ArrayList<UserContact>()
    var adapter: ContactAdapter? = null
    var userData: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trackers)
        userData = UserData(applicationContext)

//        TestingData()
        adapter = ContactAdapter(this, contactList)
        lvContacts.adapter = adapter

        lvContacts.onItemClickListener = AdapterView.OnItemClickListener{
            parent, view, position, id ->
            val userInfo = contactList[position]
            UserData.trackers.remove(userInfo.phoneNumber)
            RefreshActivity()

//            Save to sharedReference
            userData!!.SaveContactInfo()

//
//          Remove from realtime database
            val databaseReference = FirebaseDatabase.getInstance().reference
            val userData = UserData(applicationContext)
            databaseReference.child("Users").child(userData.LoadPhoneNumber()).child("Finders").child(userInfo.phoneNumber!!).removeValue()

        }

        userData!!.LoadContactInfo()
        RefreshActivity()
    }

    fun TestingData(){

        contactList.add(UserContact("Vikesh Patil", "12354123"))
        contactList.add(UserContact("Karan Patil", "52341234"))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        var inflater = menuInflater
        inflater.inflate(R.menu.tracker_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            R.id.done -> {
                finish()
            }
            R.id.addContact -> {
                CheckContactPermission()
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

        PickContact()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CONTACT_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PickContact()
                } else {
                    Toast.makeText(this, "Can not access to contacts", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                }
        }
    }

    val PICK_CODE = 1234
    private fun PickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            PICK_CODE -> {
                if (resultCode == Activity.RESULT_OK){
                    val contactData = data!!.data
                    val cR= contentResolver.query(contactData!!, null, null, null, null)

                    if (cR!!.moveToFirst()){
                        val id = cR!!.getString(cR.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                        val hasPhone = cR!!.getString(cR.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                        if (hasPhone == "1"){
                            val phone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                                null, null)

                            phone!!.moveToFirst()
                            var phoneNumber = phone.getString(phone.getColumnIndex("data1"))
                            val name= cR!!.getString(cR.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            phoneNumber = UserData.formatPhoneNumber(phoneNumber)
                            UserData.trackers.put(phoneNumber, name)

                            RefreshActivity()
//                            Save to shared reference
                            userData!!.SaveContactInfo()

//                            Save to realtime database
                            val databaseReference = FirebaseDatabase.getInstance().reference
                            val userData = UserData(applicationContext)
                            databaseReference.child("Users").child(userData.LoadPhoneNumber()).child("Finders").child(phoneNumber).setValue(true)

                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun RefreshActivity(){
        contactList.clear()

        for ((key, value) in UserData.trackers){
            contactList.add(UserContact(value, key))
        }
        adapter!!.notifyDataSetChanged()
    }

    class ContactAdapter: BaseAdapter{
        var contactList = ArrayList<UserContact>()
        var context: Context? = null
        constructor(context: Context, contactList: ArrayList<UserContact>){
            this.contactList = contactList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val userContact = contactList[position]
            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val contactView = inflater.inflate(R.layout.contact_ticket, null)
            contactView.tvName.text = userContact.name
            contactView.tvPhoneNumber.text = userContact.phoneNumber

            return contactView
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


}
