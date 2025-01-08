package app.aloha.ui.page

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.TopicActivity
import app.aloha.ui.component.Card
import app.aloha.ui.component.CardInfo
import app.aloha.viewmodel.TopicViewModel

@Composable
fun ExplorePage(modifier: Modifier = Modifier) {
    val topicVM: TopicViewModel = hiltViewModel()
    val topics = remember { topicVM.topics }

    val context = LocalContext.current
    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        topics.forEach {
            Card(it.name, it.description) {
                val intent = Intent(context, TopicActivity::class.java)
                println(it.id)
                intent.putExtra("id", it.id)
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun TopicCard(
    name: String,
    description: String?,
    viewCount: Int,
    postCount: Int,
    lastActivity: Long
) {
    val info = mutableListOf<CardInfo>()
    if (viewCount > 0)
        info.add(CardInfo(R.drawable.ic_hot, viewCount.toString(), "Yesterday's view count"))

    if (postCount > 0)
        info.add(CardInfo(R.drawable.ic_comment, postCount.toString(), "Yesterday's post count"))

    if (lastActivity != -1L)
        info.add(CardInfo(R.drawable.ic_recent_activity, lastActivity.toString(), "Recent activity"))

    Card(name, description ?: "", info.toTypedArray())
}