package com.rosterloh.mirror.models.google;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GoogleMapsResponse {

    private List<Result> results = new ArrayList<>();

    public List<Result> getResults() {
        return results;
    }
}
