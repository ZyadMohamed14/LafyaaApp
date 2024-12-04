package com.example.ecommerceapp.features.dashboard.home.domain.model

import android.util.Log
import com.example.ecommerceapp.core.utils.CountdownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date


data class SalesAdUIModel(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    var imageUrl: String? = null,
    val type: String? = null,
    var productId: String? = null,
    var categoryId: String? = null,
    var externalLink: String? = null,
    var endAt: Date? = null
) {

    private var timer: CountdownTimer? = null

    val hours = MutableStateFlow("")
    val minutes = MutableStateFlow("")
    val seconds = MutableStateFlow("")
    val days = MutableStateFlow("")  // Add a StateFlow for days remaining
    fun safeParseInt(value: String): Int {
        return try {
            value.toInt()
        } catch (e: NumberFormatException) {
            0 // Return 0 if the value is empty or not a valid integer
        }
    }

    fun startCountdown() {
        endAt?.let {
            val currentTime = System.currentTimeMillis()
            val remainingTime = it.time - currentTime

            // Convert remaining time to days, hours, minutes, and seconds
            val daysRemaining = remainingTime / (1000 * 60 * 60 * 24)
            val hoursRemaining = (remainingTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
            val minutesRemaining = (remainingTime % (1000 * 60 * 60)) / (1000 * 60)
            val secondsRemaining = (remainingTime % (1000 * 60)) / 1000

            // Update the UI state with the calculated time
            days.value = daysRemaining.toString()
            hours.value = hoursRemaining.toString()
            minutes.value = minutesRemaining.toString()
            seconds.value = secondsRemaining.toString()

            // Start countdown if there's only 1 day remaining
            if (daysRemaining == 1L) {
                startTimer(it)  // Call startTimer when 1 day is left
            }
        }
    }

    private fun startTimer(endAt: Date) {
        // Stop any existing timer
        timer?.stop()

        // Initialize and start a new countdown timer
        timer = CountdownTimer(endAt) { hours, minutes, seconds ->
            this.hours.value = hours.toString()
            this.minutes.value = minutes.toString()
            this.seconds.value = seconds.toString()

            Log.d("CountdownTimer", "hours: $hours, minutes: $minutes, seconds: $seconds")
        }
        timer?.start()
    }

    fun stopCountdown() {
        timer?.stop()
    }
}

enum class SalesAdType {
    PRODUCT, CATEGORY, EXTERNAL_LINK, EMPTY
}