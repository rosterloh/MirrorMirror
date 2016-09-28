package com.rosterloh.mirror.setup;

import com.rosterloh.mirror.models.Configuration;

import rx.Subscriber;

public class SetupPresenterImpl implements SetupPresenter {

    private SetupView view;
    private SetupInteractor interactor;

    public SetupPresenterImpl(SetupView view, SetupInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
        interactor.start(new ConfigurationSubscriber());
    }

    /**
     * Validate user settings
     *
     * @param location       could be city/address etc.
     * @param subreddit      subreddit string
     * @param pollingDelay   update UI every x minutes
     * @param serverAddress  address of MQTT broker server
     * @param serverPort     port of MQTT broker server
     * @param voiceCommands  should listen for voice commands
     */
    @Override
    public void validate(String location,
                         String subreddit,
                         String pollingDelay,
                         String serverAddress,
                         String serverPort,
                         boolean voiceCommands) {

        interactor.validate(location,
                subreddit,
                pollingDelay,
                serverAddress,
                serverPort,
                voiceCommands,
                new ConfigurationSubscriber());
    }

    /**
     * Show error message in toast
     *
     * @param error message to show
     */
    @Override
    public void showError(String error) {
        view.showError(error);
    }

    /**
     * Launch main activity with a configuration
     *
     * @param configuration all relevant settings
     */
    @Override
    public void launchMainActivity(Configuration configuration) {

        view.navigateToMainActivity(configuration);
    }

    /**
     * Callback for RxObservables emitted by interactor
     * this callback is used if this is a new configuration or
     * if the configuration was stored in preferences.
     */
    private final class ConfigurationSubscriber extends Subscriber<Configuration> {

        @Override
        public void onStart() {
            view.showLoading();
        }

        @Override
        public void onCompleted() {
            view.hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(Configuration configuration) {
            view.navigateToMainActivity(configuration);
        }
    }
}
