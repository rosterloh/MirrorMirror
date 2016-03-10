package com.rosterloh.mirror.services;

import com.rosterloh.mirror.models.CurrentWeather;
import com.rosterloh.mirror.models.yahoo_weather.Channel;
import com.rosterloh.mirror.models.yahoo_weather.YahooWeatherResponse;
import com.rosterloh.mirror.util.Constants;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class YahooService {

    private YahooApi mYahooApi;

    public YahooService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.YAHOO_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mYahooApi = retrofit.create(YahooApi.class);
    }

    public Observable<CurrentWeather> getCurrentWeather(YahooWeatherResponse response) {

        Channel weatherData = response.getQuery().getResults().getChannel();

        // Convert degrees to cardinal directions for wind
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
        String degrees = weatherData.getWind().getDirection();
        String direction = degrees.equals("") ? "-" : directions[(int) Math.round((Double.parseDouble(weatherData.getWind().getDirection()) % 360) / 45)];

        return Observable.just(new CurrentWeather.Builder()
                .title(weatherData.getTitle())
                .statusCode(weatherData.getItem().getCondition().getCode())
                .temperature(weatherData.getItem().getCondition().getTemp())
                .humidity(weatherData.getAtmosphere().getHumidity())
                .pressure(weatherData.getAtmosphere().getPressure())
                .visibility(weatherData.getAtmosphere().getVisibility())
                .sunrise(weatherData.getAstronomy().getSunrise())
                .sunset(weatherData.getAstronomy().getSunset())
                .windSpeed(weatherData.getWind().getSpeed())
                .windTemperature(weatherData.getWind().getChill())
                .windDirection(direction)
                .forecast(weatherData.getItem().getForecast())
                .build());
    }

    public YahooApi getApi() {

        return mYahooApi;
    }

    public interface YahooApi {

        @GET("yql")
        Observable<YahooWeatherResponse> getCurrentWeatherConditions(@Query("q") String query, @Query("format") String format);
    }
}
