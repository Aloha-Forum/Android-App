package app.aloha.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.aloha.domain.timeLagFromCurrent


@Composable
fun CommentCard(uid: String, content: String, postAt: String, nth: Int? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Row(
            Modifier.height(51.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                Modifier
                    .weight(1f)
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(Modifier.fillMaxHeight().width(32.dp), Alignment.Center) {
                    Avatar(uid, size = 24.dp)
                }
                Column {
                    BodyText(
                        uid,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Label(
                        text = timeLagFromCurrent(postAt),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            if (nth != null) {
                Label(
                    text = "#$nth",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        BodyText(content)
    }
}