package com.vikeshpatil.noteapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_note.*
import java.lang.Exception

class NewNote : AppCompatActivity() {

    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)



        try {
            var bundle = intent.extras
            id = bundle!!.getInt("ID", 0)
            if(id != 0){
                eTNoteTitle.setText(bundle.getString("Title"))
                eTDesc.setText(bundle.getString("Desc"))
            }
        }catch (e: Exception){
            println("Error getting values in bundle " + e.message)
        }
    }

    fun AddNewNote(view: View){

        var dbManager = DBManager(this)

        var values = ContentValues()
        values.put("Title", eTNoteTitle.text.toString())
        values.put("Description", eTDesc.text.toString())

        if (id == 0){

            var id = dbManager.Insert(values)

            if(id > 0){
                Toast.makeText(this, "Note is added successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Unable to add note :(", Toast.LENGTH_LONG).show()
            }
        }else{
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.Update(values, "ID=?", selectionArgs)
            if(id > 0){
                Toast.makeText(this, "Note is updated added successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Unable to update note :(", Toast.LENGTH_LONG).show()
            }
        }
    }
}
