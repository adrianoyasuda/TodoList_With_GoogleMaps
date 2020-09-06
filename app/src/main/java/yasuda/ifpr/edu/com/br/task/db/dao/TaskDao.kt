package yasuda.ifpr.edu.com.br.task.db.dao

import androidx.room.*
import yasuda.ifpr.edu.com.br.task.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tb_task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM tb_task WHERE id = :id LIMIT 1")
    fun findById(id: Int): Task?

    @Query("SELECT * FROM tb_task WHERE title LIKE :title AND description LIKE :description")
    fun findByName(title: String, description: String): List<Task>

    @Insert
    fun  insert(task: Task): Long

    @Update
    fun  update(task: Task)

    @Delete
    fun remove(task: Task)

}