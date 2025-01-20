package app.aloha.ui.component

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.aloha.R
import app.aloha.viewmodel.AuthViewModel

@Composable
fun GoogleOAuthButton(launcher: ActivityResultLauncher<Intent>) {
    val authVM: AuthViewModel = hiltViewModel()
    Row(
        Modifier
            .clickable { launcher.launch(authVM.authIntent) }
            .height(48.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseSurface, RoundedCornerShape(15.dp))
            .padding(12.dp, 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                ImageVector.vectorResource(R.drawable.ic_google),
                "Google Icon",
                Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.inverseOnSurface,
            )
        }

        Text(
            "Access with Google",
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
    }
}