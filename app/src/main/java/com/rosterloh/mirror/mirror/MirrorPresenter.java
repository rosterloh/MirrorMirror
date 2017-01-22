package com.rosterloh.mirror.mirror;

import android.support.annotation.NonNull;

import com.rosterloh.mirror.data.DataRepository;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.util.Constants;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Listens to user actions from the UI ({@link MirrorActivity}), retrieves the data and updates the
 * UI as required.
 */
public class MirrorPresenter implements MirrorContract.Presenter {

    private final DataRepository dataRepository;

    private final MirrorContract.View mirrorView;

    public MirrorPresenter(@NonNull DataRepository dataRepository, @NonNull MirrorContract.View view) {

        this.dataRepository = dataRepository;
        mirrorView = view;

        mirrorView.setPresenter(this);
    }

    @Override
    public void start() {

        Observable.interval(0, Integer.valueOf(Constants.POLLING_DELAY_DEFAULT), TimeUnit.MINUTES)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        dataRepository.getWeather().subscribe(new Consumer<Weather>() {
                            @Override
                            public void accept(Weather weather) throws Exception {
                                if (!mirrorView.isActive()) {
                                    return;
                                }
                                mirrorView.setLoadingIndicator(true);
                                mirrorView.showWeather(weather);
                                mirrorView.setLoadingIndicator(false);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mirrorView.showLoadingDataError();
                            }
                        });
                    }
                })
                .subscribe();
    }

    @Override
    public void result(int requestCode, int resultCode) {
    }

}
