package app.aloha.internet.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("postId")
    val id: String,

    val topicId: String,
    val uid: String,
    val postAt: String,
    val title: String,
    val body: String,
    val likeCount: Int,
    val dislikeCount: Int,
    val lastActivity: String,
    val popularity: PostPopularity
)


data class PostPopularity(
    val viewCount: Int,
    val commentCount: Int
)