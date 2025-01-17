package app.aloha.ui.page

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.aloha.activity.AuthActivity
import app.aloha.R
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.TopAppBar

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    Column {
        val activity = LocalContext.current as? Activity
        Box(
            Modifier.clickable { activity?.startActivity(Intent(activity, AuthActivity::class.java)) }
        ) {
            TopAppBar(
                "Guest",
                "Click here to log in...",
                AppBarNavIcon(R.drawable.ic_person, "User Icon")
            )
        }
        Column(modifier) {


        }
    }
}