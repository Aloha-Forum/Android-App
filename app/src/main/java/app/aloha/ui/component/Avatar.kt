package app.aloha.ui.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.aloha.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

@Composable
fun Avatar(uid: String?, reloadAvatar: Boolean = false, size: Dp = 48.dp, onClick: () -> Unit = {}) {
    val url = "https://alohapublic.blob.core.windows.net/avatars/$uid"

    val context = LocalContext.current
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    var bitmap by remember {
        mutableStateOf(BitmapFactory.decodeResource(context.resources, R.drawable.anonymous))
    }

    uid?.let {
        LaunchedEffect(it) {
            coroutineScope.launch {
                try {
                    val connection = URL(url).openConnection()

                    connection.connect()

                    val input = connection.getInputStream()
                    bitmap = BitmapFactory.decodeStream(input)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "User Avatar",
            modifier = Modifier
                .clip(CircleShape)
                .size(size)
                .clickable { onClick() }
        )
    }
}