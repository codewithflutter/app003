package com.vikeshpatil.findmyphone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userData = UserData(this)
        userData.LoadPhoneNumber()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        var inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
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
}
