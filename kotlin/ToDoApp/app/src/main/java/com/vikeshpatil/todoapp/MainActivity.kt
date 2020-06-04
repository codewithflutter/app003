package com.vikeshpatil.todoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_ticket.view.*

class MainActivity : AppCompatActivity() {

    var todoItemList = ArrayList<TodoItem>()
    var adapter: TodoItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoItemList.add(TodoItem("Water Plants"))
        todoItemList.add(TodoItem("Study"))
        todoItemList.add(TodoItem("Visit Park"))

    }

    override fun onResume() {
        super.onResume()
        LoadItemsQuery("%")
    }

    fun LoadItemsQuery(Description: String){

        var dbManager = DBManager(this)
        val selectionArgs = arrayOf(Description)
        val projections = arrayOf("ID", "Description", "isComplete")
        var cursor = dbManager.View(projections, "Description LIKE ?", selectionArgs, "Description")
        todoItemList.clear()

        if (cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val description = cursor.getString(cursor.getColumnIndex("Description"))
                val isComplete = cursor.getInt(cursor.getColumnIndex("isComplete"))

                todoItemList.add(TodoItem(description, isComplete(isComplete)))
            }while (cursor.moveToNext())
        }

        adapter = TodoItemAdapter(todoItemList, applicationContext)
        lvTodoItems.adapter = adapter

    }

    fun isComplete(value:Int) : Boolean{
        return value == 1
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.addItem -> {
                val intent = Intent(this, NewTodoItem::class.java)
                startActivity(intent)
            }

            R.id.done -> {
//                TODO: list of competed todo's
            }
        }

        return true
    }

    class TodoItemAdapter: BaseAdapter{

        var todoItemList = ArrayList<TodoItem>()
        var context:Context? = null

        constructor(todoItemList: ArrayList<TodoItem>, context: Context):super(){
            this.todoItemList = todoItemList
            this.context = context
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var item = todoItemList[position]

            var inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var view = inflater.inflate(R.layout.item_ticket, null)
            view.todoItem.text = item.description

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
