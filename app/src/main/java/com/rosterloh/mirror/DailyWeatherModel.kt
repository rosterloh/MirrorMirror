package com.rosterloh.mirror

open class DailyWeatherModel {
    open var summary: String = ""

    open var icon: String? = null

    open var data: List<WeatherModel>? = null
}