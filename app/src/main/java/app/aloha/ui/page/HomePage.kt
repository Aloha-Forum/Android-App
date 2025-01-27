package app.aloha.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.aloha.ui.component.TopAppBar

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column {
        TopAppBar("Home")
    }
}