package com.rosterloh.mirror.activity;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.rosterloh.mirror.MirrorApplication;
import com.rosterloh.mirror.R;
import com.rosterloh.mirror.databinding.ActivityMainBinding;
import com.rosterloh.mirror.models.Configuration;
import com.rosterloh.mirror.models.RedditPost;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.presenters.MainPresenter;
import com.rosterloh.mirror.util.ASFObjectStore;
import com.rosterloh.mirror.util.Constants;
import com.rosterloh.mirror.views.MainView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView,
        View.OnSystemUiVisibilityChangeListener {

    private ActivityMainBinding binding;

    @Inject
    MainPresenter presenter;

    @Inject
    ASFObjectStore<Configuration> objectStore;

    MaterialDialog mapDialog;

    //MqttAndroidClient client;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ((MirrorApplication) getApplication()).createMainComponent(this).inject(this);
        Assent.setActivity(this, this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Configuration configuration = objectStore.get();
        boolean didLoadOldConfig = getIntent().getBooleanExtra(Constants.SAVED_CONFIGURATION_IDENTIFIER, false);

        //never sleep
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (didLoadOldConfig)
            showConfigurationSnackbar();

        presenter.setConfiguration(configuration);
        /*
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), configuration.getServerAddress(), clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(MainActivity.class.getSimpleName(), "Connected to MQTT");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(MainActivity.class.getSimpleName(), "Failed to connect to MQTT");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        */
    }

    private void showConfigurationSnackbar() {
        Snackbar snackbar = Snackbar
                .make(binding.weatherLayout, getString(R.string.old_config_found_snackbar), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.old_config_found_snackbar_back), view -> {
                    onBackPressed();
                });

        snackbar.show();
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
    public void showListening() {
        binding.ivListening.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListening() {
        binding.ivListening.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMap(String location) {

        String url = Constants.STATIC_MAPS_URL_FIRST +
                location + Constants.STATIC_MAPS_URL_SECOND +
                location + Constants.STATIC_MAPS_URL_THIRD +
                getString(R.string.static_maps_api_key);

        mapDialog = new MaterialDialog.Builder(this)
                .customView(R.layout.map_image, false)
                .contentGravity(GravityEnum.CENTER)
                .build();

        View imageView = mapDialog.getCustomView();
        Picasso.with(MainActivity.this).load(url).into((ImageView) imageView);
        mapDialog.show();
    }

    @Override
    public void hideMap() {
        if (null != mapDialog && mapDialog.isShowing()) {
            mapDialog.dismiss();
            mapDialog = null;
        }
        hideSystemUI();
    }

    @Override
    @SuppressWarnings("all")
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

        if (binding.weatherLayout.getVisibility() != View.VISIBLE) {
            binding.weatherLayout.setVisibility(View.VISIBLE);
            binding.weatherStatsLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    @SuppressWarnings("all")
    public void displayTopRedditPost(RedditPost redditPost) {
        binding.tvRedditPostTitle.setText(redditPost.getTitle());
        binding.tvRedditPostVotes.setText(redditPost.getUps() + "");
        if (binding.redditLayout.getVisibility() != View.VISIBLE)
            binding.redditLayout.setVisibility(View.VISIBLE);
    }

    @Override
    @SuppressWarnings("all")
    public void displayLatestCalendarEvent(String event) {
        binding.tvCalendarEvent.setText(event);
        if (binding.calendarLayout.getVisibility() != View.VISIBLE)
            binding.calendarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Assent.setActivity(this, this);
        hideSystemUI();
        presenter.start(Assent.isPermissionGranted(Assent.READ_CALENDAR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.finish();
        /*
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(MainActivity.class.getSimpleName(), "Disconnected from MQTT");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // something went wrong, but probably we are disconnected anyway
                    Log.d(MainActivity.class.getSimpleName(), "Error disconnecting from MQTT");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        */
        if (isFinishing())
            Assent.setActivity(this, null);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {

        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            hideSystemUI();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MirrorApplication) getApplication()).releaseMainComponent();
    }

}
