package com.rosterloh.mirror.views;

import com.rosterloh.mirror.models.Configuration;

public interface SetupView extends BaseView {

    void showLoading();

    void hideLoading();

    void navigateToMainActivity(Configuration configuration);
}
