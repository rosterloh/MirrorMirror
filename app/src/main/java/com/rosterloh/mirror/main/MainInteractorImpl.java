package com.rosterloh.mirror.main;

import android.app.Application;

import com.rosterloh.mirror.models.RedditPost;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.services.ForecastIOService;
import com.rosterloh.mirror.services.GoogleCalendarService;
import com.rosterloh.mirror.services.MqttService;
import com.rosterloh.mirror.services.RedditService;
import com.rosterloh.mirror.util.Constants;
import com.rosterloh.mirror.util.WeatherIconGenerator;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import edu.cmu.pocketsphinx.Assets;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainInteractorImpl implements MainInteractor {

    private Application application;
    private ForecastIOService forecastIOService;
    private GoogleCalendarService googleCalendarService;
    private RedditService redditService;
    private WeatherIconGenerator weatherIconGenerator;
    private CompositeDisposable compositeDisposable;
    private MqttService mqttService;

    public MainInteractorImpl(Application application,
                              ForecastIOService forecastIOService,
                              GoogleCalendarService googleCalendarService,
                              RedditService redditService,
                              WeatherIconGenerator weatherIconGenerator,
                              MqttService mqttService) {

        this.application = application;
        this.forecastIOService = forecastIOService;
        this.googleCalendarService = googleCalendarService;
        this.redditService = redditService;
        this.weatherIconGenerator = weatherIconGenerator;
        this.compositeDisposable = new CompositeDisposable();
        this.mqttService = mqttService;
    }

    @Override
    public void loadLatestCalendarEvent(int updateDelay, DisposableObserver<String> subscriber) {

        compositeDisposable.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
            .flatMap(ignore -> googleCalendarService.getLatestCalendarEvent())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .subscribeWith(subscriber));
    }

    @Override
    public void loadTopRedditPost(String subreddit,
                                  int updateDelay,
                                  DisposableObserver<RedditPost> subscriber) {

        compositeDisposable.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
            .flatMap(ignore -> redditService.getApi().getTopRedditPostForSubreddit(subreddit, Constants.REDDIT_LIMIT))
            .flatMap(redditService::getRedditPost)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .subscribeWith(subscriber));
    }

    @Override
    public void loadWeather(String location,
                            int updateDelay,
                            String apiKey,
                            DisposableObserver<Weather> subscriber) {

        compositeDisposable.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
            .flatMap(ignore -> forecastIOService.getApi().getCurrentWeatherConditions(apiKey, location, Constants.WEATHER_QUERY_SECOND_CELSIUS))
            .flatMap(response -> forecastIOService.getCurrentWeather(response, weatherIconGenerator, application))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .subscribeWith(subscriber));
    }

    @Override
    public void getAssetsDirForSpeechRecognizer(DisposableObserver<File> subscriber) {

        Observable.defer(() -> {
                try {
                    Assets assets = new Assets(application);
                    File assetDir = assets.syncAssets();
                    return Observable.just(assetDir);
                } catch (IOException e) {
                    throw new RuntimeException("IOException: " + e.getLocalizedMessage());
                }
            })
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(subscriber);
    }

    @Override
    public void unSubscribe() {

        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public MqttService getMqtt() {

        return this.mqttService;
    }

    @Override
    public void addMqttSubscriber(DisposableObserver<String> subscriber) {

        compositeDisposable.add(this.mqttService.observableListenerWrapper()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(subscriber));
    }
}
