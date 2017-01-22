package com.rosterloh.mirror.mirror;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.rosterloh.mirror.R;
import com.rosterloh.mirror.data.DataRepository;
import com.rosterloh.mirror.data.LocalDataSource;
import com.rosterloh.mirror.data.RemoteDataSource;
import com.rosterloh.mirror.util.ActivityUtils;

public class MirrorActivity extends AppCompatActivity implements View.OnSystemUiVisibilityChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mirror_act);

        MirrorFragment mirrorFragment =
                (MirrorFragment) getSupportFragmentManager().findFragmentById(R.id.fl_content);
        if (mirrorFragment == null) {
            // Create the fragment
            mirrorFragment = MirrorFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mirrorFragment, R.id.fl_content);
        }

        DataRepository data = DataRepository.getInstance(RemoteDataSource.getInstance(),
                LocalDataSource.getInstance(getApplicationContext()));
        // Create the presenter
        MirrorPresenter mirrorPresenter = new MirrorPresenter(data, mirrorFragment);

        MirrorViewModel mirrorViewModel =
                new MirrorViewModel(getApplicationContext(), mirrorPresenter);

        mirrorFragment.setViewModel(mirrorViewModel);

        //never sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        View mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        mDecorView.setOnSystemUiVisibilityChangeListener(this);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            hideSystemUI();
        }
    }
/*
    public void showListening() {
        binding.ivListening.setVisibility(View.VISIBLE);
    }

    public void hideListening() {
        binding.ivListening.setVisibility(View.INVISIBLE);
    }

    public void displayCurrentWeather(Weather weather) {

        binding.ivCurrentWeather.setImageResource(weather.getIconId());
        binding.tvCurrentTemp.setText(weather.getTemperature());
        binding.tvLastUpdated.setText(getString(R.string.last_updated) + " " + weather.getLastUpdated());

        binding.tvStatsWind.setText(weather.getWindInfo());
        binding.tvStatsHumidity.setText(weather.getHumidityInfo());
        binding.tvStatsPressure.setText(weather.getPressureInfo());
        binding.tvStatsVisibility.setText(weather.getVisibilityInfo());

        binding.tvForecastDate1.setText(weather.getForecast().get(0).getDate());
        binding.tvForecastTemp1.setText(weather.getForecast().get(0).getTemperature());
        binding.ivForecastWeather1.setImageResource(weather.getForecast().get(0).getIconId());
        binding.tvForecastDate2.setText(weather.getForecast().get(1).getDate());
        binding.tvForecastTemp2.setText(weather.getForecast().get(1).getTemperature());
        binding.ivForecastWeather2.setImageResource(weather.getForecast().get(1).getIconId());
        binding.tvForecastDate3.setText(weather.getForecast().get(2).getDate());
        binding.tvForecastTemp3.setText(weather.getForecast().get(2).getTemperature());
        binding.ivForecastWeather3.setImageResource(weather.getForecast().get(2).getIconId());
        binding.tvForecastDate4.setText(weather.getForecast().get(3).getDate());
        binding.tvForecastTemp4.setText(weather.getForecast().get(3).getTemperature());
        binding.ivForecastWeather4.setImageResource(weather.getForecast().get(2).getIconId());
    }

    public void displayTopRedditPost(RedditPost redditPost) {
        binding.tvRedditPostTitle.setText(redditPost.getTitle());
        binding.tvRedditPostVotes.setText(redditPost.getUps() + "");
    }

    public void displayCalendarEvents(String events) {
        binding.tvCalendarEvent.setText(events);
    }

    public void handleMqttEvent(String payload) {

        Bundle loggingParams = new Bundle();
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        switch (payload) {
            case "0":
                loggingParams.putString("motion", "screen on");
                layoutParams.screenBrightness = 0;
                window.setAttributes(layoutParams);
                break;
            case "1":
                loggingParams.putString("motion", "screen off");
                layoutParams.screenBrightness = -1;
                window.setAttributes(layoutParams);
                break;
            default:
                loggingParams.putString("payload", payload);
                break;
        }
        firebaseAnalytics.logEvent("mqtt", loggingParams);
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
