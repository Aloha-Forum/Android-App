package app.aloha.viewmodel

import android.app.Activity.RESULT_CANCELED
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.dataStore
import app.aloha.domain.GoogleOAuth
import app.aloha.internet.service.AuthApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val apiClient: Retrofit,
) : ViewModel() {
    private val dataStore = context.dataStore

    private val authService = AuthorizationService(context)

    val authIntent: Intent
        get() {
            val config = AuthorizationServiceConfiguration(GoogleOAuth.authEndpoint, GoogleOAuth.tokenEndpoint)

            val req = AuthorizationRequest.Builder(config, GoogleOAuth.clientId, ResponseTypeValues.CODE, GoogleOAuth.redirectUri)
                .setScopes("openid", "profile", "email")
                .build()

            return authService.getAuthorizationRequestIntent(req)
        }

    fun exchangeToken(response: AuthorizationResponse, callback: AuthResultCallback) {
        authService.performTokenRequest(response.createTokenExchangeRequest()) { tokenResponse, exception ->
            if (tokenResponse?.accessToken != null) {
                apiClient.create(AuthApiService::class.java)
                    .loginWithGoogle(tokenResponse.accessToken!!)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                setAccessToken(tokenResponse.accessToken!!)
                                callback.onSuccess()
                            }
                            else
                                callback.onFailure()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            callback.onFailure()
                        }
                    })
            }
            else
                callback.onFailure()
        }
    }

    fun getAccessToken() {
        dataStore.data.map { it[stringPreferencesKey("accessToken")] }
    }

    private fun setAccessToken(token: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey("accessToken")] = token
            }
        }
    }
}

interface AuthResultCallback {
    fun onSuccess()
    fun onFailure(message: String? = null)
}