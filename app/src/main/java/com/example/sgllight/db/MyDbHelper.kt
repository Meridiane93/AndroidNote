package com.example.sgllight.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
      // создаёт базу данных
class MyDbHelper(context: Context):SQLiteOpenHelper
          (context,MyDbNameClass.DATABASE_NAME,null,MyDbNameClass.DATABASE_VERSION) {

          override fun onCreate(db: SQLiteDatabase?) { // создаёт базу данных
              db?.execSQL(MyDbNameClass.CREAT_TABLE)
          }

          override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { // обновляет базу
              db?.execSQL(MyDbNameClass.SQL_DELETE_TABLE)
              onCreate(db)
          }
      }