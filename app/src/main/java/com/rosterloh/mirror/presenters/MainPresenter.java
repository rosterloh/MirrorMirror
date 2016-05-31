package com.rosterloh.mirror.presenters;

import com.rosterloh.mirror.models.Configuration;

public interface MainPresenter {

    void finish();

    void setConfiguration(Configuration configuration);

    void start(boolean hasAccessToCalendar);

    void showError(String error);
}
