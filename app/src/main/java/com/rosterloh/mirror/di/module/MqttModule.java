package com.rosterloh.mirror.di.module;

import android.app.Application;

import com.rosterloh.mirror.services.MqttService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MqttModule {

    @Provides
    @Singleton
    public MqttService provideMqttService(Application application) {

        return new MqttService(application);
    }
}
