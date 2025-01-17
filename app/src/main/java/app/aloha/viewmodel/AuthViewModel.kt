package app.aloha.viewmodel

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.dataStore
import app.aloha.domain.GoogleOAuth
import app.aloha.internet.model.User
import app.aloha.internet.service.AuthApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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

    private val service = AuthorizationService(context)

    val authIntent: Intent
        get() {
            val config = AuthorizationServiceConfiguration(GoogleOAuth.authEndpoint, GoogleOAuth.tokenEndpoint)

            val req = AuthorizationRequest.Builder(config, GoogleOAuth.clientId, ResponseTypeValues.CODE, GoogleOAuth.redirectUri)
                .setScopes("openid", "profile", "email")
                .build()

            return service.getAuthorizationRequestIntent(req)
        }

    fun getAccessToken(res: AuthorizationResponse, callBack: TokenCallback) {
        service.performTokenRequest(res.createTokenExchangeRequest()) { serviceRes, e ->
            if (serviceRes?.accessToken != null)
                callBack.onSuccess(serviceRes.accessToken!!)
            else
                callBack.onFailure(e.toString())
        }
    }

    fun login(token: String, callback: LoginCallback) {
        apiClient.create(AuthApiService::class.java)
            .login(token)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        callback.onSuccess()
                        setUserData(response.body()!!, token)
                    }
                    else
                        callback.onFailure()
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    callback.onFailure()
                }
            })
    }

    fun signup(token: String, uid: String, callback: SignupCallback) {
        apiClient.create(AuthApiService::class.java)
            .signup(token, uid)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        callback.onSuccess()
                        setUserData(response.body()!!, token)
                    }
                    else
                        callback.onFailure()
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    callback.onFailure()
                }

            })
    }

    fun getAccessToken() {
        dataStore.data.map { it[stringPreferencesKey("accessToken")] }
    }

    fun getUserData() {
        dataStore.data.map {
            User(
                it[stringPreferencesKey("id")]!!,
                it[stringPreferencesKey("email")]!!
            )
        }
    }

    private fun setUserData(user: User, token: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey("accessToken")] = token
                preferences[stringPreferencesKey("email")] = user.email
                preferences[stringPreferencesKey("uid")] = user.id
            }
        }
    }
}

interface TokenCallback {
    fun onSuccess(token: String)
    fun onFailure(message: String? = null)
}

interface LoginCallback {
    fun onSuccess()
    fun onFailure(message: String? = null)
}

interface SignupCallback {
    fun onSuccess()
    fun onFailure(message: String? = null)
}