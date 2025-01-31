package app.aloha.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.domain.timestampToIsoString
import app.aloha.internet.model.Comment
import app.aloha.internet.model.Post
import app.aloha.internet.service.CommentApiService
import app.aloha.internet.service.PostApiService
import app.aloha.internet.service.PublishCommentRequest
import app.aloha.internet.service.PublishCommentResponse
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
class CommentViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val service: CommentApiService
) : ViewModel() {
    private val _comment = mutableStateListOf<Comment>()
    val comment: SnapshotStateList<Comment> get() = _comment

    lateinit var postId: String
    var content by mutableStateOf("")

    fun getComment(postId: String, page: Int = 0) {
        service
            .get(postId, page)
            .enqueue(object : Callback<List<Comment>> {
                override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                    println(1)
                    if (response.body() != null)
                        _comment.addAll(0, response.body()!!)
                }

                override fun onFailure(call: Call<List<Comment>>, t: Throwable) { }
            })
    }

    private fun requestPublish(token: String, success: (String) -> Unit, error: (String) -> Unit) {
        service
            .post(token, PublishCommentRequest(postId, content))
            .enqueue(object : Callback<PublishCommentResponse> {
                override fun onResponse(call: Call<PublishCommentResponse>, response: Response<PublishCommentResponse>) {
                    val res = response.body()

                    if (response.isSuccessful && res != null) {

                        viewModelScope.launch {
                            dataStoreRepo.get(PrefKeys.USERNAME).collect {
                                _comment.add(
                                    Comment(
                                        res.commentId,
                                        postId,
                                        timestampToIsoString(System.currentTimeMillis()),
                                        content,
                                        it ?: "You",
                                    )
                                )
                                success(res.commentId)
                            }
                        }
                    }
                    else
                        error(response.errorBody().toString())
                }

                override fun onFailure(call: Call<PublishCommentResponse>, t: Throwable) {
                    error(t.toString())
                }
            })
    }

    /**
     * @param success a post id will be passed as the parameter
     */
    fun publish(success: (String) -> Unit, error: (String) -> Unit) {
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