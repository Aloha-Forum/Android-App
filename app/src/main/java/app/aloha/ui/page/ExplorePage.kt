package app.aloha.ui.page

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.activity.TopicActivity
import app.aloha.internet.model.Topic
import app.aloha.ui.component.Card
import app.aloha.ui.component.SearchBar
import app.aloha.ui.component.Title
import app.aloha.viewmodel.TopicViewModel

@Composable
private fun EmptyTopicList(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), Alignment.Center) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Title("Σ(っ°Д°;)っ")
            Text("No topic found")
        }
    }
}

private fun displayTopicPosts(context: Context, topicId: String) {
    val intent = Intent(context, TopicActivity::class.java).apply {
        putExtra("id", topicId)
    }
    context.startActivity(intent)
}

@Composable
private fun TopicList(topics: List<Topic>, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyColumn(
        modifier.padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(topics) { topic ->
            Card(topic.name, topic.description) {
                println(topic.id)
                displayTopicPosts(context, topic.id)
            }
        }
    }
}

@Composable
fun ExplorePage(modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Box(
            Modifier
                .height(150.dp)
                .fillMaxWidth(),
            Alignment.BottomStart
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "Explore",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                SearchBar()
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val topicVM = hiltViewModel<TopicViewModel>()
            val topics = remember { topicVM.topics }

            when (topics.isNotEmpty()) {
                true -> TopicList(topics.values.toList())
                else -> EmptyTopicList()
            }
        }
    }
}