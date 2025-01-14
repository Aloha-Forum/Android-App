package app.aloha.domain

import android.net.Uri

object GoogleOAuth {
    val clientId = "228702697340-18o9anmrcrtjh51nvjuq8nvj6rltcmd0.apps.googleusercontent.com"
    val redirectUri = Uri.parse("app.aloha:/oauth2redirect")
    val authEndpoint = Uri.parse("https://accounts.google.com/o/oauth2/v2/auth")
    val tokenEndpoint = Uri.parse("https://oauth2.googleapis.com/token")
}