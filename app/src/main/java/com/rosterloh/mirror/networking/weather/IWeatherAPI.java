package com.rosterloh.mirror.networking.weather;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IWeatherAPI {

    @GET("{apiKey}/{latLong}?units=si")
    Observable<WeatherResponse> getWeather(@Path("apiKey") String apiKey, @Path("latLong") String latLong);
}
