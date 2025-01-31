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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.CommentCard
import app.aloha.ui.component.DislikeButton
import app.aloha.ui.component.LikeButton
import app.aloha.ui.component.Title
import app.aloha.ui.component.TopAppBar
import app.aloha.ui.theme.AlohaForumTheme
import app.aloha.viewmodel.CommentViewModel
import app.aloha.viewmodel.PostViewModel
import app.aloha.viewmodel.Vote
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

            val commentVM: CommentViewModel = hiltViewModel()

            val comment = remember { commentVM.comment }

            LaunchedEffect(Unit) {
                commentVM.postId = id
                commentVM.getComment(id)
            }

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
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            Modifier
                                .padding(24.dp)
                                .fillMaxWidth()
                        ){
                            Title(post?.title ?: "")
                            CommentCard(post?.uid ?: "", post?.body ?: "", post?.postAt ?: "")
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
                                    var vote by remember { postVM.vote }
                                    LikeButton(post?.likeCount ?: 0, vote == Vote.Like) {
                                        vote = if (vote == Vote.Like) Vote.None else Vote.Like
                                        postVM.vote(id, vote)
                                    }
                                    DislikeButton(post?.dislikeCount ?: 0, vote == Vote.Dislike) {
                                        vote = if (vote == Vote.Dislike) Vote.None else Vote.Dislike
                                        postVM.vote(id, vote)
                                    }
                                }
                            }
                        }

                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                .padding(24.dp)
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            comment.forEachIndexed { index, it ->
                                CommentCard(it.uid, it.content, it.createdAt, index+1)
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
fun PostBottomBar(modifier: Modifier = Modifier) {
    val commentVM = hiltViewModel<CommentViewModel>()
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(shadowElevation = 8.dp) {
        Row(
            modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .navigationBarsPadding()
                .height(56.dp)
        ) {
            Row(Modifier.weight(1f)) {
                TextField(
                    value = commentVM.content,
                    onValueChange = { commentVM.content = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    placeholder = { Text("Leave a comment...") },
                )
            }
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .clickable {
                        commentVM.publish(
                            success = {
                                commentVM.content = ""
                                keyboardController?.hide()
                            },
                            error = {
                                println(it)
                            }
                        )
                    },
                Alignment.Center
            ) {
                Icon(painterResource(R.drawable.ic_send), "publish comment")
            }
        }
    }
}