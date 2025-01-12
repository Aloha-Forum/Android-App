package app.aloha.domain

import java.text.DateFormat
import java.text.SimpleDateFormat
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