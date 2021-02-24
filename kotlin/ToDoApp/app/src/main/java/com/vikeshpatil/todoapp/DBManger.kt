package com.vikeshpatil.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DBManager{

    val DB_NAME = "TodoItemsDatabase"
    val TABLE_NAME = "TodoItems"
    val COLUMN_ID = "ID"
    val COLUMN_DESCRIPTION = "Description"
    val COLUMN_IS_COMPLETE = "isComplete"
    val DB_VERION = 1

    val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("+ COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_IS_COMPLETE + " NUMBER(1));"

    var sqlDB:SQLiteDatabase? = null

    constructor(context: Context){
        var db = DatabaseHelper(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelper: SQLiteOpenHelper {
        var context:Context? = null
        constructor(context: Context):super(context!!, DB_NAME, null, DB_VERION){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {

            println("\n" + CREATE_TABLE + "\n")
            db!!.execSQL(CREATE_TABLE)
            Toast.makeText(this.context, "Database is created", Toast.LENGTH_LONG).show()

        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP IF EXISTS " + TABLE_NAME)
        }
    }

    fun Insert(values:ContentValues):Long{

        val ID = sqlDB!!.insert(TABLE_NAME, "", values)
        return ID
    }

    //    View Data
    fun View(projection:Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor{

        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE_NAME
        val cursor = queryBuilder.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder)
        return cursor
    }

    fun Delete(selection: String, selectionArgs: Array<String>): Int{
        val count = sqlDB!!.delete(TABLE_NAME, selection, selectionArgs)
        return count
    }

    fun Update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int{
        val count = sqlDB!!.update(TABLE_NAME, values, selection, selectionArgs)
        return count
    }
}