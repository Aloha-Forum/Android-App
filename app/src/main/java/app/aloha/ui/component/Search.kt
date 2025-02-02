package app.aloha.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.viewmodel.SearchViewModel

private fun search(searchVM: SearchViewModel, query: String) {
    searchVM.searchQuery = query
    searchVM.search()
}

@Composable
private fun SearchLeadingIcon(modifier: Modifier = Modifier) {
    val searchVM = hiltViewModel<SearchViewModel>()

    when (searchVM.searchQuery.isNotEmpty()) {
        true -> {
            Icon(
                rememberVectorPainter(Icons.Default.ArrowBack),
                contentDescription = "Clear search result",
                modifier.clickable { search(searchVM, "") }
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
    val searchVM = hiltViewModel<SearchViewModel>()

    TextField(
        value = searchVM.searchQuery,
        onValueChange = { search(searchVM, it) },
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .height(56.dp),
        leadingIcon = {
            SearchLeadingIcon()
        },
        placeholder = {
            BodyText("Search...")
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { searchVM.search() }),
    )
}