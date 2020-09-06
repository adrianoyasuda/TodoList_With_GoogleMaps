package yasuda.ifpr.edu.com.br.task.adapters

import yasuda.ifpr.edu.com.br.task.model.Task

public interface TaskAdapterListener {
    fun taskRemoved(task:Task)
    fun taskInsert(task: Task)
    fun taskUpdate(task: Task)
}