package app.aloha.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.aloha.ui.component.TopAppBar

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column {
        TopAppBar("Home")
        Row(modifier) {
            Text("Home Screen")
        }
    }
}