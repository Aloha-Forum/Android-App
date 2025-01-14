package app.aloha.internet.service

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("/auth")
    fun loginWithGoogle(@Header("authorization") accessToken: String): Call<Void>
}