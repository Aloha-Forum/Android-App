package app.aloha.activity

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import app.aloha.R
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.GoogleOAuthButton
import app.aloha.ui.component.Label
import app.aloha.ui.component.Title
import app.aloha.ui.component.TopAppBar
import app.aloha.ui.theme.AlohaForumTheme
import app.aloha.viewmodel.AuthCallback
import app.aloha.viewmodel.AuthViewModel
import app.aloha.viewmodel.TokenCallback
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationResponse

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlohaForumTheme {
                enableEdgeToEdge()

                Scaffold(topBar = { AuthActivityAppBar() }) { innerPadding ->
                    Box(
                        Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoginPage()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginPage() {
    val authVM: AuthViewModel = viewModel()
    val activity = LocalContext.current as Activity

    val context = LocalContext.current as Activity
    var accessToken by remember { mutableStateOf<String?>(null) }
    var username by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val response = AuthorizationResponse.fromIntent(it.data!!)
            if (response != null) {
                authVM.getAccessToken(response, object : TokenCallback {
                        override fun onSuccess(token: String) {
                            authVM.login(token, object : AuthCallback {
                                override fun onSuccess() {
                                    println("sucess")
                                    activity.run {
                                        setResult(RESULT_OK)
                                        finish()
                                    }
                                }

                                override fun onFailure(code: Int?) {
                                    println("failed")

                                    when (code) {
                                        404 -> {
                                            // user doesn't have an account
                                            // start the process of sign up
                                            accessToken = token
                                        }
                                        else -> {

                                        }
                                    }
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
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth(), Alignment.Center
        ) {
            Column(
                Modifier.fillMaxWidth(),
                Arrangement.spacedBy(36.dp),
                Alignment.CenterHorizontally
            ) {
                Title("Welcome to Aloha Forum")

                AppLogo()


                if (accessToken != null) {
                    var validationResult by remember { mutableStateOf<ValidationResult?>(null) }

                    UsernameInputField(username) { username = it }
                    Label("3-20 characters, letters and digits only.")

                    FlowRow(
                        Modifier
                            .heightIn(min = 48.dp)
                            .fillMaxWidth()
                            .clickable { accessToken = null },
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f), Alignment.CenterStart) {
                            Label(
                                "Switch account",
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                        }
                        Button(
                            onClick = {
                                val result = validateUsername(username)
                                if (result is ValidationResult.Success) {
                                    authVM.signup(accessToken!!, username, object : AuthCallback {
                                        override fun onSuccess() {
                                            context.finish()
                                        }

                                        override fun onFailure(code: Int?) {
                                            println(code)
                                        }

                                    })
                                } else {
                                    validationResult = result
                                }
                            },
                            Modifier.padding(top = 16.dp)
                        ) {
                            Text("Sign up")
                        }
                    }

                    validationResult?.let {
                        if (it is ValidationResult.Error)
                            Label(it.message, Modifier.padding(top = 8.dp), Color.Red)
                    }
                }
                else {
                    GoogleOAuthButton(launcher)
                }

            }
        }
        Label("By using our app, you accept our privacy policy.")
    }
}
@Composable
fun UsernameInputField(
    username: String,
    onUsernameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text("Username") },
        placeholder = { Text("Enter your username") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

fun validateUsername(username: String): ValidationResult {
    return when {
        username.isBlank() -> ValidationResult.Error("Username cannot be empty.")
        username.length < 3 -> ValidationResult.Error("Username must be at least 3 characters long.")
        username.length > 20 -> ValidationResult.Error("Username must be no more than 20 characters long.")
        !username.all { it.isLetterOrDigit() } -> ValidationResult.Error("Username can only contain letters and digits.")
        else -> ValidationResult.Success
    }
}

sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

@Composable
private fun AuthActivityAppBar() {
    val activity = LocalContext.current as? Activity

    TopAppBar("Entry",
        navIcon = AppBarNavIcon(R.drawable.ic_arrow_back, "Close the current page") {
            activity?.run {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    )
}

@Composable
private fun AppLogo(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.img_logo_transparent),
        contentDescription = "Our app logo"
    )
}