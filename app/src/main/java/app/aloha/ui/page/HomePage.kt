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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.activity.PostActivity
import app.aloha.internet.model.Post
import app.aloha.ui.component.PostCard
import app.aloha.ui.component.Title
import app.aloha.ui.component.TopAppBar
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

    Box(modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 92.dp)
        ) {
            items(posts) { post ->
                PostCard(post) { displayPost(context, post.id) }
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
private fun HomePageTopBar(modifier: Modifier = Modifier) {
    TopAppBar("Home", modifier=modifier)
}

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(modifier) {
        HomePageTopBar(Modifier.padding(bottom = 2.dp))
        Column(
            Modifier.weight(1f),
            Arrangement.spacedBy(24.dp)
        ) {
            val postVM = hiltViewModel<PostViewModel>()
            val posts = remember { postVM.recommend }

            when (posts.isNotEmpty()) {
                true -> RecommendList(posts)
                else -> EmptyPostList()
            }
        }
    }
}