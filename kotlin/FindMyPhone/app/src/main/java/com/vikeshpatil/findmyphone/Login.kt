package com.vikeshpatil.findmyphone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun RegisterEvent(view: View){

        val userData = UserData(this)
        userData.savePhone(inputPhone.text.toString())

        finish()

    }
}
