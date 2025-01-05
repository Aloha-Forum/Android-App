package app.aloha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.aloha.ui.component.BottomNavigationBar
import app.aloha.ui.page.ExplorePage
import app.aloha.ui.page.HomePage
import app.aloha.ui.page.ProfilePage
import app.aloha.ui.theme.AlohaForumTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlohaForumTheme {
                val navController = rememberNavController()

                Scaffold(
                    Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Page.Home.route,
                        modifier = Modifier.padding(innerPadding).padding(12.dp)
                    ) {
                        composable(Page.Home.route) { HomePage() }
                        composable(Page.Explore.route) { ExplorePage() }
                        composable(Page.Profile.route) { ProfilePage() }
                    }
                }
            }
        }
    }
}

sealed class Page(val route: String, val title: String, @DrawableRes val activeIcon: Int, @DrawableRes val inActiveIcon: Int) {
    data object Home : Page("home", "Home", R.drawable.ic_home_fill, R.drawable.ic_home)
    data object Explore : Page("explore", "Explore", R.drawable.ic_explore_fill, R.drawable.ic_explore)
    data object Profile : Page("profile", "Profile", R.drawable.ic_person_fill, R.drawable.ic_person)
}