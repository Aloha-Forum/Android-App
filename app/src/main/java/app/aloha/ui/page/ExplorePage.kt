package app.aloha.ui.page

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import app.aloha.ui.component.TopAppBar
import app.aloha.viewmodel.TopicViewModel

@Composable
fun ExplorePage(modifier: Modifier = Modifier) {
    Column {
        TopAppBar("Explore")
        Column(modifier.padding(horizontal = 16.dp), Arrangement.spacedBy(24.dp)) {
            val topicVM: TopicViewModel = hiltViewModel()
            val topics = remember { topicVM.topics.values }
            val context = LocalContext.current

            topics.forEach {
                Card(it.name, it.description) {
                    val intent = Intent(context, TopicActivity::class.java)
                    intent.putExtra("id", it.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}