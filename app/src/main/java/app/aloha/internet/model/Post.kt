package app.aloha.internet.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("postId")
    val id: String,

    val topicId: String,
    val uid: String,
    val title: String,
    val body: String,
    val postAt: String,
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val lastActivity: String = postAt,
    val commentCount: Int = 0,
    val vote: Int = 0
)