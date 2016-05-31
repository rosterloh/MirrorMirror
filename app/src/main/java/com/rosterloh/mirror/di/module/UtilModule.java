package com.rosterloh.mirror.di.module;

import com.rosterloh.mirror.models.Configuration;
import com.rosterloh.mirror.util.ASFObjectStore;
import com.rosterloh.mirror.util.WeatherIconGenerator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilModule {

    @Provides
    @Singleton
    public WeatherIconGenerator provideWeatherIconGenerator() {

        return new WeatherIconGenerator();
    }

    @Provides
    @Singleton
    public ASFObjectStore<Configuration> provideASFObjectStore() {

        return new ASFObjectStore<>();
    }
}