package app.aloha.domain

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun timeLagFromCurrent(a: Long): String {
    val lag = System.currentTimeMillis() - a

    if (lag < 60*60*1000) {
        return TimeUnit.MILLISECONDS.toMinutes(lag).toString() + " mins ago"
    }
    else if (lag < 24*60*60*1000) {
        return TimeUnit.MILLISECONDS.toHours(lag).toString() + " hrs ago"
    }
    else if (lag < 3*24*60*60*1000) {
        return TimeUnit.MILLISECONDS. toDays(lag).toString() + " days ago"
    }
    else if (lag < 365*24*60*60*1000L) {
        val f: DateFormat = SimpleDateFormat("dd MMM", Locale("en"))
        return f.format(Date(a))
    }
    val f: DateFormat = SimpleDateFormat("dd MMM yyyy", Locale("en"))
    return f.format(Date(a))
}

fun timeLagFromCurrent(isoDateString: String): String {
    return try {
        // Parse the ISO date string to Date
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        format.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val date = format.parse(isoDateString) ?: return "Invalid date format"

        val lag = System.currentTimeMillis() - date.time

        when {
            lag < 60 * 60 * 1000 -> {
                "${TimeUnit.MILLISECONDS.toMinutes(lag)} mins ago"
            }
            lag < 24 * 60 * 60 * 1000 -> {
                "${TimeUnit.MILLISECONDS.toHours(lag)} hrs ago"
            }
            lag < 3 * 24 * 60 * 60 * 1000 -> {
                val formatter = SimpleDateFormat("dd MMM", Locale.ENGLISH)
                formatter.format(date)
            }
            lag < 365 * 24 * 60 * 60 * 1000L -> {
                val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                formatter.format(date)
            }
            else -> {
                val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
                formatter.format(date)
            }
        }
    } catch (e: ParseException) {
        "Invalid date format"
    }
}