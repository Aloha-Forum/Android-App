package app.aloha.internet.service

import app.aloha.internet.model.Comment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CommentApiService {
    @GET("/api/comment")
    fun get(
        @Query("postId") targetId: String,
        @Query("page") page: Int = 0
    ): Call<List<Comment>>

    @POST("/api/comment")
    fun post(
        @Header("authorization") token: String,
        @Body body: PublishCommentRequest
    ): Call<PublishCommentResponse>
}

data class PublishCommentRequest(
    val postId: String,
    val content: String
)

data class PublishCommentResponse(
    val commentId: String,
)