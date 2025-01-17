package app.aloha.ui.component

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.aloha.activity.Page

@Composable
fun BottomNavigationBar(navController: NavController) {
    val pages = listOf(Page.Home, Page.Explore, Page.Profile)

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDest = currentBackStackEntry.value?.destination

    NavigationBar {
        pages.forEach { screen ->
            val selected = currentDest?.route == screen.route

            NavigationBarItem(
                icon = {
                    Icon(
                        ImageVector.vectorResource(
                            if (selected) screen.activeIcon else screen.inActiveIcon
                        ),
                        screen.title,
                        Modifier.heightIn(min=38.dp),
                        tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationRoute ?: "") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}