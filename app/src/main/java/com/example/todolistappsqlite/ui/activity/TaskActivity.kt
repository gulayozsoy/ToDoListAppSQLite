package com.example.todolistappsqlite.ui.activity

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.todolistappsqlite.R
import com.example.todolistappsqlite.db.TaskRepository
import com.example.todolistappsqlite.model.Task
import kotlinx.android.synthetic.main.activity_task.*
import java.util.*

@Suppress("NAME_SHADOWING")
class TaskActivity : AppCompatActivity() {

    private lateinit var taskRepository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        setSupportActionBar(task_toolbar)
        supportActionBar?.title = "Add Todo"

        taskRepository = TaskRepository(this)

        //MainActivity'de uzun tıklamalı Edit butonuna basıldığında
        // güncellenecek task verilerinin intent'le edittext ve date'de alınması:
        if(intent.extras != null) {
            val task: Task = intent.extras!!.getSerializable(MainActivity.EXTRA_TASK) as Task
            task_name_edt.setText(task.name)
            end_date_text.setText(task.date)
        }

        confirm_fab.setOnClickListener{
            //fab'a tıklandığında intent mi yani update mi gelmiş yoksa insert mi ona bakar
            //bunu intent'in boş olup olmamasından anlar
            if(intent.extras != null) {
                val task: Task = intent.extras!!.getSerializable(MainActivity.EXTRA_TASK) as Task
                val rowId = taskRepository.updateTask(Task(task.id, task_name_edt.text.toString(),end_date_text.text.toString()))
                if(rowId > -1) Toast.makeText(this, "Güncellendi", Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, "Güncelleme Başarısız", Toast.LENGTH_SHORT).show()
            }
            else {
                //girilen edit alanı boş değilse(!) yani birşeyler girilmişse
                if(!TextUtils.isEmpty(task_name_edt.text.toString())) {
                    val date: String =
                        if (end_date_text.text == null || end_date_text.text ==  getString(R.string.end_date)) "No End date"
                        else end_date_text.toString()

                    val rowId = taskRepository.insertTask(Task(name = task_name_edt.text.toString(), date = date))

                    if(rowId > -1) Toast.makeText(this, "Eklendi", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this, "Ekleme başarısız!", Toast.LENGTH_SHORT).show()

                } else Toast.makeText(this,"Task adı boş geçilemez", Toast.LENGTH_SHORT).show()
            }
        }
        end_date_layout.setOnClickListener{ getDatePickerDialog()}
    }

    private fun getDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this, { view, year: Any, month: Any, dayOfMonth->
            val endDate = "$dayOfMonth.$month.$year"  //01.01.2020
            end_date_text.text = endDate
        }, year, month, day)

        //Kullanıcı güncel tarihten önceki günleri seçemesin!

        dialog.datePicker.minDate = System.currentTimeMillis()
        dialog.show()
    }
}
