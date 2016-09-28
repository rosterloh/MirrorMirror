package com.rosterloh.mirror.di.module;

import android.app.Application;

import com.rosterloh.mirror.di.PerActivity;
import com.rosterloh.mirror.main.MainInteractor;
import com.rosterloh.mirror.main.MainInteractorImpl;
import com.rosterloh.mirror.main.MainPresenter;
import com.rosterloh.mirror.main.MainPresenterImpl;
import com.rosterloh.mirror.services.ForecastIOService;
import com.rosterloh.mirror.services.GoogleCalendarService;
import com.rosterloh.mirror.services.MqttService;
import com.rosterloh.mirror.services.RedditService;
import com.rosterloh.mirror.util.WeatherIconGenerator;
import com.rosterloh.mirror.main.MainView;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private MainView mainView;

    public MainModule(MainView mainView) {

        this.mainView = mainView;
    }

    @Provides
    @PerActivity
    public MainPresenter provideMainPresenter(MainInteractor interactor, Application application) {

        return new MainPresenterImpl(mainView, interactor, application);
    }

    @Provides
    @PerActivity
    public MainInteractor provideMainInteractor(Application application,
                                                ForecastIOService forecastIOService,
                                                GoogleCalendarService googleMapService,
                                                RedditService redditService,
                                                WeatherIconGenerator iconGenerator,
                                                MqttService mqttService) {

        return new MainInteractorImpl(application, forecastIOService, googleMapService,
                                      redditService, iconGenerator, mqttService);
    }
}
