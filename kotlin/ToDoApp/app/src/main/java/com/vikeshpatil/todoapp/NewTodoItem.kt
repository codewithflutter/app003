package com.vikeshpatil.todoapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_new_todo_item.*
import kotlinx.android.synthetic.main.item_ticket.*
import java.lang.Exception

class NewTodoItem : AppCompatActivity() {

    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_todo_item)

        try {
            var bundle: Bundle? = intent.extras
            id = bundle!!.getInt("ID", 0)
            if (id != 0){
                todoItem.setText(bundle.getString("Description"))
            }
        }catch (e: Exception){
            print("Error getting values in bundle" + e.message)
        }
    }

    fun ItemAddEvent(view: View){

        var dbManager = DBManager(this)
        var values = ContentValues()
        values.put("Description", inputNewItem.text.toString())
        values.put("isComplete", 0)

        if (id == 0){
//            Add item to database
            var id = dbManager.Insert(values)

            if (id > 0){
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Unable to add item", Toast.LENGTH_LONG).show()
            }
        }else{
//            Update Do To Item
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.Update(values, "ID=?", selectionArgs)
            if (id > 0){
                Toast.makeText(this, "Item Updated Added Successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Unable to Update Item", Toast.LENGTH_LONG).show()
            }
        }

        finish()
    }
}
