package com.rosterloh.mirror.mirror;

import android.content.Context;

import com.rosterloh.mirror.SnackbarChangedCallback;
import com.rosterloh.mirror.networking.MirrorRequestsManager;

/**
 * Listens to user actions from the list item in ({@link MirrorFragment}) and redirects them to the
 * Fragment's actions listener.
 */
public class NewsItemViewModel extends NewsViewModel implements SnackbarChangedCallback.SnackBarViewModel {

    private final NewsItemNavigator newsItemNavigator;

    public NewsItemViewModel(Context context, MirrorRequestsManager mirrorRequestsManager,
                             NewsItemNavigator itemNavigator) {
        super(context, mirrorRequestsManager);
        newsItemNavigator = itemNavigator;
    }

    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void itemClicked() {
        String itemId = getItemId();
        if (itemId == null) {
            // Click happened before task was loaded, no-op.
            return;
        }
        newsItemNavigator.openItemDetails(itemId);
    }
}
