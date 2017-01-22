package com.rosterloh.mirror.data;

import android.support.annotation.NonNull;

import com.rosterloh.mirror.models.Weather;

import io.reactivex.Observable;

/**
 * Main entry point for accessing application data.
 * <p>
 * For simplicity, only getWeather() has callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when new data is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface DataSource {

    Observable<Weather> getWeather();

    void saveWeather(@NonNull Weather weather);

    void refreshData();
}
