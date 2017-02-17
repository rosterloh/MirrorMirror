package com.rosterloh.mirror.networking.news;

import com.google.gson.annotations.SerializedName;

public class NewsRequest {

    @SerializedName("source") private String source;

    @SerializedName("sortBy") private String sort;

    @SerializedName("apiKey") private String key;

    public NewsRequest(String source, String sort, String key) {
        this.source = source;
        this.sort = sort;
        this.key = key;
    }

    public String getSource() {
        return source;
    }

    public String getSort() {
        return sort;
    }

    public String getKey() {
        return key;
    }
}
