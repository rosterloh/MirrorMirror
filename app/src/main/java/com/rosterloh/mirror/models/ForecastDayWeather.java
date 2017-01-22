package com.rosterloh.mirror.models;

public class ForecastDayWeather {

    private String iconId;
    private String temperature;
    private String date;

    public ForecastDayWeather(String iconId, String temperature, String date) {
        this.iconId = iconId;
        this.temperature = temperature;
        this.date = date;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
