package com.rosterloh.mirror.services;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rosterloh.mirror.models.Configuration;
import com.rosterloh.mirror.util.Constants;

public class SharedPreferenceService {

    private SharedPreferences preferences;

    public SharedPreferenceService(Application application) {

        this.preferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public void storeConfiguration(Configuration configuration) {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(Constants.SP_LOCATION_IDENTIFIER, configuration.getLocation());
        editor.putString(Constants.SP_SUBREDDIT_IDENTIFIER, configuration.getSubreddit());
        editor.putInt(Constants.SP_POLLING_IDENTIFIER, configuration.getPollingDelay());
        editor.putString(Constants.SP_SERVER_IDENTIFIER, configuration.getServerAddress());
        editor.putBoolean(Constants.SP_CELSIUS_IDENTIFIER, configuration.isCelsius());
        editor.putBoolean(Constants.SP_VOICE_IDENTIFIER, configuration.isVoiceCommands());
        editor.putBoolean(Constants.SP_REMEMBER_IDENTIFIER, configuration.isRememberConfig());

        editor.apply();
    }

    public Configuration getRememberedConfiguration() {

        Configuration configuration = new Configuration.Builder()
                .location(getLocation())
                .subreddit(getSubreddit())
                .pollingDelay(getPollingDelay())
                .serverAddress(getServerAddress())
                .celsius(getCelsius())
                .rememberConfig(getRememberConfiguration())
                .voiceCommands(getVoiceCommands())
                .build();

        return configuration;
    }

    public String getLocation() {
        return preferences.getString(Constants.SP_LOCATION_IDENTIFIER, null);
    }

    public String getSubreddit() {
        return preferences.getString(Constants.SP_SUBREDDIT_IDENTIFIER, null);
    }

    public int getPollingDelay() {
        return preferences.getInt(Constants.SP_POLLING_IDENTIFIER, 0);
    }

    public String getServerAddress() {
        return preferences.getString(Constants.SP_SERVER_IDENTIFIER, null);
    }

    public boolean getCelsius() {
        return preferences.getBoolean(Constants.SP_CELSIUS_IDENTIFIER, false);
    }

    public boolean getVoiceCommands() {
        return preferences.getBoolean(Constants.SP_VOICE_IDENTIFIER, false);
    }

    public boolean getRememberConfiguration() {
        return preferences.getBoolean(Constants.SP_REMEMBER_IDENTIFIER, false);
    }

    public void removeConfiguration() {

        SharedPreferences.Editor editor = preferences.edit();

        editor.remove(Constants.SP_LOCATION_IDENTIFIER);
        editor.remove(Constants.SP_SUBREDDIT_IDENTIFIER);
        editor.remove(Constants.SP_POLLING_IDENTIFIER);
        editor.remove(Constants.SP_SERVER_IDENTIFIER);
        editor.remove(Constants.SP_CELSIUS_IDENTIFIER);
        editor.remove(Constants.SP_VOICE_IDENTIFIER);
        editor.remove(Constants.SP_REMEMBER_IDENTIFIER);

        editor.apply();
    }
}
