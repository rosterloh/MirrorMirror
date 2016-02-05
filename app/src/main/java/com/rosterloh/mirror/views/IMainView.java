package com.rosterloh.mirror.views;

import com.rosterloh.mirror.models.CurrentWeather;
import com.rosterloh.mirror.models.RedditPost;

import java.io.File;
import java.io.IOException;

public interface IMainView {

    void displayCurrentWeather(CurrentWeather currentWeather);

    void displayLatestCalendarEvent(String event);

    void displayTopRedditPost(RedditPost redditPost);

    void showContent(int which);

    void onError(String message);

    void setupRecognizer(File assetDir) throws IOException;

    void setListeningMode(String mode);

    void startPolling();

    void talk(String message);
}
