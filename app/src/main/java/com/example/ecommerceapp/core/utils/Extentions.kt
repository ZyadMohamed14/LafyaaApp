package com.example.ecommerceapp.core.utils


import android.os.Build
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun String.isValidEmail():Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
fun String.capitalizeWords(): String {
    return this.split(" ")
        .joinToString(" ") { it.capitalize() }
}
fun String.isValidPhoneNumber(): Boolean {
    // Basic pattern that matches phone numbers with 10-15 digits, allowing optional dashes, spaces, or parentheses
    val phonePattern = "^(\\+\\d{1,3}[- ]?)?\\d{10,15}\$"
    return Regex(phonePattern).matches(this)
}
fun String.toColor(): Color? {
    val regex = Regex("""Color\((\d+\.?\d*), (\d+\.?\d*), (\d+\.?\d*), (\d+\.?\d*), .*\)""")
    val matchResult = regex.matchEntire(this)

    return if (matchResult != null) {
        val (r, g, b, a) = matchResult.destructured
        Color(
            red = r.toFloat(),
            green = g.toFloat(),
            blue = b.toFloat(),
            alpha = a.toFloat()
        )
    } else {
        null // Return null if the string is not in the expected format
    }
}

fun Date.getCurrentFormattedDate(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Use java.time for API 26+
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
        LocalDate.ofEpochDay(this.time / (24 * 60 * 60 * 1000)).format(formatter)
    } else {
        // Use SimpleDateFormat for older APIs
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        formatter.format(this)
    }
}