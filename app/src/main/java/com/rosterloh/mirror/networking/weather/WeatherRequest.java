package com.rosterloh.mirror.networking.weather;

import com.google.gson.annotations.SerializedName;

public class WeatherRequest {

    @SerializedName("apiKey") private String key;

    @SerializedName("latLong") private String latlon;

    public WeatherRequest(String key, String latlon) {
        this.key = key;
        this.latlon = latlon;
    }

    public String getKey() {
        return key;
    }

    public String getLatLong() {
        return latlon;
    }
}
