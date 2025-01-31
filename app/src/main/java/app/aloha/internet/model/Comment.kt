package app.aloha.internet.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("commentId")
    val id: String,
    val postId: String,
    val createdAt: String,
    val content: String,
    val uid: String,
//    val likeCount: Int = 0,
//    val dislikeCount: Int = 0,
)