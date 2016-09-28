package com.rosterloh.mirror.setup;

import com.rosterloh.mirror.models.Configuration;
import com.rosterloh.mirror.BaseView;

public interface SetupView extends BaseView {

    void showLoading();

    void hideLoading();

    void navigateToMainActivity(Configuration configuration);
}
