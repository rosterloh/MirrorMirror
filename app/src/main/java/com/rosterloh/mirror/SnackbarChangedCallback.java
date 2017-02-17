package com.rosterloh.mirror;

import android.databinding.Observable;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Callback to apply to a {@link android.databinding.ObservableField<String>} that shows a Snackbar
 * whenever the text is updated.
 */
public class SnackbarChangedCallback extends Observable.OnPropertyChangedCallback {

    private final WeakReference<View> view;

    private final SnackBarViewModel viewModel;

    public SnackbarChangedCallback(View descendantOfCoordinatorLayout,
                                   SnackBarViewModel viewModel) {
        view = new WeakReference<>(descendantOfCoordinatorLayout);
        this.viewModel = viewModel;
    }

    @Override
    public void onPropertyChanged(Observable observable, int i) {
        if (view.get() == null) {
            return;
        }
        Snackbar.make(view.get(),
                viewModel.getSnackbarText(),
                Snackbar.LENGTH_SHORT).show();
    }

    public interface SnackBarViewModel {
        String getSnackbarText();
    }
}
