package com.rosterloh.mirror.presenters;

import com.rosterloh.mirror.util.Constants;
import com.rosterloh.mirror.views.ISetupView;

import java.lang.ref.WeakReference;

public class SetupPresenterImpl implements ISetupPresenter {

    private WeakReference<ISetupView> mSetupView;

    public SetupPresenterImpl(ISetupView view) {

        mSetupView = new WeakReference<>(view);
    }

    @Override
    public void launch(String location, String subreddit, String pollingDelay, String server, boolean celsius, boolean voiceCommands) {

        if (pollingDelay.equals("") || pollingDelay.equals("0")) pollingDelay = Constants.POLLING_DELAY_DEFAULT;

        if (mSetupView.get() != null) {
            mSetupView.get().navigateToMainActivity(
                    location.length() == 0 ? Constants.LOCATION_DEFAULT : location,
                    subreddit.length() == 0 ? Constants.SUBREDDIT_DEFAULT : subreddit,
                    Integer.parseInt(pollingDelay),
                    server.length() == 0 ? Constants.SERVER_DEFAULT : server,
                    celsius,
                    voiceCommands);
        }
    }
}
