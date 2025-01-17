package app.aloha.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.domain.timeLagFromCurrent
import app.aloha.ui.component.TopAppBar
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.IconText
import app.aloha.ui.component.Title
import app.aloha.ui.theme.AlohaForumTheme
import app.aloha.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val id = intent.getStringExtra("id")

            val postVM: PostViewModel = hiltViewModel()
            val post = postVM.posts[id]
            postVM.getPost(id!!)

            AlohaForumTheme {
                Scaffold(
                    topBar = {
                        val activity = LocalContext.current as? Activity
                        TopAppBar(
                            intent.getStringExtra("topic"),
                            navIcon = AppBarNavIcon(
                                R.drawable.ic_arrow_back,
                                contentDescription = "Close the current page",
                                onClick = { activity?.finish() }
                            )
                        )
                     },
                    bottomBar = { PostBottomBar() }
                ) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Title(post?.title ?: "")
                        Row(
                            Modifier.height(51.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            UserIcon()
                            Text(
                                if (post != null)
                                    post.uid + "  •  " + timeLagFromCurrent(post.postAt)
                                else "uid + time",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        val onSurfaceColor = MaterialTheme.colorScheme.onSurfaceVariant
                        Column(
                            Modifier
                                .padding(start = 12.dp)
                                .drawWithContent {
                                    drawContent()
                                    drawLine(
                                        color = onSurfaceColor,
                                        start = Offset(0f, 0f),
                                        end = Offset(0f, size.height),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }
                        ) {
                            Text(post?.body ?: "", Modifier.padding(start = 24.dp))
                        }
                        Row(
                            Modifier
                                .padding(vertical = 12.dp)
                                .height(48.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row {
                                LikeButton(post?.likeCount ?: 0, false)
                                DislikeButton(post?.dislikeCount ?: 0, false)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserIcon() {
    Box(
        Modifier
            .size(24.dp)
            .background(Color.Black, CircleShape)
    )
}

@Composable
fun LikeButton(count: Int, isLiked: Boolean) {
    IconText(
        if (isLiked) R.drawable.ic_thumb_up_fill else R.drawable.ic_thumb_up,
        count.toString(),
        contentDescription = "LikeCount",
        Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable {

            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun DislikeButton(count: Int, isDisliked: Boolean) {
    IconText(
        if (isDisliked) R.drawable.ic_thumb_down_fill else R.drawable.ic_thumb_down,
        count.toString(),
        contentDescription = "LikeCount",
        Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable {

            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun PostBottomBar() {
    Row(
        Modifier
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .height(48.dp)
    ) {
        var text by remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            }
        )
    }
}