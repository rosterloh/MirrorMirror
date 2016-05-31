package com.rosterloh.mirror.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.rosterloh.mirror.MirrorApplication;
import com.rosterloh.mirror.R;
import com.rosterloh.mirror.models.Configuration;
import com.rosterloh.mirror.models.RedditPost;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.presenters.MainPresenter;
import com.rosterloh.mirror.util.ASFObjectStore;
import com.rosterloh.mirror.util.Constants;
import com.rosterloh.mirror.views.MainView;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView,
        View.OnSystemUiVisibilityChangeListener {

    @BindView(R.id.iv_current_weather) ImageView ivWeatherCondition;;
    @BindView(R.id.tv_current_temp) TextView tvWeatherTemperature;
    @BindView(R.id.weather_layout) LinearLayout llWeatherLayout;
    @BindView(R.id.tv_last_updated) TextView tvWeatherLastUpdated;
    @BindView(R.id.iv_listening) ImageView ivListening;

    @Nullable @BindView(R.id.tv_summary) TextView tvWeatherSummary;
    @Nullable @BindView(R.id.weather_stats_layout) LinearLayout llWeatherStatsLayout;
    @Nullable @BindView(R.id.calendar_layout) LinearLayout llCalendarLayout;
    @Nullable @BindView(R.id.reddit_layout) RelativeLayout rlRedditLayout;
    @Nullable @BindView(R.id.iv_forecast_weather1) ImageView ivDayOneIcon;
    @Nullable @BindView(R.id.tv_forecast_temp1) TextView tvDayOneTemperature;
    @Nullable @BindView(R.id.tv_forecast_date1) TextView tvDayOneDate;
    @Nullable @BindView(R.id.iv_forecast_weather2) ImageView ivDayTwoIcon;
    @Nullable @BindView(R.id.tv_forecast_temp2) TextView tvDayTwoTemperature;
    @Nullable @BindView(R.id.tv_forecast_date2) TextView tvDayTwoDate;
    @Nullable @BindView(R.id.iv_forecast_weather3) ImageView ivDayThreeIcon;
    @Nullable @BindView(R.id.tv_forecast_temp3) TextView tvDayThreeTemperature;
    @Nullable @BindView(R.id.tv_forecast_date3) TextView tvDayThreeDate;
    @Nullable @BindView(R.id.iv_forecast_weather4) ImageView ivDayFourIcon;
    @Nullable @BindView(R.id.tv_forecast_temp4) TextView tvDayFourTemperature;
    @Nullable @BindView(R.id.tv_forecast_date4) TextView tvDayFourDate;
    @Nullable @BindView(R.id.tv_stats_wind) TextView tvWeatherWind;
    @Nullable @BindView(R.id.tv_stats_humidity) TextView tvWeatherHumidity;
    @Nullable @BindView(R.id.tv_stats_pressure) TextView tvWeatherPressure;
    @Nullable @BindView(R.id.tv_stats_visibility) TextView tvWeatherVisibility;
    @Nullable @BindView(R.id.tv_calendar_event) TextView tvCalendarEvent;
    @Nullable @BindView(R.id.tv_reddit_post_title) TextView tvRedditPostTitle;
    @Nullable @BindView(R.id.tv_reddit_post_votes) TextView tvRedditPostVotes;

    @BindString(R.string.old_config_found_snackbar) String oldConfigFound;
    @BindString(R.string.old_config_found_snackbar_back) String getOldConfigFoundBack;
    @BindString(R.string.give_command) String giveCommand;
    @BindString(R.string.listening) String listening;
    @BindString(R.string.command_understood) String commandUnderstood;
    @BindString(R.string.executing) String executing;
    @BindString(R.string.last_updated) String lastUpdated;
    @BindString(R.string.static_maps_api_key) String mapsApiKey;

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
        setContentView(R.layout.activity_main);
        ((MirrorApplication) getApplication()).createMainComponent(this).inject(this);
        Assent.setActivity(this, this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Configuration configuration = objectStore.get();
        boolean didLoadOldConfig = getIntent().getBooleanExtra(Constants.SAVED_CONFIGURATION_IDENTIFIER, false);

        ViewStub viewStub = configuration.isSimpleLayout() ?
                (ViewStub) findViewById(R.id.stub_simple) :
                (ViewStub) findViewById(R.id.stub_verbose);
        if (null != viewStub) viewStub.inflate();

        ButterKnife.bind(this);

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
                .make(llWeatherLayout, getString(R.string.old_config_found_snackbar), Snackbar.LENGTH_LONG)
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
        ivListening.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListening() {
        ivListening.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMap(String location) {

        String url = Constants.STATIC_MAPS_URL_FIRST +
                location + Constants.STATIC_MAPS_URL_SECOND +
                location + Constants.STATIC_MAPS_URL_THIRD +
                mapsApiKey;

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
    public void displayCurrentWeather(Weather weather, boolean isSimpleLayout) {

        this.ivWeatherCondition.setImageResource(weather.getIconId());
        this.tvWeatherTemperature.setText(weather.getTemperature());
        this.tvWeatherLastUpdated.setText(getString(R.string.last_updated) + " " + weather.getLastUpdated());

        if (!isSimpleLayout) {
            this.tvWeatherWind.setText(weather.getWindInfo());
            this.tvWeatherHumidity.setText(weather.getHumidityInfo());
            this.tvWeatherPressure.setText(weather.getPressureInfo());
            this.tvWeatherVisibility.setText(weather.getVisibilityInfo());

            this.tvDayOneDate.setText(weather.getForecast().get(0).getDate());
            this.tvDayOneTemperature.setText(weather.getForecast().get(0).getTemperature());
            this.ivDayOneIcon.setImageResource(weather.getForecast().get(0).getIconId());
            this.tvDayTwoDate.setText(weather.getForecast().get(1).getDate());
            this.tvDayTwoTemperature.setText(weather.getForecast().get(1).getTemperature());
            this.ivDayTwoIcon.setImageResource(weather.getForecast().get(1).getIconId());
            this.tvDayThreeDate.setText(weather.getForecast().get(2).getDate());
            this.tvDayThreeTemperature.setText(weather.getForecast().get(2).getTemperature());
            this.ivDayThreeIcon.setImageResource(weather.getForecast().get(2).getIconId());
            this.tvDayFourDate.setText(weather.getForecast().get(3).getDate());
            this.tvDayFourTemperature.setText(weather.getForecast().get(3).getTemperature());
            this.ivDayFourIcon.setImageResource(weather.getForecast().get(2).getIconId());
        } else {
            this.tvWeatherSummary.setText(weather.getSummary());
        }

        if (this.llWeatherLayout.getVisibility() != View.VISIBLE) {
            this.llWeatherLayout.setVisibility(View.VISIBLE);
            this.llWeatherStatsLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    @SuppressWarnings("all")
    public void displayTopRedditPost(RedditPost redditPost) {
        tvRedditPostTitle.setText(redditPost.getTitle());
        tvRedditPostVotes.setText(redditPost.getUps() + "");
        if (this.rlRedditLayout.getVisibility() != View.VISIBLE)
            this.rlRedditLayout.setVisibility(View.VISIBLE);
    }

    @Override
    @SuppressWarnings("all")
    public void displayLatestCalendarEvent(String event) {
        this.tvCalendarEvent.setText(event);
        if (this.llCalendarLayout.getVisibility() != View.VISIBLE)
            this.llCalendarLayout.setVisibility(View.VISIBLE);
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
