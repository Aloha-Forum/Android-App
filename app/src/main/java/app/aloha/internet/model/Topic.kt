package app.aloha.internet.model

import com.google.gson.annotations.SerializedName

data class Topic(
    @SerializedName("topicId")
    val id: String,
    val name: String,
    val description: String,
    val popularity: TopicPopularity,
    val lastActivity: Long,
)

data class TopicPopularity(
    val viewCount: Int,
    val postCount: Int
)
