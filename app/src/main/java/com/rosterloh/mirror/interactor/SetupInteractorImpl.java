package com.rosterloh.mirror.interactor;

import android.app.Application;

import com.rosterloh.mirror.models.Configuration;
import com.rosterloh.mirror.services.GoogleMapsService;
import com.rosterloh.mirror.services.SharedPreferenceService;
import com.rosterloh.mirror.util.Constants;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SetupInteractorImpl implements SetupInteractor {

    private Application application;
    private SharedPreferenceService preferenceService;
    private GoogleMapsService googleMapsService;

    public SetupInteractorImpl(Application application,
                               SharedPreferenceService preferenceService,
                               GoogleMapsService googleMapsService) {

        this.application = application;
        this.preferenceService = preferenceService;
        this.googleMapsService = googleMapsService;
    }

    @Override
    public void start(Subscriber<Configuration> configurationSubscriber) {

        if (preferenceService.getRememberConfiguration()) {

            Observable.just(preferenceService.getRememberedConfiguration())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(configurationSubscriber);
       }
    }

    @Override
    public void validate(String location,
                         String subreddit,
                         String pollingDelay,
                         String serverAddress,
                         String serverPort,
                         boolean voiceCommands,
                         boolean simpleLayout,
                         Subscriber<Configuration> configurationSubscriber) {

        googleMapsService.getApi().getLatLongForAddress(location.isEmpty() ? Constants.LOCATION_DEFAULT : location, "false")
            .flatMap(googleMapsService::getLatLong)
            .flatMap(latLng -> generateConfiguration(latLng, subreddit, pollingDelay, serverAddress, serverPort, voiceCommands, simpleLayout))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(configurationSubscriber);
    }

    private Observable<Configuration> generateConfiguration(String latLong,
                                                            String subreddit,
                                                            String pollingDelay,
                                                            String serverAddress,
                                                            String serverPort,
                                                            boolean voiceCommands,
                                                            boolean rememberConfig) {

        Configuration configuration = new Configuration.Builder()
            .location(latLong)
            .subreddit(subreddit.isEmpty() ? Constants.SUBREDDIT_DEFAULT : subreddit)
            .pollingDelay((pollingDelay.equals("") || pollingDelay.equals("0")) ? Integer.parseInt(Constants.POLLING_DELAY_DEFAULT) : Integer.parseInt(pollingDelay))
            .serverAddress(serverAddress.isEmpty() ? Constants.SERVER_DEFAULT : serverAddress)
            .serverPort((serverPort.equals("") || serverPort.equals("0")) ? Integer.parseInt(Constants.PORT_DEFAULT) : Integer.parseInt(serverPort))
            .rememberConfig(rememberConfig)
            .voiceCommands(voiceCommands)
            .build();

        if (rememberConfig) {
            preferenceService.storeConfiguration(configuration);
        } else {
            preferenceService.removeConfiguration();
        }

        configuration.setRememberConfig(false);

        return Observable.just(configuration);
    }
}
