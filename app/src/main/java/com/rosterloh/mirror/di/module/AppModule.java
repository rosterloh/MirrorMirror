package com.rosterloh.mirror.di.module;

import android.app.Application;

import com.rosterloh.mirror.MirrorApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private MirrorApplication app;

    public AppModule(MirrorApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }
}