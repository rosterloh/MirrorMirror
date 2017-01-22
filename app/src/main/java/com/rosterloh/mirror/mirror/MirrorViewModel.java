package com.rosterloh.mirror.mirror;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.rosterloh.mirror.BR;
import com.rosterloh.mirror.R;
import com.rosterloh.mirror.models.Weather;

/**
 * Exposes the data to be used in the {@link MirrorContract.View}.
 * <p>
 * {@link BaseObservable} implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a {@link Bindable} annotation to the property's
 * getter method.
 */

public class MirrorViewModel extends BaseObservable {

    private final MirrorContract.Presenter presenter;

    private Context context;

    @Bindable
    public boolean isListening = false;
    @Bindable
    public boolean isLoading = false;
    @Bindable
    public String currentWeather;
    @Bindable
    public String currentTemperature;
    @Bindable
    public String lastUpdate;
    @Bindable
    public String wind;
    @Bindable
    public String pressure;
    @Bindable
    public String humidity;
    @Bindable
    public String visibility;

    public MirrorViewModel(Context context, MirrorContract.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    public void setLoading(boolean active) {
        isLoading = active;
        notifyPropertyChanged(BR.isLoading);
    }

    public void setWeather(Weather weather) {

        currentWeather = weather.getIconId();
        currentTemperature = weather.getTemperature();
        lastUpdate = context.getString(R.string.last_updated, weather.getLastUpdated());
        wind = weather.getWindInfo();
        pressure = weather.getPressureInfo();
        visibility = weather.getVisibilityInfo();

        notifyPropertyChanged(BR.currentWeather);
        notifyPropertyChanged(BR.currentTemperature);
        notifyPropertyChanged(BR.lastUpdate);
        notifyPropertyChanged(BR.wind);
        notifyPropertyChanged(BR.pressure);
        notifyPropertyChanged(BR.visibility);
    }
}
