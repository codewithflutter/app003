package com.vikeshpatil.zooinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.animal_info.*

class AnimalInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.animal_info)

    val bundle: Bundle? = intent.extras
    val name = bundle!!.getString("name")
    val des = bundle!!.getString("des")
        val image = bundle.getInt("image")
        IVAnimalInfo.setImageResource(image)
        TVDesInfo.text = des
        TVInfoTitle.text = name
    }
}
