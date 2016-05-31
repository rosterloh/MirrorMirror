package com.rosterloh.mirror.di.module;

import android.app.Application;

import com.rosterloh.mirror.services.ForecastIOService;
import com.rosterloh.mirror.services.GoogleCalendarService;
import com.rosterloh.mirror.services.GoogleMapsService;
import com.rosterloh.mirror.services.RedditService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    public ForecastIOService provideForecastIOService() {

        return new ForecastIOService();
    }

    @Provides
    @Singleton
    public GoogleCalendarService provideGoogleCalendarService(Application application) {

        return new GoogleCalendarService(application);
    }

    @Provides
    @Singleton
    public GoogleMapsService provideGoogleMapService() {

        return new GoogleMapsService();
    }

    @Provides
    @Singleton
    public RedditService redditService() {

        return new RedditService();
    }

}