package com.rosterloh.mirror.interactor;

import com.rosterloh.mirror.models.RedditPost;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.services.MqttService;

import java.io.File;

import io.reactivex.observers.DisposableObserver;

public interface MainInteractor {

    void loadLatestCalendarEvent(int updateDelay, DisposableObserver<String> subscriber);

    void loadTopRedditPost(String subreddit, int updateDelay, DisposableObserver<RedditPost> subscriber);

    void loadWeather(String location, int updateDelay, String apiKey, DisposableObserver<Weather> subscriber);

    void getAssetsDirForSpeechRecognizer(DisposableObserver<File> subscriber);

    void unSubscribe();

    MqttService getMqtt();

    void addMqttSubscriber(DisposableObserver<String> subscriber);
}