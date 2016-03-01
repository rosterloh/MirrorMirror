package com.rosterloh.mirror.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.rosterloh.mirror.MirrorApplication;
import com.rosterloh.mirror.util.Constants;

public class SharedPreferenceService {

    private static SharedPreferenceService sInstance;
    private SharedPreferences mPrefs;

    private SharedPreferenceService() {

    }

    public static SharedPreferenceService instance() {
        if (sInstance == null) {
            sInstance = new SharedPreferenceService();
        }

        return sInstance;
    }

    private SharedPreferences getPreferencesManager() {

        if (mPrefs == null) {
            mPrefs = MirrorApplication.getContext().getSharedPreferences("saveState", Context.MODE_PRIVATE);
        }

        return mPrefs;
    }

    public void storeConfiguration(String location, String subreddit, int pollingDelay,
                                   String server, boolean celsius, boolean voiceCommands,
                                   boolean rememberConfiguration, boolean simpleLayout) {

        SharedPreferences.Editor editor = getPreferencesManager().edit();

        editor.putString(Constants.SP_LOCATION_IDENTIFIER, location);
        editor.putString(Constants.SP_SUBREDDIT_IDENTIFIER, subreddit);
        editor.putInt(Constants.SP_POLLING_IDENTIFIER, pollingDelay);
        editor.putString(Constants.SP_SERVER_IDENTIFIER, server);
        editor.putBoolean(Constants.SP_CELSIUS_IDENTIFIER, celsius);
        editor.putBoolean(Constants.SP_VOICE_IDENTIFIER, voiceCommands);
        editor.putBoolean(Constants.SP_REMEMBER_IDENTIFIER, rememberConfiguration);
        editor.putBoolean(Constants.SP_LAYOUT_IDENTIFIER, simpleLayout);

        editor.apply();
    }

    public String getLocation() {
        return getPreferencesManager().getString(Constants.SP_LOCATION_IDENTIFIER, null);
    }

    public String getSubreddit() {
        return getPreferencesManager().getString(Constants.SP_SUBREDDIT_IDENTIFIER, null);
    }

    public int getPollingDelay() {
        return getPreferencesManager().getInt(Constants.SP_POLLING_IDENTIFIER, 0);
    }

    public String getServerAddress() {
        return getPreferencesManager().getString(Constants.SP_SERVER_IDENTIFIER, null);
    }

    public boolean getCelsius() {
        return getPreferencesManager().getBoolean(Constants.SP_CELSIUS_IDENTIFIER, false);
    }

    public boolean getVoiceCommands() {
        return getPreferencesManager().getBoolean(Constants.SP_VOICE_IDENTIFIER, false);
    }

    public boolean getRememberConfiguration() {
        return getPreferencesManager().getBoolean(Constants.SP_REMEMBER_IDENTIFIER, false);
    }
    public boolean getSimpleLayout() {
        return getPreferencesManager().getBoolean(Constants.SP_LAYOUT_IDENTIFIER, false);
    }

    public void removeConfiguration() {

        SharedPreferences.Editor editor = getPreferencesManager().edit();

        editor.remove(Constants.SP_LOCATION_IDENTIFIER);
        editor.remove(Constants.SP_SUBREDDIT_IDENTIFIER);
        editor.remove(Constants.SP_POLLING_IDENTIFIER);
        editor.remove(Constants.SP_SERVER_IDENTIFIER);
        editor.remove(Constants.SP_CELSIUS_IDENTIFIER);
        editor.remove(Constants.SP_VOICE_IDENTIFIER);
        editor.remove(Constants.SP_REMEMBER_IDENTIFIER);
        editor.remove(Constants.SP_LAYOUT_IDENTIFIER);

        editor.apply();
    }
}
