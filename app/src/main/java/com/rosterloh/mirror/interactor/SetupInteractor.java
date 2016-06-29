package com.rosterloh.mirror.interactor;

import com.rosterloh.mirror.models.Configuration;

import rx.Subscriber;

public interface SetupInteractor {

    void validate(String location, String subreddit, String pollingDelay, String serverAddress, boolean celsius, boolean voiceCommands, boolean rememberConfig, Subscriber<Configuration> configurationSubscriber);

    void start(Subscriber<Configuration> configurationSubscriber);
}
