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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.TopAppBar
import app.aloha.ui.theme.AlohaForumTheme
import app.aloha.viewmodel.EditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val editVM = hiltViewModel<EditViewModel>()
            val topicId = intent.getStringExtra("topicId")

            topicId?.let {
                editVM.topicId = it
            }

            AlohaForumTheme {
                Scaffold(
                    Modifier.fillMaxSize(),
                    topBar = { TopBar() },
                    bottomBar = { BottomBar() }
                ) { innerPadding ->
                    EditScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    val activity = LocalContext.current as Activity

    TopAppBar(
        "Create Post",
        navIcon = AppBarNavIcon(R.drawable.ic_arrow_back, "Close this page") { activity.finish() },
        modifier = modifier
    )
}

@Composable
private fun PublishButton(modifier: Modifier = Modifier) {
    val editVM = hiltViewModel<EditViewModel>()
    val activity = LocalContext.current as Activity

    Box(
        modifier
            .fillMaxHeight()
            .clickable {
                if (editVM.validateTitle() && editVM.validateContent()) {
                    editVM.publish(
                        success = {
                            activity.finish()
                        },
                        error = { println(it) }
                    )
                }
            },
        Alignment.Center
    ) {
        Text("Publish")
    }
}


@Composable
private fun BottomBar(modifier: Modifier = Modifier) {
    val activity = LocalContext.current as Activity
    Row(
        modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
            .navigationBarsPadding()
            .height(56.dp)
            .fillMaxWidth(),
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable { activity.finish() },
            Alignment.Center
        ) {
            Text("Cancel")
        }

        Box(
            Modifier
                .padding(vertical = 8.dp)
                .background(MaterialTheme.colorScheme.outline)
                .fillMaxHeight()
                .width(1.dp)
        ) { }

        PublishButton(Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleTextField(modifier: Modifier = Modifier) {
    val editVM = hiltViewModel<EditViewModel>()

    Column {
        Text("Title")

        TextField(
            value = editVM.title,
            onValueChange = { editVM.title = it },
            modifier = modifier.clip(RoundedCornerShape(16.dp)),
            placeholder = { Text("Title") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            )
        )
    }
}

@Composable
private fun ContentTextField(modifier: Modifier = Modifier) {
    val editVM = hiltViewModel<EditViewModel>()

    Column {
        Text("Content")

        TextField(
            value = editVM.content,
            onValueChange = { editVM.content = it },
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .heightIn(min=350.dp),
            placeholder = { Text("Enter the content...") }
        )
    }
}

@Composable
private fun EditScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .padding(16.dp, 24.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TitleTextField(Modifier.fillMaxWidth())
            ContentTextField(Modifier.fillMaxWidth())
        }
    }
}