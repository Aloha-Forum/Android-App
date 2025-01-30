package app.aloha.internet.service

import app.aloha.internet.model.Post
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PostApiService {
    @GET("/api/post")
    fun getPost(@Query("id") id: String): Call<Post>

    @GET("/api/recommend")
    fun getRecommend(@Query("page") page: Int): Call<List<Post>>

    @POST("/api/post")
    fun publish(
        @Header("authorization") accessToken: String,
        @Body req: PublishRequest
    ): Call<PublishResponse>
}

data class PublishRequest(
    val title: String,
    val content: String,
    val topicId: String,
)

data class PublishResponse(
    val postId: String,
    val postAt: String
)