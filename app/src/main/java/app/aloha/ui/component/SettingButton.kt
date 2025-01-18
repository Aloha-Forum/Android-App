package app.aloha.ui.component

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.activity.WebViewActivity
import app.aloha.domain.getNextTheme
import app.aloha.domain.getThemeName
import app.aloha.viewmodel.SettingViewModel

@Composable
fun ThemeButton(modifier: Modifier = Modifier) {
    val settVM: SettingViewModel = hiltViewModel()
    val theme by settVM.getTheme().collectAsState(null)

    ProfileButton(
        "Themed",
        R.drawable.ic_theme, "Switch the current color scheme",
        modifier,
        data = getThemeName(theme),
        onClick = { settVM.setTheme(getNextTheme(theme)) }
    )
}

@Composable
fun GitHubButton(modifier: Modifier = Modifier) {
    WebViewButton(
        "Our GitHub",
        R.drawable.ic_github, "Open the GitHub repository of this app",
        R.string.github_url,
        modifier
    )
}

@Composable
fun PrivacyPolicyButton(modifier: Modifier = Modifier) {
    WebViewButton(
        "Privacy Policy",
        R.drawable.ic_policy, "Open privacy policy page",
        R.string.privacy_policy_url,
        modifier
    )
}

@Composable
private fun WebViewButton(
    text: String,
    @DrawableRes iconRes: Int,
    contentDescription: String,
    @StringRes url: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    ProfileButton(text, iconRes, contentDescription, modifier) {
        val intent = Intent(context, WebViewActivity::class.java)
            .putExtra("url", context.resources.getString(url))

        context.startActivity(intent)
    }
}

@Composable
fun ProfileButton(
    text: String,
    @DrawableRes iconRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    data: String? = null,
    onClick: () -> Unit = { }
) {
    Row(
        modifier
            .height(48.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(
                ImageVector.vectorResource(iconRes),
                contentDescription,
                tint = color
            )
            BodyText(text, color = color)
        }
        data?.let { Label(it, color = color) }
    }
}