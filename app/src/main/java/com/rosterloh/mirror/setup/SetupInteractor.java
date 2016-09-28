package com.rosterloh.mirror.setup;

import com.rosterloh.mirror.models.Configuration;

import io.reactivex.observers.DisposableObserver;

public interface SetupInteractor {

    void validate(String location,
                  String subreddit,
                  String pollingDelay,
                  String serverAddress,
                  String serverPort,
                  boolean voiceCommands,
                  DisposableObserver<Configuration> configurationSubscriber);

    void start(DisposableObserver<Configuration> configurationSubscriber);
}
