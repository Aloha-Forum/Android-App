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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
                            ),
                        )
                    },
                    bottomBar = { PostBottomBar(Modifier.imePadding()) }
                ) { innerPadding ->
                    Column(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                    ) {
                        Column(
                            Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .padding(16.dp, 24.dp)
                                .fillMaxWidth()
                        ){
                            Box(
                                Modifier
                                    .padding(bottom = 12.dp),
                                Alignment.TopStart
                            ) {
                                Title(post?.title ?: "")
                            }
                            CommentCard(post?.uid ?: "", post?.body ?: "", post?.postAt ?: "")

                            Row(
                                Modifier
                                    .padding(top = 24.dp)
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
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                                        RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                                    )
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {

                                Text(
                                    "Comments",
                                    Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                comment.forEachIndexed { index, it ->
                                    CommentCard(it.uid, it.content, it.createdAt, index+1)

                                    if (index != comment.size - 1) {
                                        Box(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(MaterialTheme.colorScheme.outlineVariant)
                                        )
                                    }
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
fun PostBottomBar(modifier: Modifier = Modifier) {
    val commentVM = hiltViewModel<CommentViewModel>()
    val keyboardController = LocalSoftwareKeyboardController.current

    val systemUiController = rememberSystemUiController()
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(colorScheme) {
        systemUiController.setStatusBarColor(colorScheme.surface)
        systemUiController.setNavigationBarColor(colorScheme.surfaceContainer)
    }

    Surface(modifier, shadowElevation = 8.dp) {
        Row(
            Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .navigationBarsPadding()
                .padding(16.dp, 8.dp)
                .wrapContentHeight()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(16.dp, 12.dp)
                ) {
                    if (commentVM.content.isEmpty()) {
                        Text(
                            text = "Leave a comment...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }

                    BasicTextField(
                        value = commentVM.content,
                        onValueChange = { commentVM.content = it },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Start
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Box(
                Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .clip(CircleShape)
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