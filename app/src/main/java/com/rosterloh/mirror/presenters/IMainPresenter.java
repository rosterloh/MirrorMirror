package com.rosterloh.mirror.presenters;

public interface IMainPresenter {

    void loadLatestCalendarEvent(int updateDelay);

    void loadWeather(final String location, boolean celsius, int updateDelay);

    void loadTopRedditPost(final String subreddit, int updateDelay);

    void unSubscribe();

    void setupRecognitionService();

    void processCommand(String command);
}
