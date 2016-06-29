package com.rosterloh.mirror.models;

public class Configuration {

    private boolean celsius;
    private String location;
    private String subreddit;
    private int pollingDelay;
    private String serverAddress;
    private boolean rememberConfig;
    private boolean voiceCommands;

    public static class Builder {

        private boolean celsius;
        private String location;
        private String subreddit;
        private int pollingDelay;
        private String serverAddress;
        private boolean rememberConfig;
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

        public Builder rememberConfig(boolean rememberConfig) {
            this.rememberConfig = rememberConfig;
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
        this.rememberConfig = builder.rememberConfig;
        this.voiceCommands = builder.voiceCommands;
    }

    public boolean isCelsius() {
        return celsius;
    }

    public void setCelsius(boolean celsius) {
        this.celsius = celsius;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public int getPollingDelay() {
        return pollingDelay;
    }

    public String getServerAddress() { return serverAddress; }

    public void setPollingDelay(int pollingDelay) {
        this.pollingDelay = pollingDelay;
    }

    public boolean isVoiceCommands() {
        return voiceCommands;
    }

    public void setVoiceCommands(boolean voiceCommands) {
        this.voiceCommands = voiceCommands;
    }

    public boolean isRememberConfig() {
        return rememberConfig;
    }

    public void setRememberConfig(boolean rememberConfig) {
        this.rememberConfig = rememberConfig;
    }
}
