package app.aloha.internet.service

import app.aloha.internet.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {
    @POST("/login")
    fun login(@Header("authorization") accessToken: String): Call<User>

    @POST("/signup")
    fun signup(@Header("authorization") accessToken: String, @Query("uid") uid: String): Call<User>
}