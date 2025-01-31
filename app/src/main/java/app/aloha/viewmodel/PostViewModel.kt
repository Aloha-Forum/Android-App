package app.aloha.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.internet.model.Post
import app.aloha.internet.service.PostApiService
import app.aloha.internet.service.TopicApiService
import app.aloha.internet.service.VoteRequest
import app.aloha.repository.DataStoreRepo
import app.aloha.repository.PrefKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

enum class Vote(val value: Int) {
    Like(1),
    Dislike(0),
    None(-1)
}

@HiltViewModel
class PostViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val service: PostApiService
) : ViewModel() {
    private val _posts = mutableStateMapOf<String, Post>()
    val posts: SnapshotStateMap<String, Post> get() = _posts

    private val _recommend = mutableStateListOf<Post>()
    val recommend: SnapshotStateList<Post> get() = _recommend

    var vote: MutableState<Vote> = mutableStateOf(Vote.None)

    fun getPost(id: String) {
        viewModelScope.launch {
            dataStoreRepo.get(PrefKeys.ACCESS_TOKEN).collect { token ->
                val call = if (token == null) service.getPost(id) else service.getPost(token, id)
                call.enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        val body = response.body()
                        if (body != null) {
                            _posts[id] = body
                            vote.value = Vote.entries.find { it.value == body.vote } ?: Vote.None
                        }
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {}
                })
            }
        }
    }

    fun getRecommend(page: Int=0) {
        service
            .getRecommend(page)
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    response.body()?.let {
                        _recommend.addAll(it)
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) { }
            })
    }

    fun vote(postId: String, type: Vote) {
        viewModelScope.launch {
            dataStoreRepo.get(PrefKeys.ACCESS_TOKEN).collect { token ->
                if (token != null) {
                    service
                        .vote(token, VoteRequest(postId, type.value))
                        .enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                println(response.code())
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) { }
                        })
                }
                else
                    error("Login required")
            }
        }
    }

    init {
        getRecommend(page = 0)
    }
}