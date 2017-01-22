package com.rosterloh.mirror.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.api.DarkSky;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged;
import io.reactivex.schedulers.Schedulers;

/**
 * Concrete implementation to load data from the data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted data and data
 * obtained from the server, by using the remote data source only if the local database doesn't
 * exist or is empty.
 */
public class DataRepository implements DataSource {

    @Nullable
    private static DataRepository INSTANCE = null;

    @NonNull
    private final DataSource remoteDataSource;

    @NonNull
    private final DataSource localDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    private Object[] cachedData;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean cacheIsDirty = false;

    // Prevent direct instantiation.
    private DataRepository(@NonNull DataSource remote,
                           @NonNull DataSource local) {

        remoteDataSource = remote;
        localDataSource = local;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remote the backend data source
     * @param local  the device storage data source
     * @return the {@link DataRepository} instance
     */
    public static DataRepository getInstance(DataSource remote, DataSource local) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(remote, local);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(DataSource, DataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }


    /**
     * Gets weather from remote data source
     */
    @Override
    public Observable<Weather> getWeather() {
        // Respond immediately with cache if available and not dirty
        if (cachedData != null && cachedData[0] != null && !cacheIsDirty) {
            return Observable.just((Weather) cachedData[0]);
        } else if (cachedData == null) {
            cachedData = new Object[3];
        }

        Observable<Weather> remoteWeather = getAndSaveRemoteWeather();

        if (cacheIsDirty) {
            return remoteWeather;
        } else {
            // Query the local storage if available. If not, query the network.
            Observable<Weather> localWeather = getAndCacheLocalWeather();

            //TODO Figure out concat local + remote and return first not null
            return remoteWeather;
        }
    }

    private Observable<Weather> getAndCacheLocalWeather() {
        return localDataSource
                .getWeather()
                .doOnNext(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {
                        cachedData[0] = weather;
                    }
                });
    }

    private Observable<Weather> getAndSaveRemoteWeather() {
        return remoteDataSource
                .getWeather()
                .doOnNext(new Consumer<Weather>() {
                    @Override
                    public void accept(Weather weather) throws Exception {
                        localDataSource.saveWeather(weather);
                        cachedData[0] = weather;
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        cacheIsDirty = false;
                    }
                });
    }

    @Override
    public void saveWeather(@NonNull Weather weather) {

        remoteDataSource.saveWeather(weather);
        localDataSource.saveWeather(weather);

        // Do in memory cache update to keep the app UI up to date
        if (cachedData == null) {
            cachedData = new Object[3];
        }
        cachedData[0] = weather;
    }


    @Override
    public void refreshData() {
        cacheIsDirty = true;
    }
}
