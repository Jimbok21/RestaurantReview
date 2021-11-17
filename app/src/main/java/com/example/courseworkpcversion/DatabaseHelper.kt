package com.example.courseworkpcversion

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

//A class to read and write user log in data into a database
class DatabaseHelper(context: Context):SQLiteOpenHelper(context, dbname, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        //uses an SQL command to create a database to store user information
        val query = "CREATE TABLE USER(username TEXT,password TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insertData(name:String, password:String) {
        //adds data to the database
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        values.put("username", name)
        values.put("password", password)
        Log.d("Success", "Details Added Successfully")
        db.insert("USER", null, values)
        db.close()
    }

    fun validUserCheck(username: String, password: String):Boolean {
        //checks if data is in the database
        val db = writableDatabase
        val query = "select * from user where username = '$username' and password = '$password'"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    companion object {
        private val dbname = "userDB"
        private val factory = null
        private val version = 1
    }
}