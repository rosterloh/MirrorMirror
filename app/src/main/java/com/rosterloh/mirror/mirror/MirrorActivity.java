package com.rosterloh.mirror.mirror;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.rosterloh.mirror.R;
import com.rosterloh.mirror.ViewModelHolder;
import com.rosterloh.mirror.networking.MirrorRequestsManager;

public class MirrorActivity extends AppCompatActivity implements
        NewsItemNavigator, View.OnSystemUiVisibilityChangeListener {

    private MirrorViewModel viewModel;

    public static final String MIRROR_VIEWMODEL_TAG = "MIRROR_VIEWMODEL_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mirror_act);

        MirrorFragment mirrorFragment = findOrCreateViewFragment();

        viewModel = findOrCreateViewModel();

        // Link View and ViewModel
        mirrorFragment.setViewModel(viewModel);

        //never sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private MirrorViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<MirrorViewModel> retainedViewModel =
                (ViewModelHolder<MirrorViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(MIRROR_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            MirrorViewModel viewModel = new MirrorViewModel(
                    MirrorRequestsManager.getInstance(getApplicationContext()),
                    getApplicationContext());

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    MIRROR_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @NonNull
    private MirrorFragment findOrCreateViewFragment() {
        MirrorFragment mirrorFragment =
                (MirrorFragment) getSupportFragmentManager().findFragmentById(R.id.fl_content);
        if (mirrorFragment == null) {
            // Create the fragment
            mirrorFragment = MirrorFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mirrorFragment, R.id.fl_content);
        }
        return mirrorFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewModel.handleActivityResult(requestCode, resultCode);
    }

    @Override
    public void openItemDetails(String itemId) {
        // Expand to show full content
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
