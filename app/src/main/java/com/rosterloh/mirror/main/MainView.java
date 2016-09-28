package com.rosterloh.mirror.main;

import com.rosterloh.mirror.models.RedditPost;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.BaseView;

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
