package yasuda.ifpr.edu.com.br.task.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_task")
data class Task(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description : String,
    @ColumnInfo(name = "done")
    var done : Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}