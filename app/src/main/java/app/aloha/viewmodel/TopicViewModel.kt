package app.aloha.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.internet.model.Post
import app.aloha.internet.model.Topic
import app.aloha.internet.service.TopicApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val service: TopicApiService
) : ViewModel() {
    private val _topic = mutableStateOf<Topic?>(null)
    val topic: State<Topic?> get() = _topic

    private val _posts = mutableStateOf<List<Post>>(emptyList())
    val posts: State<List<Post>> get() = _posts

    private fun getTopics() {
        service.getTopics().enqueue(object : Callback<List<Topic>> {
            override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                response.body()?.firstOrNull()?.let { topic ->
                    _topic.value = topic
                }
            }

            override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
                println(t)
            }
        })
    }

    fun getTopicPosts(id: String) {
        service.getTopicPosts(id).enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                response.body()?.let {
                    _posts.value = it
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {}
        })
    }

    fun addPost(newPost: Post) {
        _posts.value = listOf(newPost) + _posts.value
    }

    init {
        viewModelScope.launch {
            getTopics()
        }
    }
}