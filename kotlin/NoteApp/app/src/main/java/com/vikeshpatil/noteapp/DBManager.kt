package com.vikeshpatil.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DBManager{

    val dbName = "NotesDatabase"
    val tableName = "Notes"
    val colId = "ID"
    val colTitle = "Title"
    val colDesc = " Description"
    val dbVersion = 1

    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + colId + " INTEGER PRIMARY KEY, "+ colTitle + " TEXT, " + colDesc + " TEXT);"

    var sqlDB:SQLiteDatabase? = null

    constructor(context: Context){
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes: SQLiteOpenHelper {
        var context:Context? = null
        constructor(context: Context):super(context, dbName, null, dbVersion){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {

            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "Database is created", Toast.LENGTH_LONG).show()

        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP IF EXISTS " + tableName)
        }
    }

    fun Insert(values:ContentValues):Long{

        val ID = sqlDB!!.insert(tableName, "", values)
        return ID
    }

//    View Data
    fun Query(projection:Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor{

        val query = SQLiteQueryBuilder()
        query.tables = tableName
        val cursor = query.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder)
        return cursor
    }

    fun Delete(selection: String, selectionArgs: Array<String>): Int{
        val count = sqlDB!!.delete(tableName, selection, selectionArgs)
        return count
    }

    fun Update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int{

        val count = sqlDB!!.update(tableName, values, selection, selectionArgs)
        return count
    }
}