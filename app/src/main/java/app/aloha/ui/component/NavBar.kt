package app.aloha.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.aloha.activity.Page

private fun handleOnClickNavItem(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationRoute ?: "") {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun NavItem(
    isSelected: Boolean,
    @DrawableRes active: Int,
    @DrawableRes inactive: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier
            .clip(RoundedCornerShape(30.dp))
            .clickable(onClick = onClick),
        Alignment.Center
    ) {
        Icon(
            ImageVector.vectorResource(if (isSelected) active else inactive),
            contentDescription = null,
            tint =
                if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    val pages = listOf(Page.Home, Page.Explore, Page.Profile)

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDest = currentBackStackEntry.value?.destination

    val selectedIndex = pages.indexOfFirst { it.route == currentDest?.route }

    val density = LocalDensity.current
    var parentWidth by remember { mutableStateOf(0.dp) }
    Box(
        modifier
            .navigationBarsPadding()
            .padding(24.dp, 16.dp)
            .fillMaxWidth()
            .height(60.dp)
            .onGloballyPositioned { coordinates ->
                parentWidth = with(density) { coordinates.size.width.toDp() }
            }
    ) {
        val itemWidth = parentWidth / 3
        val backgroundOffset by animateDpAsState(selectedIndex * itemWidth)

        Surface(
            shape = RoundedCornerShape(30.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(2.dp)
            ) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .width(itemWidth)
                        .offset(x = backgroundOffset)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(30.dp)
                        )
                )

                Row(
                    Modifier
                        .fillMaxHeight()
                ) {
                    pages.forEach {
                        NavItem(
                            isSelected = pages.indexOf(it) == selectedIndex,
                            active = it.active,
                            inactive = it.inactive,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(itemWidth),
                            onClick = { handleOnClickNavItem(navController, it.route) }
                        )
                    }
                }
            }
        }
    }
}