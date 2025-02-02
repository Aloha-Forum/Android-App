package app.aloha.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import app.aloha.internet.model.Topic
import app.aloha.internet.service.TopicApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val service: TopicApiService
) : ViewModel() {
    private val _searchResults = mutableStateMapOf<String, Topic>()
    val searchResults: SnapshotStateMap<String, Topic> get() = _searchResults

    var searchQuery by mutableStateOf("")

    fun search() {
        service
            .search(searchQuery)
            .enqueue(object : Callback<List<Topic>> {
                override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                    _searchResults.clear()
                    response.body()?.forEach { topic ->
                        _searchResults[topic.id] = topic
                    }
                }

                override fun onFailure(call: Call<List<Topic>>, t: Throwable) {
                    println(t)
                }
            })
    }

    init {
        search()
    }
}