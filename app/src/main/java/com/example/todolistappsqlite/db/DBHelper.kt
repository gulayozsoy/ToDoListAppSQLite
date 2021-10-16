package com.example.todolistappsqlite.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        //database ile ilgili olan özellikler private belirlenmiş!
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "todolist.db"

        val TABLE_NAME = "table_task"
        //tablomuzdaki sütun adları belirtilir. KEY_ID, primary key'dir ve otomatik artsın.
        val KEY_ID = "id"
        val KEY_NAME = "name"
        val KEY_DATE = "date"

        //Burada amaç bütün uygulama boyunca Tek bir instance yani tek bir DBHelper nesnesi
        //üzerinden tüm uygulamayı götürmek
        //Synchronized yazmanın amacı da tek bir thread üzerinden işlemi yürütebilmek, başka thread
        //leri meşgul etmemeli
        private var mInstance: DBHelper? = null
        @Synchronized fun getInstance(context: Context): DBHelper {
            //önceden bir örnek oluşturulmamışsa
            if(mInstance == null) {
                mInstance = DBHelper(context.applicationContext)
            }

            return mInstance as DBHelper
        }


    }

    //onCreate içerisine tabloları oluşturacağız.SQL sorgusu yazacağız
    override fun onCreate(db: SQLiteDatabase?) = createTable(db)

    //Database versiyonu arttırıldığı zaman yapılacak işlemi anlatır
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    private fun createTable(db:SQLiteDatabase?) {
        //SQL sorgusu ile tablo oluşturulur, query'de tablo yapısı veri tipleri girilir, sorgu çalıştırılır
        val CREATE_TASK_TABLE = "CREATE TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_NAME TEXT, $KEY_DATE TEXT)"
        db?.execSQL(CREATE_TASK_TABLE)
    }
}