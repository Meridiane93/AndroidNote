package com.example.sgllight.db

import android.provider.BaseColumns

object MyDbNameClass {
    const val TABLE_NAME = "my_table" // название таблицы
    const val COLUMN_NAME_TITLE = "title" // колонка 1
    const val COLUMN_NAME_CONTENT = "content" // колонка 2
    const val COLUMN_NAME_IMAGE_URI = "uri" // колонка 3
    const val COLUMN_NAME_TIME = "time"

    const val DATABASE_VERSION = 2 // версия
    const val DATABASE_NAME = "MyLessonDb.db" // название базы данных

    // создание таблицы внутри базы данных
    const val CREAT_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_NAME_TITLE TEXT, " +
            "$COLUMN_NAME_CONTENT TEXT, " +
            "$COLUMN_NAME_IMAGE_URI TEXT, $COLUMN_NAME_TIME TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}