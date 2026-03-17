package com.example.weatherapp.utils.constants

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
@RequiresApi(Build.VERSION_CODES.O)

object TimeUtils {

        fun calculateDuration(from: String, to: String): String {
            val fromParts = from.split(":")
            val toParts = to.split(":")

            val fromMinutes = fromParts[0].toInt() * 60 + fromParts[1].toInt()
            val toMinutes = toParts[0].toInt() * 60 + toParts[1].toInt()

            val diffMinutes = toMinutes - fromMinutes
            val hours = diffMinutes / 60
            val minutes = diffMinutes % 60

            return when {
                hours > 0 && minutes > 0 -> "$hours h $minutes min"
                hours > 0 -> "$hours h"
                else -> "$minutes min"
            }
        }
     fun calculateDelay(from: String): Long {
        val now = LocalTime.now().withSecond(0).withNano(0)
        val target = LocalTime.parse(from)
        val nowSeconds = now.toSecondOfDay()
        val targetSeconds = target.toSecondOfDay()
        return targetSeconds - nowSeconds.toLong() }
}