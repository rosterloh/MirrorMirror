package com.rosterloh.mirror.di.module;

import android.app.Application;

import com.rosterloh.mirror.services.SharedPreferenceService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {

    @Provides
    @Singleton
    public SharedPreferenceService provideSharedPreferenceService(Application application) {

        return new SharedPreferenceService(application);
    }
}