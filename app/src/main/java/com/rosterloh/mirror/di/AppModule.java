package com.rosterloh.mirror.di;

import android.arch.lifecycle.ViewModelProvider;

import com.rosterloh.mirror.viewmodel.MirrorViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = ViewModelSubComponent.class)
class AppModule {

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(
            ViewModelSubComponent.Builder viewModelSubComponent) {
        return new MirrorViewModelFactory(viewModelSubComponent.build());
    }
}
