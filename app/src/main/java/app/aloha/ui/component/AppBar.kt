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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBar(
    title: String?,
    description: String? = null,
    navIcon: AppBarNavIcon? = null,
    modifier: Modifier = Modifier
) {
    Surface(modifier, shadowElevation = 4.dp) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .background(MaterialTheme.colorScheme.surface)
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

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(Modifier.heightIn(48.dp, 60.dp), Alignment.CenterStart) {
                    Text(
                        title ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }

                description?.run {
                    BodyText(description, Modifier.heightIn(max = 60.dp))
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