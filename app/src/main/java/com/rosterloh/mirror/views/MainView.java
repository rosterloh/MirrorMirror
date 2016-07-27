package com.rosterloh.mirror.views;

import com.rosterloh.mirror.models.RedditPost;
import com.rosterloh.mirror.models.Weather;

public interface MainView extends BaseView {

    void showListening();

    void hideListening();

    void showMap(String location);

    void hideMap();

    void displayCurrentWeather(Weather weather);

    void displayLatestCalendarEvent(String event);

    void displayTopRedditPost(RedditPost redditPost);

    void handleMqttEvent(String payload);
}
