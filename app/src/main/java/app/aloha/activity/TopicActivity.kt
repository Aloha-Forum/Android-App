package app.aloha.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.domain.timeLagFromCurrent
import app.aloha.ui.component.TopAppBar
import app.aloha.ui.component.Card
import app.aloha.ui.component.CardInfo
import app.aloha.ui.theme.AlohaForumTheme
import app.aloha.viewmodel.TopicViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlohaForumTheme {
                val id = intent.getStringExtra("id")

                val topicVM: TopicViewModel = hiltViewModel()
                val topic = topicVM.topics[id]

                Scaffold(
                    topBar = { TopAppBar(topic?.name, topic?.description) }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        val posts = remember { topicVM.posts }
                        topicVM.getTopicPosts(id!!)
                        val context = LocalContext.current

                        LazyColumn(
                            Modifier
                                .fillMaxHeight()
                                .padding(12.dp, 24.dp)
                        ) {
                            items(posts[id] ?: emptyList()) {
                                PostCard(
                                    it.title,
                                    it.body.trim().substring(0,
                                        (it.body.trim().length - 1).coerceAtMost(50)
                                    ),
                                    it.uid,
                                    it.popularity.commentCount,
                                    it.lastActivity,
                                ) {
                                    val intent = Intent(context, PostActivity::class.java)
                                    intent.putExtra("id", it.id)
                                    intent.putExtra("topic", topic?.name)
                                    context.startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostCard(
    name: String,
    description: String?,
    uid: String,
    commentCount: Int,
    lastActivity: Long,
    onClick: () -> Unit = {}
) {
    val info = mutableListOf<CardInfo>()
    info.add(CardInfo(R.drawable.ic_person, uid, "Publisher"))
    info.add(CardInfo(R.drawable.ic_comment, commentCount.toString(), "Comment Count"))
    info.add(CardInfo(R.drawable.ic_recent_activity, timeLagFromCurrent(lastActivity), "Recent activity"))

    Card(name, description ?: "", info.toTypedArray(), onClick)
}