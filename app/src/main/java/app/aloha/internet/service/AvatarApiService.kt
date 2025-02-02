package app.aloha.internet.service

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AvatarApiService {
    @Multipart
    @POST("/api/avatar")
    fun uploadAvatar(
        @Header("authorization") accessToken: String,
        @Part file: MultipartBody.Part
    ): Call<AvatarUploadResponse>
}

data class AvatarUploadResponse(
    val avatarUrl: String
)
