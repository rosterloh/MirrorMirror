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
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainInteractorImpl implements MainInteractor {

    private Application application;
    private ForecastIOService forecastIOService;
    private GoogleCalendarService googleCalendarService;
    private RedditService redditService;
    private WeatherIconGenerator weatherIconGenerator;
    private CompositeSubscription compositeSubscription;
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
        this.compositeSubscription = new CompositeSubscription();
        this.mqttService = mqttService;
    }

    @Override
    public void loadLatestCalendarEvent(int updateDelay, Subscriber<String> subscriber) {

        compositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
            .flatMap(ignore -> googleCalendarService.getLatestCalendarEvent())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber));
    }

    @Override
    public void loadTopRedditPost(String subreddit,
                                  int updateDelay,
                                  Subscriber<RedditPost> subscriber) {

        compositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
            .flatMap(ignore -> redditService.getApi().getTopRedditPostForSubreddit(subreddit, Constants.REDDIT_LIMIT))
            .flatMap(redditService::getRedditPost)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber));
    }

    @Override
    public void loadWeather(String location,
                            int updateDelay,
                            String apiKey,
                            Subscriber<Weather> subscriber) {

        compositeSubscription.add(Observable.interval(0, updateDelay, TimeUnit.MINUTES)
            .flatMap(ignore -> forecastIOService.getApi().getCurrentWeatherConditions(apiKey, location, Constants.WEATHER_QUERY_SECOND_CELSIUS))
            .flatMap(response -> forecastIOService.getCurrentWeather(response, weatherIconGenerator, application))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber));
    }

    @Override
    public void getAssetsDirForSpeechRecognizer(Subscriber<File> subscriber) {

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
            .subscribe(subscriber);
    }

    @Override
    public void unSubscribe() {

        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public MqttService getMqtt() {

        return this.mqttService;
    }

    @Override
    public void addMqttSubscriber(Subscriber<String> subscriber) {

        compositeSubscription.add(this.mqttService.observableListenerWrapper()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }
}
