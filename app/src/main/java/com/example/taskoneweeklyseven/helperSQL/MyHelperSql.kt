package com.example.taskoneweeklyseven.helperSQL

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class MyHelperSql(context: Context?) :SQLiteOpenHelper(context, "ACDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE DOTAHEROES(_id INTEGER PRIMARY KEY AUTOINCREMENT,VALUE TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}