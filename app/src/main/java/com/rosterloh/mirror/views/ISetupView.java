package com.rosterloh.mirror.views;

public interface ISetupView {

    void navigateToMainActivity(String location, String subreddit, int pollingDelay, String server,
                                boolean celsius, boolean voiceCommands, boolean rememberConfig, boolean simpleLayout);
}
