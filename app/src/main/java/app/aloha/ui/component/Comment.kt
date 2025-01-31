package app.aloha.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import app.aloha.activity.UserIcon
import app.aloha.domain.timeLagFromCurrent

@Composable
fun CommentCard(uid: String, content: String, postAt: String, nth: Int? = null) {
    Column {

        Row(
            Modifier.height(51.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                UserIcon()
                BodyText(
                    text = "$uid • ${timeLagFromCurrent(postAt)}",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            if (nth != null) {
                Label(
                    text = "#$nth",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        val onSurfaceColor = MaterialTheme.colorScheme.onSurfaceVariant
        Column(
            Modifier
                .padding(start = 12.dp)
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = onSurfaceColor,
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
        ) {
            Text(content, Modifier.padding(start = 24.dp))
        }
    }
}