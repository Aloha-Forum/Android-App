package app.aloha.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.aloha.R

@Composable
fun LikeButton(count: Int, isLike: Boolean, onClick: () -> Unit) {
    IconText(
        if (isLike) R.drawable.ic_thumb_up_fill else R.drawable.ic_thumb_up,
        (count + if (isLike) 1 else 0).toString(),
        contentDescription = "LikeCount",
        Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun DislikeButton(count: Int, isDisliked: Boolean, onClick: () -> Unit) {
    IconText(
        if (isDisliked) R.drawable.ic_thumb_down_fill else R.drawable.ic_thumb_down,
        (count + if (isDisliked) 1 else 0).toString(),
        contentDescription = "DislikeCount",
        Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}