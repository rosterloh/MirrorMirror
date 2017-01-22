package com.rosterloh.mirror.mirror;

import com.rosterloh.mirror.models.Weather;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MirrorContract {

    interface View {

        void setPresenter(Presenter presenter);

        void setLoadingIndicator(boolean active);

        void showWeather(Weather weather);

        void showLoadingDataError();

        boolean isActive();
    }

    interface Presenter {

        void start();

        void result(int requestCode, int resultCode);
    }
}
