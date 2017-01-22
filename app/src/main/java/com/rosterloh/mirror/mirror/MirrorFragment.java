package com.rosterloh.mirror.mirror;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rosterloh.mirror.R;
import com.rosterloh.mirror.databinding.MirrorFragBinding;
import com.rosterloh.mirror.models.Weather;

/**
 * Display current data.
 */
public class MirrorFragment extends Fragment implements MirrorContract.View {

    private MirrorContract.Presenter presenter;

    private MirrorViewModel viewModel;

    public MirrorFragment() {
        // Requires empty public constructor
    }

    public static MirrorFragment newInstance() {
        return new MirrorFragment();
    }

    @Override
    public void setPresenter(@NonNull MirrorContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MirrorFragBinding binding = MirrorFragBinding.inflate(inflater, container, false);

        binding.setMirror(viewModel);

        setupTextViews(binding);

        return binding.getRoot();
    }

    void setupTextViews(MirrorFragBinding binding) {

        Typeface weatherFontIcon = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/weathericons-regular-webfont.ttf");
        Typeface robotoBlack = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Black.ttf");

        binding.tvCurrentWeather.setTypeface(weatherFontIcon);
        binding.tcTime.setTypeface(robotoBlack);
        binding.tcDate.setTypeface(robotoBlack);
    }

    public void setViewModel(MirrorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        viewModel.setLoading(active);
    }

    @Override
    public void showWeather(Weather weather) {
        viewModel.setWeather(weather);
    }

    @Override
    public void showLoadingDataError() {
        showMessage(getString(R.string.loading_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

}
