package com.example.todolistappsqlite.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistappsqlite.R
import com.example.todolistappsqlite.model.Task

//mainactivity'den varolan context verisini ve veritabanından çekeceğimiz listeyi
//TaskAdapter() parantez içine, costructor parametresi olarak göndereceğiz

class TaskAdapter(var context: Context, var taskList: ArrayList<Task>):
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private lateinit var onTaskCompleteListener: OnTaskCompleteListener
    private lateinit var onTaskEditListener: OnTaskEditListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskName.text = taskList[position].name
        holder.taskDate.text = taskList[position].date

        //başlangıçta checkbox'un görünümü tick'li olmasın
        holder.completeCheckBox.isChecked = false

        holder.completeCheckBox.setOnClickListener{
            onTaskCompleteListener.let {
                it.onTaskComplete(taskList[position].id)
            }
        }

        //itemview, list item'ın kendisi!
        holder.itemView.setOnCreateContextMenuListener { menu, v, menuInfo ->
            //Edit'e tıklandığında gerçekleşecekler yer alır
            menu.add("Edit").setOnMenuItemClickListener {
                //let ile null'dan farklıysa şartı getirilir'
                onTaskEditListener.let {
                    it.onEditTask(taskList[position])
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    fun updateList(newList: ArrayList<Task>) {

        //ana aktiviteden çağrılırsa eski liste silinip yeni liste eklenir
        taskList.clear()
        taskList.addAll(newList)
        //recyclerview'in yeni listeden haberdar olması sağlanır
        notifyDataSetChanged()
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val taskName = view.findViewById<TextView>(R.id.item_task_name)
        val taskDate = view.findViewById<TextView>(R.id.item_date)
        val completeCheckBox = view.findViewById<CheckBox>(R.id.item_complete)
    }

    //fonksiyonun parametresi olarak vereceğimiz değer ile sınıftaki değer eşitlendi.
    fun setOnTaskCompleteListener(onTaskCompleteListener: OnTaskCompleteListener){
        this.onTaskCompleteListener = onTaskCompleteListener
    }

    //OnTaskEditListener interface'ini methodla alırız.
    fun setOnTaskEditListener(onTaskEditListener: OnTaskEditListener) {
        this.onTaskEditListener = onTaskEditListener
    }

    interface OnTaskCompleteListener {
        fun onTaskComplete(taskId: Int)
    }

    interface OnTaskEditListener {
        fun onEditTask(task: Task)
    }
}