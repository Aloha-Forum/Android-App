package app.aloha.ui.page

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.aloha.R
import app.aloha.activity.AuthActivity
import app.aloha.activity.WebViewActivity
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.GitHubButton
import app.aloha.ui.component.Label
import app.aloha.ui.component.PrivacyPolicyButton
import app.aloha.ui.component.ProfileButton
import app.aloha.ui.component.ThemeButton
import app.aloha.ui.component.TopAppBar

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    Column {
        val activity = LocalContext.current as? Activity
        Box(
            Modifier.clickable { activity?.startActivity(Intent(activity, AuthActivity::class.java)) }
        ) {
            TopAppBar(
                "Guest",
                "Click here to log in...",
                AppBarNavIcon(R.drawable.ic_person, "User Icon")
            )
        }
        Column(
            modifier.padding(24.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Label("General", fontWeight = FontWeight.Bold)
            ProfileButton("Account Info", R.drawable.ic_person_search, "Account Info Icon")

            ThemeButton( )

            HorizontalDivider(Modifier.padding(bottom = 12.dp))

            Label("System", fontWeight = FontWeight.Bold)

            GitHubButton()
            PrivacyPolicyButton()

            ProfileButton("App Ver.", R.drawable.ic_construct, "App Ver.",
                data=LocalContext.current.packageManager.getPackageInfo(LocalContext.current.packageName, 0).versionName
            )
        }
    }
}