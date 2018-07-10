package com.rosterloh.mirror

import androidx.annotation.DrawableRes

open class WeatherModel {
    open var time: Long = 0

    open var temperature: Double = 0.0

    open var temperatureHigh: Double = 0.0

    open var temperatureLow: Double = 0.0

    open var humidity: Float = 0.0f

    open var uvIndex: Int = 0

    open var icon: String? = null

    open var summary: String = ""

    @DrawableRes
    fun getIconDrawable(): Int {
        if (icon != null) {
            when (icon) {
                "clear-day" -> return R.drawable.clear_day
                "clear-night" -> return R.drawable.clear_night
                "rain" -> return R.drawable.rain
                "snow" -> return R.drawable.snow
                "sleet" -> return R.drawable.sleet
                "wind" -> return R.drawable.wind
                "fog" -> return R.drawable.fog
                "cloudy" -> return R.drawable.cloudy
                "partly-cloudy-day" -> return R.drawable.partly_cloudy_day
                "partly-cloudy-night" -> return R.drawable.partly_cloudy_night
            }
        }

        return R.drawable.unknown
    }
}