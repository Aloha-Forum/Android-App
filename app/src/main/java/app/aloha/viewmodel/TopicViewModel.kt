package app.aloha.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.internet.model.Post
import app.aloha.internet.service.TopicApiService
import app.aloha.internet.model.Topic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    private val apiClient: Retrofit
): ViewModel() {
    private val _topics = mutableStateMapOf<String, Topic>()
    val topics: SnapshotStateMap<String, Topic> get() = _topics

    private val _posts = mutableStateMapOf<String, List<Post>>()
    val posts: SnapshotStateMap<String, List<Post>> get() = _posts

    var searchQuery by mutableStateOf("")

    private fun getTopics() {
        apiClient.create(TopicApiService::class.java)
            .getTopics()
            .enqueue(object : Callback<List<Topic>> {
                override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                    _topics.clear()
                    if (response.body() != null)
                        for (topic in response.body()!!) {
                            _topics[topic.id] = topic
                        }
                }

                override fun onFailure(call: Call<List<Topic>>, t: Throwable) { println(t) }
            })
    }

    fun search() {
        apiClient.create(TopicApiService::class.java)
            .search(searchQuery)
            .enqueue(object : Callback<List<Topic>> {
                override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                    _topics.clear()
                    if (response.body() != null)
                        for (topic in response.body()!!) {
                            _topics[topic.id] = topic
                        }
                }

                override fun onFailure(call: Call<List<Topic>>, t: Throwable) { println(t) }
            })
    }

    fun getTopicPosts(id: String) {
        apiClient.create(TopicApiService::class.java)
            .getTopicPosts(id)
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.body() != null)
                        _posts[id] = response.body()!!
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) { }
            })
    }

    init {
        viewModelScope.launch {
            getTopics()
        }
    }
}