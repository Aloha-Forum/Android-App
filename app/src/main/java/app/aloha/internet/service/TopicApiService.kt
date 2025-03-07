package app.aloha.internet.service

import app.aloha.internet.model.Post
import app.aloha.internet.model.Topic
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TopicApiService {
    @GET("/api/t")
    fun getTopics(): Call<List<Topic>>

    @GET("/api/t/{id}")
    fun getTopicPosts(@Path("id") id: String): Call<List<Post>>

    @GET("/api/search")
    fun search(@Query("pattern") pattern: String): Call<List<Topic>>
}