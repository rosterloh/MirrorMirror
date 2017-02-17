package com.rosterloh.mirror.mirror;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.rosterloh.mirror.R;
import com.rosterloh.mirror.SnackbarChangedCallback;
import com.rosterloh.mirror.models.Weather;
import com.rosterloh.mirror.models.news.Article;
import com.rosterloh.mirror.networking.MirrorRequestsManager;

/**
 * Exposes the data to be used in the mirror screen.
 * <p>
 * {@link BaseObservable} implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a {@link Bindable} annotation to the property's
 * getter method.
 */
public class MirrorViewModel extends BaseObservable implements SnackbarChangedCallback.SnackBarViewModel {

    // These observable fields will update Views automatically
    public final ObservableList<Article> newsItems = new ObservableArrayList<>();

    public final ObservableBoolean isListening = new ObservableBoolean(false);
    public final ObservableBoolean isLoading = new ObservableBoolean(false);

    public final ObservableField<String> currentWeather = new ObservableField<>();
    public final ObservableField<String> currentTemperature = new ObservableField<>();
    public final ObservableField<String> lastUpdate = new ObservableField<>();
    public final ObservableField<String> wind = new ObservableField<>();
    public final ObservableField<String> pressure = new ObservableField<>();
    public final ObservableField<String> humidity = new ObservableField<>();
    public final ObservableField<String> visibility = new ObservableField<>();

    // This is a special Observable that will trigger a SnackBarChangedCallback when modified.
    final ObservableField<String> snackbarText = new ObservableField<>();

    private final MirrorRequestsManager mirrorRequestsManager;

    private Context context;

    public MirrorViewModel(MirrorRequestsManager mirrorRequestsManager, Context context) {
        this.context = context.getApplicationContext();
        this.mirrorRequestsManager = mirrorRequestsManager;
    }

    public void start() {
        loadData(false, true);
    }

    public String getSnackbarText() {
        return snackbarText.get();
    }

    void handleActivityResult(int requestCode, int resultCode) {

    }

    private void loadData(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            isLoading.set(true);
        }

        mirrorRequestsManager.getMirrorData();


        Article test = new Article();
        test.setTitle("Test Article");
        test.setDescription("This is breaking news. Super important. Read me now.");
        newsItems.add(test);
    }

    public void setWeather(Weather weather) {

        currentWeather.set(weather.getIconId());
        currentTemperature.set(weather.getTemperature());
        lastUpdate.set(context.getString(R.string.last_updated, weather.getLastUpdated()));
        wind.set(weather.getWindInfo());
        pressure.set(weather.getPressureInfo());
        visibility.set(weather.getVisibilityInfo());
    }
}
