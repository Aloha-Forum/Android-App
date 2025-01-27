package app.aloha.ui.component

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    NavigationBar() {
        pages.forEach { page ->
            val selected = currentDest?.route == page.route

            NavigationBarItem(
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIndicatorColor = Color.Unspecified,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledIconColor = MaterialTheme.colorScheme.tertiary,
                    disabledTextColor = MaterialTheme.colorScheme.tertiary,
                ),
                icon = {
                    Icon(
                        ImageVector.vectorResource(
                            if (selected) page.active else page.inactive
                        ),
                        page.route,
                        tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = {
                    if (selected)
                        Text(page.route, style = MaterialTheme.typography.labelSmall)
                },
                selected = selected,
                onClick = {
                    navController.navigate(page.route) {
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