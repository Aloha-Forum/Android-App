package app.aloha.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TopAppBar(
    title: String?,
    description: String? = null,
    navIcon: AppBarNavIcon? = null
) {
    Surface(shadowElevation = 4.dp) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .padding(
                    top = 24.dp, bottom = if (description != null) 24.dp else 12.dp,
                    start = 16.dp, end = 16.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            navIcon?.run {
                Box(Modifier.fillMaxHeight(), Alignment.CenterStart) {
                    IconButton(onClick) {
                        Icon(ImageVector.vectorResource(icon), contentDescription)
                    }
                }
            }

            Column {
                Box(Modifier.heightIn(min = 48.dp, max = 60.dp), contentAlignment = Alignment.CenterStart) {
                    Text(
                        title ?: "",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                description?.run {
                    Text(
                        description,
                        Modifier.heightIn(max = 60.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

data class AppBarNavIcon(
    @DrawableRes val icon: Int,
    val contentDescription: String,
    val onClick: () -> Unit = {}
)