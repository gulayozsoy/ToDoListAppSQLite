package com.example.todolistappsqlite.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistappsqlite.R
import com.example.todolistappsqlite.db.TaskRepository
import com.example.todolistappsqlite.model.Task
import com.example.todolistappsqlite.ui.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskCompleteListener, TaskAdapter.OnTaskEditListener {

    private lateinit var taskRepository: TaskRepository
    private lateinit var taskList: ArrayList<Task>
    private lateinit var adapter: TaskAdapter

    companion object {
        const val EXTRA_TASK = "extra task"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        supportActionBar?.title = "TODO-List"

        //TaskRepository nesnesini oluşturalım
        taskRepository = TaskRepository((this))

        taskList = taskRepository.getAllTask()

        task_recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(this, taskList)
        task_recyclerview.adapter = adapter

        //her bir liste item'ını çizgi ile bölmek için aşgdaki işlem uygulanır
        task_recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //adapter'ü setOnTaskCompleteListener'a bağlayarak burada yapılan bir SetOnClickListener başlatılır.
        adapter.setOnTaskCompleteListener(this)
        adapter.setOnTaskEditListener(this)

        //+ fab'ına tıklanınca TaskActivity'e geçiş yapılır
        add_task_fab.setOnClickListener { startActivity(Intent(this, TaskActivity::class.java))}
    }

    override fun onTaskComplete(taskId: Int) {
        taskRepository.deleteTask(taskId)
        //sildikten sonra güncel listeyi taskList'e atadık
        taskList = taskRepository.getAllTask()
        //recyclerview'in haberdar olmasını sağladık
        adapter.updateList(taskList)
    }

    //Edit butonuna tıklandığında yeni bir activity başlatacağız
    //task'i nesne olarak TaskActivity'e göndermemiz gerekir.
    //nesne gönderimi için model class'ında değişiklikler yapılır
    override fun onEditTask(task: Task) {
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(EXTRA_TASK, task)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        taskList = taskRepository.getAllTask()

        //TaskAdapter'e 3 boyutlu task array listesini
        // silip güncelleyen updateList metodu eklenir:
        adapter.updateList(taskList)
    }
}
