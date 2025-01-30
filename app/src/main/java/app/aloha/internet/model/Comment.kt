package app.aloha.internet.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("commentId")
    val id: String,
    val postId: String,
    val commentId: String,
    val createdAt: String,
    val content: String,
    val likeCount: Int,
    val dislikeCount: Int,
)