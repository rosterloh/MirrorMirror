package com.rosterloh.mirror.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.rosterloh.mirror.models.Weather;

import io.reactivex.Observable;

/**
 * Concrete implementation of a data source as a db.
 */
public class LocalDataSource implements DataSource {

    private static LocalDataSource INSTANCE;

    private Weather LOCAL_DATA;

    // Prevent direct instantiation.
    private LocalDataSource(@NonNull Context context) {
        LOCAL_DATA = new Weather.Builder().build();
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<Weather> getWeather() {
        return Observable.just(LOCAL_DATA);
    }

    @Override
    public void saveWeather(@NonNull Weather weather) {
        LOCAL_DATA = weather;
    }

    @Override
    public void refreshData() {
        // Not required because the {@link DataRepository} handles the logic of refreshing the
        // data from all the available data sources.
    }
}
