package yasuda.ifpr.edu.com.br.task.db

import androidx.room.Database
import androidx.room.RoomDatabase
import yasuda.ifpr.edu.com.br.task.db.dao.TaskDao
import yasuda.ifpr.edu.com.br.task.model.Task

@Database(entities = [Task::class],version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}