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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            SettingButton("App Theme", R.drawable.ic_theme, "Theme Icon")

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
fun SettingButton(
    text: String,
    @DrawableRes iconRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
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
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            ImageVector.vectorResource(iconRes),
            contentDescription,
            tint = color
        )
        Text(text, color=color)
    }
}