package app.aloha.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import app.aloha.R
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.TopAppBar

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    Column {
        TopAppBar(
            "Guest",
            "Click here to log in...",
            AppBarNavIcon(R.drawable.ic_person, "User Icon")
        )
        Column(modifier) {


        }
    }
}