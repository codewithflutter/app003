package com.vikeshpatil.noteapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    var notesList = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        notesList.add(Note(1, "Test note 1", "This is just a testing note. Your can ignore it."))
//        notesList.add(Note(2, "Test note 2", "This is just a testing note. Your can ignore it."))
//        notesList.add(Note(3, "Test note 3", "This is just a testing note. Your can ignore it."))


//        Load From database
        LoadQuery("%")   // % for anything

    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")   // % for anything
    }

    fun LoadQuery(title: String){
        var dbManager = DBManager(this)
        val selectionArgs = arrayOf(title)
        val projections = arrayOf(  "ID", "Title", "Description")
        var cursor = dbManager.Query(projections, "Title LIKE ?", selectionArgs, "Title")
        notesList.clear()
        if (cursor.moveToFirst()){
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Desc = cursor.getString(cursor.getColumnIndex("Description"))

                notesList.add(Note(ID, Title, Desc))

            }while (cursor.moveToNext())
        }

        var notesAdapter = NotesAdapter(this, notesList)
        LVNotes.adapter = notesAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = menu!!.findItem(R.id.noteSearch).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                LoadQuery("%" + query + "%")   // % for anything
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item != null){
            when(item.itemId){
                R.id.addNote -> {
                    var intent = Intent(this, NewNote::class.java)
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
    inner class NotesAdapter:BaseAdapter{
        var notesList = ArrayList<Note>()
        var context: Context? = null
        constructor(context: Context, notesList:ArrayList<Note>):super(){
            this.context = context
            this.notesList = notesList
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var myView = layoutInflater.inflate(R.layout.ticket, null)
            var myNote = notesList[position]

            myView.TVTitle.text = myNote.noteTitle
            myView.TVDesc.text = myNote.noteDesc

//            Delete Note
            myView.ivDeleteNote.setOnClickListener(View.OnClickListener {
               var dbManager = DBManager(this.context!!)
                val selectionArgs = arrayOf(myNote.noteId.toString())
                dbManager.Delete("Id=?", selectionArgs)
                LoadQuery("%")
            })
//              Edit Note
            myView.ivEditNote.setOnClickListener(View.OnClickListener {
                GoToEdit(myNote)
            })
            return myView
        }

        override fun getItem(position: Int): Any {
            return notesList[position]
        }

        override fun getItemId(position: Int): Long {

            return position.toLong()
        }

        override fun getCount(): Int {

            return notesList.size
        }
    }

    fun GoToEdit(note: Note){
        var intent = Intent(this, NewNote::class.java)
        intent.putExtra("ID", note.noteId)
        intent.putExtra("Title", note.noteTitle)
        intent.putExtra("Desc", note.noteDesc)
        startActivity(intent)
    }
}
