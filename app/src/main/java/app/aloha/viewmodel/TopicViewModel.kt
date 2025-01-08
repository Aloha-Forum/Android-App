package app.aloha.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _topics = mutableStateListOf<Topic>()
    val topics: SnapshotStateList<Topic>
        get() = _topics

    private fun getTopics() {
        apiClient.create(TopicApiService::class.java)
            .getTopics()
            .enqueue(object : Callback<List<Topic>> {
                override fun onResponse(call: Call<List<Topic>>, response: Response<List<Topic>>) {
                    if (response.body() != null)
                        topics.addAll(response.body()!!)
                }

                override fun onFailure(call: Call<List<Topic>>, t: Throwable) { }
            })
    }

    init {
        viewModelScope.launch {
            getTopics()
        }
    }
}