package com.example.sgllight.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

// работает с базой данных добавляет данные удаляет открывает и закрывает
class MyDbManager(context:Context) {
    val myDbHelper = MyDbHelper(context)
    var db:SQLiteDatabase ? = null

    fun openDb(){   // открыть базу данных
        db = myDbHelper.writableDatabase
    }
    fun insertToDb(title:String,content:String, uri:String,time:String){    // записать в базу данных
        val values = ContentValues().apply{ // сохраняем записанное из title and content in values
            put(MyDbNameClass.COLUMN_NAME_TITLE, title) // сохраняет в values то что записано в title
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content) // сохраняет в values то что записано content
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI, uri) // сохраняет в values ссылку на картинку
            put(MyDbNameClass.COLUMN_NAME_TIME, time)
        }
        db?.insert(MyDbNameClass.TABLE_NAME,null,values) // передаём в базу данных то что у нас в values
    }

    // перезаписать базу базу данных
    fun updateItem(title:String,content:String, uri:String, id:Int,time:String){    // записать в базу данных

        val selection = BaseColumns._ID + "=$id"

        val values = ContentValues().apply{ // сохраняем записанное из title and content in values
            put(MyDbNameClass.COLUMN_NAME_TITLE, title) // сохраняет в values то что записано в title
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content) // сохраняет в values то что записано content
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI, uri) // сохраняет в values ссылку на картинку
            put(MyDbNameClass.COLUMN_NAME_TIME, time)
        }
        db?.update(MyDbNameClass.TABLE_NAME,values, selection,null) // передаём в базу данных то что у нас в values
    }
    // считываем записанное с базы данных
    fun removeItemFromDb(id:String){    // удаляем элементы из БД
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME,selection,null) // передаём удаляемый элемент по id
    }

    fun readDbData(searchText:String) : ArrayList<ListItem> { // считать с базы (сюда помещаем только title)
        val dataList = ArrayList<ListItem>()
        val selection = "${MyDbNameClass.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(MyDbNameClass.TABLE_NAME,null,selection, arrayOf("%$searchText%"),null,null,null) // считали данные и поместили в cursor

            while (cursor?.moveToNext()!!){ // берём данные из cursor и записываем в список
                val dataTitle = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE)) // получаем данные из колонны title
                val dataContent = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT)) // получаем данные из колонны контента
                val dataUri = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_IMAGE_URI)) // // получаем данные о картинке
                val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)) // // получаем данные о id
                val time = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TIME))

                val item = ListItem()  // содержит в себе все 4 параметра титл контент и картинка
                item.id = dataId  // присваиваем item id
                item.title = dataTitle // присваиваем item титл
                item.desc = dataContent // присваиваем item контент
                item.uri = dataUri  // присваиваем item картинку
                item.time = time
                dataList.add(item) // помещаем в список
            }
        cursor.close()
        return dataList
    }
    fun closeDb(){ // закрыть базу данных
        myDbHelper.close()
    }
}