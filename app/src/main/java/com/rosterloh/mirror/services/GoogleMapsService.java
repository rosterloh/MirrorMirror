package com.rosterloh.mirror.services;

import com.rosterloh.mirror.models.google.GoogleMapsResponse;
import com.rosterloh.mirror.models.google.Location;
import com.rosterloh.mirror.models.google.Result;
import com.rosterloh.mirror.util.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class GoogleMapsService {
    private GoogleMapsApi googleMapsApi;

    public GoogleMapsService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.GOOGLE_MAPS_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleMapsApi = retrofit.create(GoogleMapsApi.class);
    }

    public Observable<String> getLatLong(GoogleMapsResponse response) {

        if (response.getResults().isEmpty()) {
            throw new RuntimeException("No matches for your city found.");
        }

        // first result in the list is the best guess
        Result bestResult = response.getResults().get(0);
        Location location = bestResult.getGeometry().getLocation();

        return Observable.just(location.getLat() + "," + location.getLng());
    }

    public GoogleMapsApi getApi() {

        return googleMapsApi;
    }

    public interface GoogleMapsApi {

        @GET("json")
        Observable<GoogleMapsResponse> getLatLongForAddress(@Query("address") String address, @Query("sensor") String sensorBool);
    }
}
