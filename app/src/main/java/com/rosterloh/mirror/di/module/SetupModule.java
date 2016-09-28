package com.rosterloh.mirror.di.module;

import android.app.Application;

import com.rosterloh.mirror.di.PerActivity;
import com.rosterloh.mirror.setup.SetupInteractor;
import com.rosterloh.mirror.setup.SetupInteractorImpl;
import com.rosterloh.mirror.setup.SetupPresenter;
import com.rosterloh.mirror.setup.SetupPresenterImpl;
import com.rosterloh.mirror.services.GoogleMapsService;
import com.rosterloh.mirror.services.SharedPreferenceService;
import com.rosterloh.mirror.setup.SetupView;

import dagger.Module;
import dagger.Provides;

@Module
public class SetupModule {

    public SetupView view;

    public SetupModule(SetupView view) {
        this.view = view;
    }

    @Provides
    @PerActivity
    public SetupInteractor provideSetupInteractor(Application application, SharedPreferenceService preferenceService, GoogleMapsService googleMapsService) {

        return new SetupInteractorImpl(application, preferenceService, googleMapsService);
    }

    @Provides
    @PerActivity
    public SetupPresenter provideSetupPresenter(SetupInteractor interactor) {
        return new SetupPresenterImpl(view, interactor);
    }
}