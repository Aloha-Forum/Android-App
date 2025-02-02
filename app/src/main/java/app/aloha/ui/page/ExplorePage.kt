package app.aloha.ui.page

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.activity.TopicActivity
import app.aloha.internet.model.Post
import app.aloha.internet.model.Topic
import app.aloha.ui.component.Card
import app.aloha.ui.component.PostCard
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

    Box(modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 92.dp)
        ) {
            items(topics) { topic ->
                Card(topic.name, topic.description) {
                    println(topic.id)
                    displayTopicPosts(context, topic.id)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background,
                            Color.Transparent,
                        )
                    )
                )
                .align(Alignment.TopCenter)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun ExplorePage(modifier: Modifier = Modifier) {
    Column {
        Box(
            modifier
                .statusBarsPadding()
                .height(150.dp)
                .fillMaxWidth()
                .padding(
                    top = 24.dp, bottom = 12.dp,
                    start = 16.dp, end = 16.dp
                ),
            Alignment.BottomStart
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Title("Explore", fontWeight = FontWeight.Bold,)
                SearchBar()
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
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