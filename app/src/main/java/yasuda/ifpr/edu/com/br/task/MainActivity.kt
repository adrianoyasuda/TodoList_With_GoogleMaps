package yasuda.ifpr.edu.com.br.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import yasuda.ifpr.edu.com.br.task.db.AppDatabase
import yasuda.ifpr.edu.com.br.task.db.dao.TaskDao
import yasuda.ifpr.edu.com.br.task.model.Task
import yasuda.ifpr.edu.com.br.task.adapters.TaskAdapter
import yasuda.ifpr.edu.com.br.task.adapters.TaskAdapterListener
import yasuda.ifpr.edu.com.br.task.api.TaskiService

class MainActivity : AppCompatActivity(), TaskAdapterListener {
    lateinit var taskDao: TaskDao
    lateinit var adapter: TaskAdapter

    lateinit var retrofit: Retrofit
    lateinit var service : TaskiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val db =
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "tb_task.db"
            )
                .allowMainThreadQueries()
                .build()
        taskDao = db.taskDao()

        fab_add.setOnClickListener {
            addCard()
        }
        retrofitSetup()
    }

    private fun addCard(){
        val xy = adapter.addTask()
        list_people.scrollToPosition(xy)
    }

    private fun retrofitSetup() {
        retrofit = Retrofit.Builder()
            .baseUrl("http://10.1.1.103:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(TaskiService::class.java)
        service.getAll()
            .enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.e("Deu Ruim", "Deu Ruim", t)
            }
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                val taski = response.body()
                if (taski != null) {
                    loadData(taski)
                }
            }
        })
    }

    private fun loadData(taski : List<Task>) {
        adapter = TaskAdapter(taski.toMutableList(), this)
        list_people.adapter = adapter
        list_people.layoutManager = LinearLayoutManager(this,
            RecyclerView.VERTICAL, false)
        list_people.itemAnimator = null
    }



    override fun taskInsert(task: Task) {
        service.insert(task)
            .enqueue(object : Callback<Task> {
                override fun onFailure(call: Call<Task>, t: Throwable) {}
                override fun onResponse(call: Call<Task>, response: Response<Task>) {
                    task.id = response.body()!!.id
                }
            })
    }

    override fun taskUpdate(task: Task) {
        service.update(task.id.toLong(), task).enqueue(object : Callback<Task> {
            override fun onFailure(call: Call<Task>, t: Throwable) {}
            override fun onResponse(call: Call<Task>, response: Response<Task>) {}
        })
    }

    override fun taskRemoved(task: Task) {
        //taskDao.remove(task)
        service.delete(task.id.toLong()).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {}
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
        })
    }

}
