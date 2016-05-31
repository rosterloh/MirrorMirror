package com.rosterloh.mirror.models.forecast;

@SuppressWarnings("unused")
public class ForecastResponse {

    private Currently currently;
    private Forecast daily;

    public Currently getCurrently() {
        return currently;
    }

    public Forecast getForecast() {
        return daily;
    }

    public Forecast getDaily() {
        return daily;
    }
}
