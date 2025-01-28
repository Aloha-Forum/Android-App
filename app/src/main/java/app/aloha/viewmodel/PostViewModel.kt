package app.aloha.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import app.aloha.internet.model.Post
import app.aloha.internet.service.PostApiService
import app.aloha.internet.service.TopicApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(
    private val service: PostApiService
) : ViewModel() {
    private val _posts = mutableStateMapOf<String, Post>()
    val posts: SnapshotStateMap<String, Post> get() = _posts

    private val _recommend = mutableStateListOf<Post>()
    val recommend: SnapshotStateList<Post> get() = _recommend

    fun getPost(id: String) {
        service
            .getPost(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    println(response.body())
                    if (response.body() != null)
                        _posts[id] = response.body()!!
                }

                override fun onFailure(call: Call<Post>, t: Throwable) { }
            })
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

    init {
        getRecommend(page = 0)
    }
}