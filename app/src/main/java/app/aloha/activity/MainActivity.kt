package app.aloha.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.aloha.R
import app.aloha.ui.component.BottomNavigationBar
import app.aloha.ui.page.ExplorePage
import app.aloha.ui.page.HomePage
import app.aloha.ui.page.ProfilePage
import app.aloha.ui.theme.AlohaForumTheme
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            AlohaForumTheme {
                Scaffold() { _ ->
                    Box(Modifier.navigationBarsPadding()) {
                        NavHost(navController, Page.Home.route, Modifier.fillMaxSize()) {
                            composable(Page.Home.route) { HomePage() }
                            composable(Page.Explore.route) { ExplorePage() }
                            composable(Page.Profile.route) { ProfilePage() }
                        }
                        BottomNavigationBar(navController, Modifier.align(Alignment.BottomCenter))
                    }
                }
            }
        }
    }
}

sealed class Page(val route: String, @DrawableRes val active: Int, @DrawableRes val inactive: Int) {
    data object Home : Page("Home", R.drawable.ic_home_fill, R.drawable.ic_home)
    data object Explore : Page("Explore", R.drawable.ic_explore_fill, R.drawable.ic_explore)
    data object Profile : Page("Profile", R.drawable.ic_person_fill, R.drawable.ic_person)
}