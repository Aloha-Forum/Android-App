package app.aloha.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.aloha.R
import app.aloha.domain.timeLagFromCurrent
import app.aloha.internet.model.Post

data class CardInfo (
    @DrawableRes val drawable: Int,
    val value: String,
    val contentDescription: String
)

@Composable
fun PostCard(p: Post, onClick: () -> Unit = {}) {
    val info = mutableListOf<CardInfo>()

    info.add(CardInfo(R.drawable.ic_person, p.uid, "Publisher"))
    info.add(CardInfo(R.drawable.ic_comment, p.commentCount.toString(), "Comment Count"))
    info.add(CardInfo(R.drawable.ic_activity, timeLagFromCurrent(p.lastActivity), "Recent activity"))

    Card(p.title, p.body, info.toTypedArray(), onClick)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Card(
    title: String,
    body: String,
    props: Array<CardInfo> = arrayOf(),
    onClick: () -> Unit = {}
) {
    Surface(shadowElevation = 4.dp) {
        Column(
            Modifier
                .clickable(onClick = onClick)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Title(
                title,
                Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    .heightIn(max = 80.dp),
            )

            if (body.isNotEmpty()) {
                Text(
                    body.trim(),
                    Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 12.dp, bottom = if (props.isNotEmpty()) 12.dp else 24.dp)
                        .heightIn(max = 60.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (props.isNotEmpty()) {
                FlowRow(
                    Modifier
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    props.forEach { IconText(it.drawable, it.value, it.contentDescription) }
                }
            }
        }
    }
}