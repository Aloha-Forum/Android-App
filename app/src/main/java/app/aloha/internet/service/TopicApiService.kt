package app.aloha.internet.service

import app.aloha.internet.model.Topic
import retrofit2.Call
import retrofit2.http.GET

interface TopicApiService {
    @GET("/api/t")
    fun getTopics(): Call<List<Topic>>
}