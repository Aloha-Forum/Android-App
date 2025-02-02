package app.aloha.viewmodel

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.internet.service.AvatarApiService
import app.aloha.internet.service.AvatarUploadResponse
import app.aloha.internet.service.PublishRequest
import app.aloha.internet.service.PublishResponse
import app.aloha.repository.DataStoreRepo
import app.aloha.repository.PrefKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class AvatarViewModel @Inject constructor(
    private val repo: DataStoreRepo,
    private val service: AvatarApiService
) : ViewModel() {

    private fun uploadAvatar(
        token: String,
        filePart: MultipartBody.Part,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        service.uploadAvatar(token, filePart)
            .enqueue(object : Callback<AvatarUploadResponse> {
                override fun onResponse(
                    call: Call<AvatarUploadResponse>,
                    response: Response<AvatarUploadResponse>
                ) {
                    if (response.isSuccessful)
                        onSuccess(response.body()?.avatarUrl ?: "")
                    else
                        onError(response.errorBody().toString())
                }

                override fun onFailure(call: Call<AvatarUploadResponse>, t: Throwable) {
                    onError(t.message ?: "Unknown error")
                }
            })
    }

    fun uploadAvatar(
        part: MultipartBody.Part,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            repo.get(PrefKeys.ACCESS_TOKEN).collect { token ->
                if (token != null)
                    uploadAvatar(token, part, onSuccess, onError)
                else
                    error("Login required")
            }
        }
    }
}