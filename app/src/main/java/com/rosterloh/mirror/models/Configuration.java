package com.rosterloh.mirror.models;

public class Configuration {

    private String location;
    private String subreddit;
    private int pollingDelay;
    private String serverAddress;
    private int serverPort;
    private boolean voiceCommands;

    public static class Builder {

        private String location;
        private String subreddit;
        private int pollingDelay;
        private String serverAddress;
        private int serverPort;
        private boolean voiceCommands;

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

        public Builder serverPort(int serverPort) {
            this.serverPort = serverPort;
            return this;
        }

        public Builder voiceCommands(boolean voiceCommands) {
            this.voiceCommands = voiceCommands;
            return this;
        }

        public Configuration build() {

            return new Configuration(this);
        }
    }

    private Configuration(Builder builder) {

        this.location = builder.location;
        this.subreddit = builder.subreddit;
        this.pollingDelay = builder.pollingDelay;
        this.serverAddress = builder.serverAddress;
        this.serverPort = builder.serverPort;
        this.voiceCommands = builder.voiceCommands;
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

    public int getServerPort() { return serverPort; }

    public void setPollingDelay(int pollingDelay) {
        this.pollingDelay = pollingDelay;
    }

    public boolean isVoiceCommands() {
        return voiceCommands;
    }

    public void setVoiceCommands(boolean voiceCommands) {
        this.voiceCommands = voiceCommands;
    }
}
