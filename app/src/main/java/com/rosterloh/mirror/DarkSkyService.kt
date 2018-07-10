package com.rosterloh.mirror

import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DarkSkyService {
    @GET("/forecast/{api_key}/{lat_lon}?exclude=flags,alerts,minutely,hourly")
    fun getForecast(@Path("api_key") apiKey: String, @Path("lat_lon") latLon: String,
                    @Query("units") units: String): Deferred<Response<WeatherResult>>
}