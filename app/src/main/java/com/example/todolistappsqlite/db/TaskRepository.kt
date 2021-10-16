package com.example.todolistappsqlite.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.todolistappsqlite.model.Task

class TaskRepository(var context: Context) {

    private var mDBHelper: DBHelper = DBHelper.getInstance(context)
    //fonksiyon bize bir liste döndürecek:
    fun getAllTask(): ArrayList<Task> {
        val list = ArrayList<Task>()

        //okuma işlemi yapacağımızdan okunabilir database nesnesi oluşturalım
        val db = mDBHelper.readableDatabase

        //query'i yazalım
        val query =
            "SELECT ${DBHelper.KEY_ID}, ${DBHelper.KEY_NAME}, ${DBHelper.KEY_DATE} FROM ${DBHelper.TABLE_NAME}"

        //şimdi de query'i veritabanında işletmemiz gerekiyor.cursor sınıfından bir nesne oluşturup
        //bu sınıf sayesinde veritabanındaki bütün kolonları teker teker okuyabiliyoruz.

        val cursor: Cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID))
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                val date = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DATE))

                //şimdi de db'den tek tek okuyarak oluşturduğumuz Task dataclass nesnesi haline çevirip
                //recyclerview listesine ekleyeceğiz.
                val task = Task(id, name, date)
                list.add(task)
            } while (cursor.moveToNext())
        }
        //db'den okuma bitince cursor ve database'i kapatmamız gerekir !
        cursor.close()
        db.close()

        return list
    }

    fun insertTask(task: Task): Int {

        //yazma işlemi yapacağımızdan yazılabilir database nesnesi oluşturalım
        val db = mDBHelper.writableDatabase

        //veritabanına ekleyeceğimiz veriler için contentvalues sınıfını çağıralım
        val values = ContentValues()
        values.apply {
            //Database'deki KEY_NAME alanına, dataclass'daki name atanırken
            //KEY_DATE alanına, dataclass'daki date atanır
            put(DBHelper.KEY_NAME, task.name)
            put(DBHelper.KEY_DATE, task.date)
        }

        //insert metodundan dönen id'dir. veritabanındaki değildir
        //bu id eğer insert başarısız olursa -1 döner
        val id = db.insert(DBHelper.TABLE_NAME, null, values )

        db.close()
        //sonucu int olarak almak isteriz
        return id.toInt()

    }

    //delete işlemi task id'ye göre where clause alarak gerçekleşir
    fun deleteTask(taskid: Int) {
        val db = mDBHelper.writableDatabase
        db.delete(DBHelper.TABLE_NAME, DBHelper.KEY_ID + "=?", arrayOf(taskid.toString()))
        db.close()

    }

    fun updateTask(task: Task): Int {
        val db = mDBHelper.writableDatabase
        val values = ContentValues()
        values.apply {
            put(DBHelper.KEY_NAME, task.name)
            put(DBHelper.KEY_DATE, task.date)
        }

        val id = db.update(DBHelper.TABLE_NAME, values, DBHelper.KEY_ID + "=?", arrayOf(task.id.toString()))
        return id
    }
}