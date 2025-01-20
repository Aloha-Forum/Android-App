package app.aloha.domain

sealed class ResponseResult {
    data object Success : ResponseResult()
    data class Error(val message: String) : ResponseResult()
}

fun validateUsername(username: String): ResponseResult {
    return when {
        username.isBlank() ->
            ResponseResult.Error("Username cannot be empty.")
        username.length < 3 ->
            ResponseResult.Error("Username must be at least 3 characters long.")
        username.length > 20 ->
            ResponseResult.Error("Username must be no more than 20 characters long.")
        !username.all { it.isLetterOrDigit() } ->
            ResponseResult.Error("Username can only contain letters and digits.")
        else -> ResponseResult.Success
    }
}