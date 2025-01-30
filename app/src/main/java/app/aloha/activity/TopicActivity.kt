package app.aloha.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.internet.model.Post
import app.aloha.ui.component.PostCard
import app.aloha.ui.component.TopAppBar
import app.aloha.ui.theme.AlohaForumTheme
import app.aloha.viewmodel.TopicViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val topicId = intent.getStringExtra("id")
        println("inside" +topicId)
        enableEdgeToEdge()
        setContent {
            AlohaForumTheme {
                println("theme" + topicId)
                TopicScreen(topicId)
            }
        }
    }
}

private fun startEditPage(context: Context, topicId: String) {
    val intent = Intent(context, EditActivity::class.java).apply {
        putExtra("topicId", topicId)
    }
    context.startActivity(intent)
}

private fun displayPost(context: Context, topicName: String, postId: String) {
    val intent = Intent(context, PostActivity::class.java).apply {
        putExtra("id", postId)
        putExtra("topic", topicName)
    }
    context.startActivity(intent)
}

@Composable
private fun EmptyPostList(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), Alignment.Center) {
        Text("No posts found")
    }
}

@Composable
private fun PostList(posts: List<Post>, topicName: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyColumn(modifier.padding(12.dp, 24.dp)) {
        items(posts) { post ->
            PostCard(post) {
                displayPost(context, topicName, post.id)
            }
        }
    }
}

@Composable
fun TopicScreen(topicId: String?, modifier: Modifier = Modifier, ) {
    val context = LocalContext.current
    val topicVM = hiltViewModel<TopicViewModel>()

    val topic = topicVM.topics[topicId]
    val posts = topicVM.posts[topicId] ?: emptyList()

    LaunchedEffect(Unit) {
        if (topicId != null)
            topicVM.getTopicPosts(topicId)
    }

    Scaffold(
        modifier,
        topBar = { TopAppBar(topic?.name, topic?.description) },
        floatingActionButton = { FloatingActionButton(onClick = { startEditPage(context, topicId!!) }) {
            Icon(painter = painterResource(R.drawable.ic_stylus), contentDescription = "")
        }}
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            when (posts.isEmpty()) {
                true -> EmptyPostList()
                else -> PostList(posts, topic?.name ?: "", Modifier.fillMaxHeight())
            }
        }
    }
}