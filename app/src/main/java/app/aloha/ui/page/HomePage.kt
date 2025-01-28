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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.activity.PostActivity
import app.aloha.internet.model.Post
import app.aloha.internet.model.Topic
import app.aloha.ui.component.BodyText
import app.aloha.ui.component.Card
import app.aloha.ui.component.PostCard
import app.aloha.ui.component.SearchBar
import app.aloha.ui.component.Title
import app.aloha.viewmodel.PostViewModel

@Composable
private fun EmptyPostList(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), Alignment.Center) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Title("Σ(っ°Д°;)っ")
            Text("No post found")
        }
    }
}

private fun displayPost(context: Context, postId: String) {
    val intent = Intent(context, PostActivity::class.java).apply {
        putExtra("id", postId)
    }
    context.startActivity(intent)
}

@Composable
private fun RecommendList(posts: List<Post>, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    LazyColumn(
        modifier.padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts) { post ->
            PostCard(post) { displayPost(context, post.id) }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Box(
            Modifier
                .height(150.dp)
                .fillMaxWidth(),
            Alignment.BottomStart
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "Home",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                Text(
                    "Today is your first day using our app!",
                    Modifier.height(56.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Column(Modifier.fillMaxSize().padding(top = 24.dp), Arrangement.spacedBy(24.dp)) {
            val postVM = hiltViewModel<PostViewModel>()
            val posts = remember { postVM.recommend }

            when (posts.isNotEmpty()) {
                true -> RecommendList(posts)
                else -> EmptyPostList()
            }
        }
    }
}