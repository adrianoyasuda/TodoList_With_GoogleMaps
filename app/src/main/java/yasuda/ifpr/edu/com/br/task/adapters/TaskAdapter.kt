package yasuda.ifpr.edu.com.br.task.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_task.view.*
import kotlinx.android.synthetic.main.item_title.view.*
import yasuda.ifpr.edu.com.br.task.R
import yasuda.ifpr.edu.com.br.task.model.Task


class TaskAdapter(
                    private var tasks: MutableList<Task>,
                    private var listener: TaskAdapterListener) :
RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private var taskEditing: Task? = null


    override fun getItemViewType(position: Int): Int {
        val task = tasks[position]

        return if (task ==  taskEditing){
            R.layout.item_task
        } else{
            R.layout.item_title
        }
    }

    fun addTask(): Int {
        val title = ""
        val description = ""
        val task = Task(title, description, false)

        tasks.add(0, task)
        taskEditing = task
        notifyItemInserted(0)
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(viewType, parent, false)
        )

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.fillUI(task)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceType")
        fun fillUI(task: Task) = if (task == taskEditing){
            itemView.txt_title.setText( task.title)
            itemView.txt_description.setText(task.description)

            if(task.id == 0 && tasks.indexOf(task) == 0){
                itemView.bt_Save.setImageResource(R.drawable.ic_save)
                itemView.bt_Remove.setImageResource(R.drawable.ic_cancel)
            }
            else{
                itemView.bt_Save.setImageResource(R.drawable.ic_ok)
                itemView.bt_Remove.setImageResource(R.drawable.ic_delete)
            }

            itemView.bt_Remove.setOnClickListener {
                with(this@TaskAdapter) {
                    val position = tasks.indexOf(task)
                    tasks.removeAt(position)
                    notifyItemRemoved(position)
                    listener.taskRemoved(task)
                }
            }

            itemView.bt_Save.setOnClickListener {
                if (taskEditing != null) {
                    taskEditing?.let { task ->
                        task.title = itemView.txt_title.text.toString()
                        task.description = itemView.txt_description.text.toString()

                        if(task.id == 0) {
                            taskEditing = null
                            listener.taskInsert(task)
                            notifyItemChanged(tasks.indexOf(task))
                        }
                        else {
                            taskEditing = null
                            listener.taskUpdate(task)
                            notifyItemChanged(tasks.indexOf(task))
                        }
                    }
                }
            }

        }
        else{
            itemView.txt_mtitle.text = task.title

            if (task.done){
                val card = itemView as CardView
                itemView.img_share.visibility = View.VISIBLE
                card.setCardBackgroundColor(Color.parseColor("#67ff59"))
            }
            else{
                val card = itemView as CardView
                itemView.img_share.visibility = View.GONE
                card.setCardBackgroundColor(Color.parseColor("#ff3838"))
            }

            itemView.setOnClickListener {
                taskEditing=task
                val position = tasks.indexOf(task)
                notifyItemChanged(position)
            }

            itemView.img_share.setOnClickListener {
                share(itemView ,task)
            }

            itemView.setOnLongClickListener {

                if(!task.done) {
                    val card = itemView
                    itemView.img_share.visibility = View.VISIBLE
                    card.setCardBackgroundColor(Color.parseColor("#67ff59"))
                    task.done = true
                    listener.taskUpdate(task)
                    notifyItemChanged(tasks.indexOf(task))
                }
                else{
                    val card = itemView
                    itemView.img_share.visibility = View.GONE
                    card.setCardBackgroundColor(Color.parseColor("#ff3838"))
                    task.done = false
                    listener.taskUpdate(task)
                    notifyItemChanged(tasks.indexOf(task))
                }
                true
            }
        }
    }


    private fun share(card: CardView ,task: Task) {

        val shareIntent = Intent(Intent.ACTION_SEND)

        with(shareIntent) {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Compartilhar")
            putExtra(Intent.EXTRA_TEXT, "${card.context.getString(R.string.weee)} ${task.title}".toString())

        }
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        card.rootView.context.startActivity(shareIntent)
    }
}