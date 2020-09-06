package yasuda.ifpr.edu.com.br.task.api

import retrofit2.Call
import retrofit2.http.*
import yasuda.ifpr.edu.com.br.task.model.Task

interface TaskiService {
    @GET("tasks")
    fun getAll(): Call<List<Task>>

    @GET("tasks/{id}")
    fun get(@Path("id") id: Long): Call<Task>

    @Headers("Content-Type: Application/json")
    @POST("tasks")
    fun insert(@Body task : Task) : Call<Task>

    @Headers("Content-Type: Application/json")
    @PATCH("tasks/{id}")
    fun update(@Path("id") id: Long, @Body task : Task) : Call<Task>

    @DELETE("tasks/{id}")
    fun delete(@Path("id") id: Long) : Call<Void>
}