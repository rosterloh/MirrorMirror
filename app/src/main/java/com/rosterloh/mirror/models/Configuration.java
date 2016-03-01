package com.rosterloh.mirror.models;

import java.io.Serializable;

public class Configuration implements Serializable {

    private boolean celsius;
    private String location;
    private String subreddit;
    private int pollingDelay;
    private String serverAddress;
    private boolean voiceCommands;
    private boolean simpleLayout;

    public static class Builder {

        private boolean celsius;
        private String location;
        private String subreddit;
        private int pollingDelay;
        private String serverAddress;
        private boolean voiceCommands;
        private boolean simpleLayout;

        public Builder celsius(boolean celsius) {
            this.celsius = celsius;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder subreddit(String subreddit) {
            this.subreddit = subreddit;
            return this;
        }

        public Builder pollingDelay(int pollingDelay) {
            this.pollingDelay = pollingDelay;
            return this;
        }

        public Builder serverAddress(String serverAddress) {
            this.serverAddress = serverAddress;
            return this;
        }

        public Builder voiceCommands(boolean voiceCommands) {
            this.voiceCommands = voiceCommands;
            return this;
        }

        public Builder simpleLayout(boolean simpleLayout) {
            this.simpleLayout = simpleLayout;
            return this;
        }

        public Configuration build() {

            return new Configuration(this);
        }
    }

    private Configuration(Builder builder) {

        this.celsius = builder.celsius;
        this.location = builder.location;
        this.subreddit = builder.subreddit;
        this.pollingDelay = builder.pollingDelay;
        this.serverAddress = builder.serverAddress;
        this.voiceCommands = builder.voiceCommands;
        this.simpleLayout = builder.simpleLayout;
    }

    public boolean isCelsius() {
        return celsius;
    }

    public String getLocation() {
        return location;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public int getPollingDelay() {
        return pollingDelay;
    }

    public String getServerAddress() { return serverAddress; }

    public boolean getVoiceCommands() {
        return voiceCommands;
    }

    public boolean isSimpleLayout() {
        return simpleLayout;
    }
}
