package com.vikeshpatil.todoapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_ticket.view.*

class MainActivity : AppCompatActivity(), ItemRowListener {

    var todoItemList = ArrayList<TodoItem>()
    var adapter: TodoItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddNewItem = findViewById<View>(R.id.btnAddNewItem) as FloatingActionButton

//        Add new item
        btnAddNewItem.setOnClickListener{view ->
            addNewItemDialog()
        }

        LoadItemsQuery("%")

    }

    private fun addNewItemDialog(){

        val alert = AlertDialog.Builder(this)

        val itemEditText = EditText(this)
        alert.setMessage("Add New Item")
        alert.setTitle("Enter To Do Item ")

        alert.setView(itemEditText)
        alert.setPositiveButton("Add") {dialog, positiveButton ->

            var dbManager = DBManager(this)
            var values = ContentValues()
            values.put("Description", itemEditText.text.toString())
            values.put("isComplete", 0)

            var id = dbManager.Insert(values)

            if (id > 0){
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Unable to add item", Toast.LENGTH_LONG).show()
            }

            dialog.dismiss()
            LoadItemsQuery("%")
        }
        alert.show()
    }

    override fun onResume() {
        super.onResume()
        LoadItemsQuery("%")
    }

     fun LoadItemsQuery(Description: String){

        var dbManager = DBManager(this)
        val selectionArgs = arrayOf(Description)
        val projections = arrayOf("ID", "Description", "isComplete")
        var cursor = dbManager.View(projections, "Description LIKE ?", selectionArgs, "ID")
        todoItemList.clear()

        if (cursor.moveToFirst()){
            do{
                val objectId = cursor.getInt(cursor.getColumnIndex("ID"))
                val description = cursor.getString(cursor.getColumnIndex("Description"))
                val isComplete = cursor.getInt(cursor.getColumnIndex("isComplete"))

                todoItemList.add(TodoItem(objectId, description, IntToBool(isComplete)))
            }while (cursor.moveToNext())
        }

        adapter = TodoItemAdapter(todoItemList, this)
        lvTodoItems.adapter = adapter
    }

    fun IntToBool(value:Int) : Boolean{
        return value == 1
    }

    fun BoolToInt(value: Boolean) : Int{
        return if(value){ 1 }else{ 0 }
    }

    // Function to change status of todo item as complete or incomplete
    override fun ModifyItemState(item: TodoItem, itemObjectId: Int, isDone: Boolean) {

        var dbManager = DBManager(this)
        var selectArgs = arrayOf(itemObjectId.toString())
        var values = ContentValues()
        values.put("isComplete", BoolToInt(isDone))
        val count = dbManager.Update(values, "ID=?", selectArgs)


        if (count > 0){
            if (isDone == true){
                Toast.makeText(this, item.description +" Completed", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this, item.description + " is incomplete", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this, "Failed to mark as complete!", Toast.LENGTH_LONG).show()
        }
        LoadItemsQuery("%")
    }

//    Function to Delete Todo Item
    override fun onItemDelete(itemObjectId: Int) {
        var dbManager = DBManager(this)
        var selectArgs = arrayOf(itemObjectId.toString())
        val count = dbManager.Delete("Id=?", selectArgs)

        if (count > 0){
            Toast.makeText(this, "Item Deleted", Toast.LENGTH_LONG).show()
            adapter!!.notifyDataSetChanged()
            LoadItemsQuery("%")
        }else {
            Toast.makeText(this, "Failed to Delete Item!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){


        }

        return true
    }

    inner class TodoItemAdapter: BaseAdapter{

        var todoItemList = ArrayList<TodoItem>()
        var context:Context? = null
        var rowItemListener: ItemRowListener? = null

        constructor(todoItemList: ArrayList<TodoItem>, context: Context):super(){
            this.todoItemList = todoItemList
            this.context = context
            this.rowItemListener = context as ItemRowListener
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var item = todoItemList[position]

            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var view = inflater.inflate(R.layout.item_ticket, null)
            view.todoItem.text = item.description
            view.chComplete.isChecked = item.isCompleted!!

//          Change status of item as complete or incomplete
            view.chComplete.setOnClickListener{
                rowItemListener!!.ModifyItemState(item, item.objectId!!, !item.isCompleted!!)
            }

//          Delete item
            view.btnDelete.setOnClickListener{
                rowItemListener!!.onItemDelete(item.objectId!!)
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return todoItemList[position]!!
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return todoItemList.size
        }
    }
}
