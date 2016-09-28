package com.rosterloh.mirror.setup;

import com.rosterloh.mirror.models.Configuration;

public interface SetupPresenter {

    void showError(String error);

    void validate(String location,
                  String subreddit,
                  String pollingDelay,
                  String serverAddress,
                  String serverPort,
                  boolean voiceCommands);

    void launchMainActivity(Configuration configuration);
}
