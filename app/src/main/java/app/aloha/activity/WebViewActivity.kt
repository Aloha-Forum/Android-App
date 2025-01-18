package app.aloha.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import app.aloha.R
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.TopAppBar
import app.aloha.ui.theme.AlohaForumTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val url = intent.getStringExtra("url")

            AlohaForumTheme {
                Scaffold(
                    Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(getDomainAndTLD(url ?: ""), navIcon = AppBarNavIcon(R.drawable.ic_arrow_back, "") {
                            finish()
                        })
                    }
                ) {
                    WebView(url, Modifier.padding(it))
                }
            }
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebView(url: String?, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        AndroidView(
            factory = { WebView(it).apply { settings.javaScriptEnabled = true } },
            Modifier.matchParentSize(),
            update = { it.loadUrl(url ?: "") },
        )
    }
}

fun getDomainAndTLD(url: String): String? {
    return try {
        val uri = Uri.parse(url)
        val host = uri.host ?: return null

        // Split the host by '.' and get the last two parts
        val parts = host.split(".")
        if (parts.size < 2) return null

        // Join the last two parts to get domain and TLD
        "${parts[parts.size - 2]}.${parts[parts.size - 1]}"
    } catch (e: Exception) {
        null
    }
}