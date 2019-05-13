package com.example.appcoin.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns


private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${DatabaseCoin.CoinEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DatabaseCoin.CoinEntry.COLUMN_COINNAME} TEXT," +
            "${DatabaseCoin.CoinEntry.COLUMN_COUNTRY} TEXT," +
            "${DatabaseCoin.CoinEntry.COLUMN_YEAR} TEXT)"

private const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${DatabaseCoin.CoinEntry.TABLE_NAME}"

class DataBase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "coin.db"
        const val DATABASE_VERSION = 1
    }
}
