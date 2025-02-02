package app.aloha.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.internet.model.Post
import app.aloha.internet.service.PostApiService
import app.aloha.internet.service.PublishRequest
import app.aloha.internet.service.PublishResponse
import app.aloha.repository.DataStoreRepo
import app.aloha.repository.PrefKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val service: PostApiService
) : ViewModel() {
    lateinit var topicId: String

    var title by mutableStateOf("")
    var content by mutableStateOf("")

    fun validateTitle(): Boolean {
        if (title.isBlank() || title.length < 5 || title.length > 100)
            return false

        return true
    }

    fun validateContent(): Boolean {
        return !(content.isBlank() || content.length < 20 || content.length > 5000)
    }

    private fun requestPublish(token: String, success: (Post) -> Unit, error: (String) -> Unit) {
        service
            .publish(token, PublishRequest(title, content, topicId))
            .enqueue(object : Callback<PublishResponse> {
                override fun onResponse(call: Call<PublishResponse>, response: Response<PublishResponse>) {
                    val res = response.body()
                    if (response.isSuccessful && res != null) {
                        viewModelScope.launch {
                            dataStoreRepo.get(PrefKeys.USERNAME).collect { uid ->
                                success(Post(res.postId, topicId, uid ?: "You", title, content, res.postAt))
                            }
                        }
                    }
                    else
                        error(response.errorBody().toString())
                }

                override fun onFailure(call: Call<PublishResponse>, t: Throwable) {
                    error(t.toString())
                }
            })
    }

    /**
     * @param success a post id will be passed as the parameter
     */
    fun publish(success: (Post) -> Unit, error: (String) -> Unit) {
        viewModelScope.launch {
            dataStoreRepo.get(PrefKeys.ACCESS_TOKEN).collect { token ->
                if (token != null)
                    requestPublish(token, success, error)
                else
                    error("Login required")
            }
        }
    }
}