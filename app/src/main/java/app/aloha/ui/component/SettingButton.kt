package app.aloha.ui.component

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.domain.getNextTheme
import app.aloha.domain.getThemeName
import app.aloha.viewmodel.SettingViewModel

@Composable
fun ThemeSettingButton(modifier: Modifier = Modifier) {
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

@Composable
fun SettingButton(
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