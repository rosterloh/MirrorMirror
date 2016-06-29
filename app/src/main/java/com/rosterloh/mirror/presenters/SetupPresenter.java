package com.rosterloh.mirror.presenters;

import com.rosterloh.mirror.models.Configuration;

public interface SetupPresenter {

    void showError(String error);

    void validate(String location, String subreddit, String pollingDelay, String serverAddress, boolean celsius, boolean voiceCommands, boolean rememberConfig);

    void launchMainActivity(Configuration configuration);
}
