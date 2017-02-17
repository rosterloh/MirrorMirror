package com.rosterloh.mirror.networking.weather;

import com.rosterloh.mirror.util.Constants;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherAPIService {

    private IWeatherAPI weatherAPI;
    private boolean isRequestingWeather;

    public WeatherAPIService(OkHttpClient client) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.FORECAST_BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.weatherAPI = retrofit.create(IWeatherAPI.class);
    }

    public boolean isRequestingWeather() {
        return isRequestingWeather;
    }

    public Flowable<WeatherResponse> getWeather(WeatherRequest request) {
        return weatherAPI.getWeather(request.getKey(), request.getLatLong())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        isRequestingWeather = true;
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        isRequestingWeather = false;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // do something useful
                    }
                })
                .toFlowable(BackpressureStrategy.BUFFER);
    }
}
