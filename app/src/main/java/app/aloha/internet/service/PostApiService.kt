package app.aloha.internet.service

import app.aloha.internet.model.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApiService {
    @GET("/api/post")
    fun getPost(@Query("id") id: String): Call<Post>

    @GET("/api/recommend")
    fun getRecommend(@Query("page") page: Int): Call<List<Post>>
}