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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import app.aloha.R
import app.aloha.domain.ResponseResult
import app.aloha.domain.validateUsername
import app.aloha.ui.component.AppBarNavIcon
import app.aloha.ui.component.AppLogo
import app.aloha.ui.component.BodyText
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

@Composable
fun LoginPage() {
    val authVM: AuthViewModel = viewModel()
    val activity = LocalContext.current as Activity
    var accessToken by remember { mutableStateOf<String?>(null) }

    var errMsg by remember { mutableStateOf<ResponseResult?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        errMsg = null
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val response = AuthorizationResponse.fromIntent(it.data!!)
            if (response != null) {
                isLoading = true
                authVM.getAccessToken(response, object : TokenCallback {
                        override fun onSuccess(token: String) {
                            authVM.login(token, object : AuthCallback {
                                override fun onSuccess() {
                                    isLoading = false
                                    activity.run {
                                        setResult(RESULT_OK)
                                        finish()
                                    }
                                }

                                override fun onFailure(code: Int?) {
                                    isLoading = false
                                    when (code) {
                                        404 -> {
                                            // user doesn't have an account
                                            // start the process of sign up
                                            accessToken = token
                                        }
                                        null -> {
                                            errMsg = ResponseResult.Error("Error: Timeout")
                                        }
                                        else -> {
                                            errMsg = ResponseResult.Error("Error: status code $code")
                                        }
                                    }
                                }
                            })
                        }

                        override fun onFailure(message: String?) {
                            isLoading = false
                            errMsg = ResponseResult.Error(message ?: "Server didn't response")
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
                if (isLoading) {
                    BodyText("The process may take few seconds...")

                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
                else {
                    Title("Welcome to Aloha Forum")
                    AppLogo()

                    when (accessToken != null) {
                        true -> SignUpSection(
                            accessToken,
                            setToken = { accessToken = it },
                            setError = { errMsg = it },
                            setIsLoading = { isLoading = it }
                        )
                        else -> GoogleOAuthButton(launcher)
                    }

                    errMsg?.let {
                        if (it is ResponseResult.Error)
                            Label(it.message, Modifier.padding(top = 8.dp), Color.Red)
                    }
                }
            }
        }
        PrivacyPolicyStatement(Modifier.fillMaxWidth())
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

@Composable
private fun AuthActivityAppBar() {
    val activity = LocalContext.current as? Activity

    TopAppBar(
        "Entry",
        navIcon = AppBarNavIcon(R.drawable.ic_arrow_back, "Close the current page") {
            activity?.run {
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PrivacyPolicyStatement(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    FlowRow(
        modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                val intent = Intent(context, WebViewActivity::class.java)
                    .putExtra("url", context.resources.getString(R.string.privacy_policy_url))

                context.startActivity(intent)
            }
    ) {
        Label("By using our app, you accept our ")
        Label("privacy policy", color = Color.Blue, textDecoration = TextDecoration.Underline)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SignUpSection(
    token: String?,
    modifier: Modifier = Modifier,
    setToken: (String?) -> Unit,
    setError: (ResponseResult) -> Unit,
    setIsLoading: (Boolean) -> Unit,
) {
    var uid by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UsernameInputField(uid) { uid = it }
        Label("3-20 characters, letters and digits only.")
    }

    FlowRow(
        modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable { setToken(null) },
            Alignment.CenterStart
        ) {
            Label(
                "Switch account",
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        }

        val authVM: AuthViewModel = hiltViewModel()
        val activity = LocalContext.current as? Activity
        Button(
            onClick = {
                val result = validateUsername(uid)
                setError(result)

                if (result is ResponseResult.Success) {
                    setIsLoading(true)

                    authVM.signup(token!!, uid, object : AuthCallback {
                        override fun onSuccess() {
                            activity?.finish()
                        }

                        override fun onFailure(code: Int?) {
                            when (code == null) {
                                true -> ResponseResult.Error("Timeout")
                                false -> ResponseResult.Error(code.toString())
                            }
                        }
                    })
                }
            },
            colors = ButtonColors(
                MaterialTheme.colorScheme.inverseSurface,
                MaterialTheme.colorScheme.inverseOnSurface,
                MaterialTheme.colorScheme.inverseSurface,
                MaterialTheme.colorScheme.inverseOnSurface,
            )
        ) {
            BodyText("Sign up", color = MaterialTheme.colorScheme.inverseOnSurface)
        }
    }
}