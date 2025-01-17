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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    props: Array<CardInfo> = arrayOf(),
    onClick: () -> Unit = {}
) {
    Surface(shape = RoundedCornerShape(15.dp), shadowElevation = 4.dp) {
        Column(
            Modifier
                .clickable(onClick = onClick)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(15.dp)
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Title(
                title,
                Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp,
                    )
                    .heightIn(max = 80.dp),
            )

            if (body.isNotEmpty()) {
                BodyText(
                    body,
                    Modifier
                        .padding(
                            start = 24.dp, end = 24.dp,
                            bottom = if (props.isNotEmpty()) 0.dp else 24.dp
                        )
                        .heightIn(max = 60.dp),
                )
            }

            if (props.isNotEmpty()) {
                FlowRow(
                    Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    props.forEach { IconText(it.drawable, it.value, it.contentDescription) }
                }
            }
        }
    }
}