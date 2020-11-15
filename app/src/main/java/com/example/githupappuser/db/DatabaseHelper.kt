package com.example.githupappuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.AVATAR
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.URL
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.example.githupappuser.db.DatabaseContract.UserColumns.Companion.ID

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbgithubapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " ($ID INTEGER PRIMARY KEY," +
                " $AVATAR TEXT NOT NULL," +
                " $USERNAME TEXT NOT NULL," +
                " $URL TEXT NOT NULL)"
    }

}