package app.aloha.internet.service

import app.aloha.internet.model.User
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("/login")
    fun login(@Header("authorization") accessToken: String): Call<User>

    @POST("/signup")
    fun signup(@Header("authorization") accessToken: String, uid: String): Call<User>
}