package app.aloha.viewmodel

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.domain.GoogleOAuth
import app.aloha.internet.model.User
import app.aloha.internet.service.AuthApiService
import app.aloha.repository.DataStoreRepo
import app.aloha.repository.PrefKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.combine
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
    private val repo: DataStoreRepo
) : ViewModel() {
    private val service = AuthorizationService(context)

    val authIntent: Intent
        get() {
            val config = AuthorizationServiceConfiguration(GoogleOAuth.authEndpoint, GoogleOAuth.tokenEndpoint)

            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()

            val req = AuthorizationRequest.Builder(config, GoogleOAuth.clientId, ResponseTypeValues.CODE, GoogleOAuth.redirectUri)
                .setScopes("openid", "profile", "email")
                .build()
            customTabsIntent.intent.setPackage("com.android.chrome")
            return service.getAuthorizationRequestIntent(req, customTabsIntent)
        }

    val isLogin = repo.get(PrefKeys.ACCESS_TOKEN).map { it != null }

    val user = combine(repo.get(PrefKeys.USERNAME), repo.get(PrefKeys.EMAIL)) { uid, email ->
        when (uid != null && email != null) {
            true -> User(uid, email)
            false -> null
        }
    }

    fun getAccessToken(res: AuthorizationResponse, callBack: TokenCallback) {
        service.performTokenRequest(res.createTokenExchangeRequest()) { serviceRes, e ->
            if (serviceRes?.accessToken != null)
                callBack.onSuccess(serviceRes.accessToken!!)
            else
                callBack.onFailure(e.toString())
        }
    }

    fun login(token: String, callback: AuthCallback) {
        apiClient.create(AuthApiService::class.java)
            .login(token)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        setUserState(response.body(), token)
                        callback.onSuccess()
                    }
                    else {
                        callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    println(t.message)
                    callback.onFailure()
                }
            })
    }

    fun signup(token: String, uid: String, callback: AuthCallback) {
//        apiClient.create(AuthApiService::class.java)
//            .signup(token, uid)
//            .enqueue(object : Callback<User> {
//                override fun onResponse(call: Call<User>, response: Response<User>) {
//                    if (response.isSuccessful) {
//                        setUserState(response.body()!!, token)
//                        callback.onSuccess()
//                    }
//                    else
//                        callback.onFailure()
//                }
//
//                override fun onFailure(call: Call<User>, t: Throwable) {
//                    callback.onFailure()
//                }
//            })
    }

    fun signOut() {
        setUserState(null, null)
    }

    fun setUserState(user: User?, token: String?) {
        viewModelScope.launch {
            repo.save(PrefKeys.USERNAME, user?.id)
            repo.save(PrefKeys.EMAIL, user?.email)
            repo.save(PrefKeys.ACCESS_TOKEN, token)
        }
    }
}

interface TokenCallback {
    fun onSuccess(token: String)
    fun onFailure(message: String? = null)
}

interface AuthCallback {
    fun onSuccess()
    fun onFailure(code: Int? = null)
}