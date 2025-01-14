package app.aloha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import app.aloha.viewmodel.AuthResultCallback
import app.aloha.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationResponse

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val response = AuthorizationResponse.fromIntent(result.data!!)
            if (response != null) {
                viewModel.exchangeToken(response, object : AuthResultCallback {
                    override fun onSuccess() {
                        setResult(RESULT_OK)
                        finish()
                    }

                    override fun onFailure(message: String?) {
                        setResult(RESULT_CANCELED)
                        finish()
                    }
                })
                return@registerForActivityResult
            }
        }
        setResult(RESULT_CANCELED)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher.launch(viewModel.authIntent)
    }
}