package com.rosterloh.mirror.interactor;

import android.app.Application;

import com.rosterloh.mirror.models.Configuration;
import com.rosterloh.mirror.services.GoogleMapsService;
import com.rosterloh.mirror.services.SharedPreferenceService;
import com.rosterloh.mirror.util.Constants;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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
    public void start(DisposableObserver<Configuration> configurationSubscriber) {

        Observable.just(preferenceService.getRememberedConfiguration())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(configurationSubscriber);
    }

    @Override
    public void validate(String location,
                         String subreddit,
                         String pollingDelay,
                         String serverAddress,
                         String serverPort,
                         boolean voiceCommands,
                         DisposableObserver<Configuration> configurationSubscriber) {

        googleMapsService.getApi().getLatLongForAddress(location.isEmpty() ? Constants.LOCATION_DEFAULT : location, "false")
            .flatMap(googleMapsService::getLatLong)
            .flatMap(latLng -> generateConfiguration(latLng, subreddit, pollingDelay, serverAddress, serverPort, voiceCommands))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(configurationSubscriber);
    }

    private Observable<Configuration> generateConfiguration(String latLong,
                                                            String subreddit,
                                                            String pollingDelay,
                                                            String serverAddress,
                                                            String serverPort,
                                                            boolean voiceCommands) {

        Configuration configuration = new Configuration.Builder()
            .location(latLong)
            .subreddit(subreddit.isEmpty() ? Constants.SUBREDDIT_DEFAULT : subreddit)
            .pollingDelay((pollingDelay.equals("") || pollingDelay.equals("0")) ? Integer.parseInt(Constants.POLLING_DELAY_DEFAULT) : Integer.parseInt(pollingDelay))
            .serverAddress(serverAddress.isEmpty() ? Constants.SERVER_DEFAULT : serverAddress)
            .serverPort((serverPort.equals("") || serverPort.equals("0")) ? Integer.parseInt(Constants.PORT_DEFAULT) : Integer.parseInt(serverPort))
            .voiceCommands(voiceCommands)
            .build();

        preferenceService.storeConfiguration(configuration);

        return Observable.just(configuration);
    }
}
