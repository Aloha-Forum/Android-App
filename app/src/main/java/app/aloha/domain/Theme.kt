package app.aloha.domain

fun getThemeName(value: Boolean?): String {
    return when (value) {
        false -> "Light"
        true -> "Dark"
        else -> "Adapt to System"
    }
}

fun getNextTheme(value: Boolean?): Boolean? {
    return when (value) {
        false -> true
        true -> null
        null -> false
    }
}