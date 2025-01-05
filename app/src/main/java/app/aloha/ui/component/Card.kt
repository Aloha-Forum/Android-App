package app.aloha.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

data class CardInfo (
    @DrawableRes val drawable: Int,
    val value: String,
    val contentDescription: String
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Card(
    title: String,
    body: String,
    props: Array<CardInfo>
) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(15.dp))
            .padding(24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )

        if (body.isNotEmpty()) {
            Text(
                body,
                Modifier.heightIn(max = 60.dp),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis
            )
        }

        FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            props.forEach { IconText(it.drawable, it.value, it.contentDescription) }
        }
    }
}