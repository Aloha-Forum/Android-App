package app.aloha.activity

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.aloha.R
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.Title
import app.aloha.ui.component.TopAppBar
import app.aloha.ui.theme.AlohaForumTheme
import app.aloha.viewmodel.TokenCallback
import app.aloha.viewmodel.AuthViewModel
import app.aloha.viewmodel.LoginCallback
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlohaForumTheme {
                enableEdgeToEdge()

                var pageIndex by remember { mutableStateOf(0) }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            when (pageIndex) {
                                0 -> "Login"
                                1 -> "Signup"
                                else -> ""
                            },
                            navIcon = AppBarNavIcon(
                                R.drawable.ic_arrow_back,
                                "Close the current page"
                            ) {
                                setResult(RESULT_CANCELED)
                                finish()
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        when (pageIndex) {
                            0 -> LoginPage { pageIndex = it }
                            1 -> SignupPage { pageIndex = it }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginPage(switchPage: (Int) -> Unit) {
    val authVM: AuthViewModel = viewModel()
    val activity = LocalContext.current as Activity

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val response = AuthorizationResponse.fromIntent(it.data!!)
            if (response != null) {
                authVM.getAccessToken(
                    response,
                    object : TokenCallback {
                        override fun onSuccess(token: String) {
                            authVM.login(token, object : LoginCallback {
                                override fun onSuccess() {
                                    activity.run {
                                        setResult(RESULT_OK)
                                        finish()
                                    }
                                }

                                override fun onFailure(message: String?) {
                                    println(message)
                                }
                            })
                        }

                        override fun onFailure(message: String?) {

                        }
                    }
                )
            }
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Title("Welcome back")
        GoogleOAuthButton {
            launcher.launch(authVM.authIntent)
        }
        FlowRow(
            Modifier
                .height(48.dp)
                .fillMaxWidth()
                .clickable { switchPage(1) },
            verticalArrangement = Arrangement.Center
        ) {
            Text("Don't have an account?  ")
            Text("Sign up", color= Color.Blue, textDecoration = TextDecoration.Underline)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignupPage(switchPage: (Int) -> Unit) {
    val authVM: AuthViewModel = viewModel()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val response = AuthorizationResponse.fromIntent(it.data!!)
            if (response != null) {
                authVM.getAccessToken(
                    AuthorizationResponse.fromIntent(authVM.authIntent)!!,
                    object : TokenCallback {
                        override fun onSuccess(token: String) {

                        }

                        override fun onFailure(message: String?) {

                        }
                    }
                )
            }
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Title("Welcome to Aloha forum")
        GoogleOAuthButton { launcher.launch(authVM.authIntent) }
        FlowRow(
            Modifier
                .height(48.dp)
                .fillMaxWidth()
                .clickable { switchPage(0) },
            verticalArrangement = Arrangement.Center
        ) {
            Text("Have an account?  ")
            Text("Log in", color= Color.Blue, textDecoration = TextDecoration.Underline)
        }
    }
    //         ActivityResultContracts.StartActivityForResult()
}

@Composable
fun GoogleOAuthButton(onClick: () -> Unit) {
    Row(
        Modifier
            .clickable { onClick() }
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
                tint = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            "Login with Google",
            color = MaterialTheme.colorScheme.inverseOnSurface
        )
    }
}