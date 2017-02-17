package com.rosterloh.mirror.networking;

import android.content.Context;

import com.rosterloh.mirror.BuildConfig;
import com.rosterloh.mirror.networking.news.NewsAPIService;
import com.rosterloh.mirror.networking.news.NewsRequest;
import com.rosterloh.mirror.networking.news.NewsResponse;
import com.rosterloh.mirror.networking.weather.WeatherAPIService;
import com.rosterloh.mirror.networking.weather.WeatherRequest;
import com.rosterloh.mirror.networking.weather.WeatherResponse;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MirrorRequestsManager {

    private static MirrorRequestsManager instance;

    private WeatherAPIService weatherAPIService;
    private NewsAPIService newsAPIService;

    private MirrorRequestsManager(Context context) {

        OkHttpClient client;

        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        } else {
            client = new OkHttpClient.Builder().build();
        }

        this.weatherAPIService = new WeatherAPIService(client);
        this.newsAPIService = new NewsAPIService(client);
    }

    public static MirrorRequestsManager getInstance(Context context) {

        synchronized (MirrorRequestsManager.class) {
            if (instance == null) {
                instance = new MirrorRequestsManager(context);
            }

            return instance;
        }
    }

    public boolean isRequestingInformation() {
        return weatherAPIService.isRequestingWeather() || newsAPIService.isRequestingNews();
    }

    public Flowable<Object> getMirrorData() {
        return Flowable.zip(
                getWeather(),
                getNews(),
                new BiFunction<WeatherResponse, NewsResponse, Object>() {
                    @Override
                    public Object apply(WeatherResponse weatherResponse, NewsResponse newsResponse) throws Exception {
                        return null;
                    }
                });
    }

    private Object processMirrorData(WeatherResponse weather, NewsResponse news) {
        return new Object();
    }

    private Flowable<WeatherResponse> getWeather() {
        return weatherAPIService.getWeather(new WeatherRequest("fdf071a8d2e6b76511d9c3ed742b4657", "51.8409,-0.85007"));
    }

    private Flowable<NewsResponse> getNews() {
        return newsAPIService.getNews(new NewsRequest("bbc-news", "top", "3f8941773e204d91ab0bdcf808d57767"));
    }
}
