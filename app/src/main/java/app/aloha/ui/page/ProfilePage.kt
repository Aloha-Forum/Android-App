package app.aloha.ui.page

import android.app.Activity
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import app.aloha.activity.AuthActivity
import app.aloha.R
import app.aloha.domain.getNextTheme
import app.aloha.domain.getThemeName
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.TopAppBar
import app.aloha.viewmodel.SettingViewModel

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    val settVM: SettingViewModel = hiltViewModel()

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
        Column(
            modifier.padding(24.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "General",
                color= MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            SettingButton("Account Info", R.drawable.ic_person_search, "Account Info Icon")

            ThemeSettingButton()


            HorizontalDivider(Modifier.padding(bottom = 12.dp))
            Text(
                "System",
                color= MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            SettingButton("Our GitHub", R.drawable.ic_github, "GitHub Icon")

            SettingButton("Privacy Policy", R.drawable.ic_policy, "Privacy Icon")

            SettingButton("App Info", R.drawable.ic_construct, "GitHub Icon")
        }
    }
}

@Composable
private fun SettingButton(
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
            Text(text, color=color)
        }
        data?.let {
            Text(it, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun ThemeSettingButton(modifier: Modifier = Modifier) {
    val settVM: SettingViewModel = hiltViewModel()
    val theme by settVM.getTheme().collectAsState(null)

    SettingButton(
        "Themed",
        R.drawable.ic_theme, "Theme Icon",
        modifier,
        data = getThemeName(theme),
        onClick = { settVM.setTheme(getNextTheme(theme)) }
    )
}