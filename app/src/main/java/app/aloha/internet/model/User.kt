package app.aloha.internet.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("uid")
    val id: String,
    @SerializedName("email")
    val email: String
)
