package com.rosterloh.mirror.interactor;

import com.rosterloh.mirror.models.RedditPost;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.services.MqttService;

import java.io.File;

import rx.Subscriber;

public interface MainInteractor {

    void loadLatestCalendarEvent(int updateDelay, Subscriber<String> subscriber);

    void loadTopRedditPost(String subreddit, int updateDelay, Subscriber<RedditPost> subscriber);

    void loadWeather(String location, boolean celsius, int updateDelay, String apiKey, Subscriber<Weather> subscriber);

    void getAssetsDirForSpeechRecognizer(Subscriber<File> subscriber);

    void unSubscribe();

    MqttService getMqtt();
}