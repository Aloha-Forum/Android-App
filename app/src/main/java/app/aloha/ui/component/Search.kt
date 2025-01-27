package app.aloha.ui.component

import android.adservices.topics.Topic
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.viewmodel.TopicViewModel

private fun search(topicVM: TopicViewModel, query: String) {
    topicVM.searchQuery = query
    topicVM.search()
}

@Composable
private fun SearchLeadingIcon(modifier: Modifier = Modifier) {
    val topicVM = hiltViewModel<TopicViewModel>()

    when (topicVM.searchQuery.isNotEmpty()) {
        true -> {
            Icon(
                rememberVectorPainter(Icons.Default.ArrowBack),
                contentDescription = "Clear search result",
                modifier.clickable { search(topicVM, "") }
            )
        }
        else -> {
            Icon(
                rememberVectorPainter(Icons.Default.Search),
                contentDescription = "Search",
                modifier
            )
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    val topicVM = hiltViewModel<TopicViewModel>()

    TextField(
        value = topicVM.searchQuery,
        onValueChange = { search(topicVM, it) },
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        leadingIcon = {
            SearchLeadingIcon()
        },
        placeholder = {
            BodyText("Search...")
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { topicVM.search() }),
    )
}