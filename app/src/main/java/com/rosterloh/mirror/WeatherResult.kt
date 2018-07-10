package com.rosterloh.mirror

open class WeatherResult {
    open var latitude: Float = 0.0F

    open var longitude: Float = 0.0F

    open var currently: WeatherModel? = null

    open var daily: DailyWeatherModel? = null
}