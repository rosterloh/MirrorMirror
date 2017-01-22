package com.rosterloh.mirror.data;

import android.support.annotation.NonNull;

import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.api.DarkSky;
import com.rosterloh.mirror.models.forecast.ForecastResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Implementation of the remote data source.
 */
public class RemoteDataSource implements DataSource {

    private static RemoteDataSource INSTANCE;

    private DarkSky darkSky;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }
    // Prevent direct instantiation.
    private RemoteDataSource() {
        darkSky = new DarkSky();
    }

    @Override
    public Observable<Weather> getWeather() {

        return Observable.empty()
                .flatMap(new Function<Object, Observable<ForecastResponse>>() {
                    @Override
                    public Observable<ForecastResponse> apply(Object o) throws Exception {
                        return darkSky.getApi().getCurrentWeatherConditions("fdf071a8d2e6b76511d9c3ed742b4657", "51.8409,-0.85007");
                    }
                })
                .flatMap(new Function<ForecastResponse, Observable<Weather>>() {
                    @Override
                    public Observable<Weather> apply(ForecastResponse response) throws Exception {
                        return darkSky.getCurrentWeather(response);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
    }

    @Override
    public void saveWeather(@NonNull Weather weather) {

    }

    @Override
    public void refreshData() {
        // Not required because the {@link DataRepository} handles the logic of refreshing the
        // data from all the available data sources.
    }
}
