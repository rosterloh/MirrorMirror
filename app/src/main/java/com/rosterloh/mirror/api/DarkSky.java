package com.rosterloh.mirror.api;

import android.os.Build;

import com.rosterloh.mirror.BuildConfig;
import com.rosterloh.mirror.models.ForecastDayWeather;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.models.forecast.Datum;
import com.rosterloh.mirror.models.forecast.ForecastResponse;
import com.rosterloh.mirror.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class DarkSky {

    private DarkSkyApi darkSkyApi;

    public DarkSky() {

        OkHttpClient client;

        if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        } else {
            client = new OkHttpClient.Builder().build();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.FORECAST_BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        darkSkyApi = retrofit.create(DarkSkyApi.class);
    }

    public Observable<Weather> getCurrentWeather(ForecastResponse response) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("d MMM", Locale.getDefault());

        // Convert degrees to cardinal directions for wind
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
        String direction = directions[Math.round((response.getCurrently().getWindBearing() % 360) / 45)];

        List<ForecastDayWeather> forecast = new ArrayList<>();

        int AMOUNT_OF_DAYS_IN_FORECAST = 4;
        for (int i = 0; i < AMOUNT_OF_DAYS_IN_FORECAST; i++) {
            Datum f = response.getDaily().getData().get(i+1);
            String date = dateFormatter.format(new Date((long) f.getTime() * 1000));
            int intTemp = (f.getTemperatureMin().intValue() + f.getTemperatureMax().intValue()) / 2;
            String temp = intTemp + "ºC";
            String iconId = f.getIcon();
            forecast.add(new ForecastDayWeather(iconId, temp, i == 0 ? "Tomorrow" : date));
        }

        return Observable.just(new Weather.Builder()
                .iconId(response.getCurrently().getIcon())
                .summary(response.getCurrently().getSummary())
                .temperature(response.getCurrently().getTemperature().intValue() + "ºC")
                .lastUpdated(new SimpleDateFormat("H:mm", Locale.getDefault()).format(new Date((long) response.getCurrently().getTime() * 1000)))
                .windInfo(response.getCurrently().getWindSpeed().intValue() + "km/h " + direction + " | " + response.getCurrently().getApparentTemperature().intValue() + "ºC")
                .humidityInfo((int) (response.getCurrently().getHumidity() * 100) + "%")
                .pressureInfo(response.getCurrently().getPressure().intValue() + "mb")
                .visibilityInfo(response.getCurrently().getVisibility().intValue() + "km")
                .forecast(forecast)
                .build());
    }

    public DarkSkyApi getApi() {

        return darkSkyApi;
    }

    public interface DarkSkyApi {

        @GET("{apiKey}/{latLong}?units=si")
        Observable<ForecastResponse> getCurrentWeatherConditions(@Path("apiKey") String apiKey, @Path("latLong") String latLong);
    }}
