package com.rosterloh.mirror.mirror;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import com.rosterloh.mirror.R;
import com.rosterloh.mirror.SnackbarChangedCallback;
import com.rosterloh.mirror.models.news.Article;
import com.rosterloh.mirror.networking.MirrorRequestsManager;

/**
 * Abstract class for View Models that expose a single {@link Article}.
 */
public abstract class NewsViewModel extends BaseObservable
        implements SnackbarChangedCallback.SnackBarViewModel {

    public final ObservableField<String> snackbarText = new ObservableField<>();

    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();

    private final ObservableField<Article> newsObservable = new ObservableField<>();

    private final MirrorRequestsManager mirrorRequestsManager;

    private final Context context;

    private boolean isDataLoading;

    public NewsViewModel(Context context, MirrorRequestsManager mirrorRequestsManager) {
        this.context = context.getApplicationContext(); // Force use of Application Context.
        this.mirrorRequestsManager = mirrorRequestsManager;

        // Exposed observables depend on the mTaskObservable observable:
        newsObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Article item = newsObservable.get();
                if (item != null) {
                    title.set(item.getTitle());
                    description.set(item.getDescription());
                } else {
                    title.set(context.getString(R.string.no_data));
                    description.set(context.getString(R.string.no_data_description));
                }
            }
        });
    }

    public void start(String itemId) {
        if (itemId != null) {
            isDataLoading = true;
            //mirrorRequestsManager.getNews(taskId, this);
        }
    }

    public void setItem(Article item) {
        newsObservable.set(item);
    }

    @Bindable
    public boolean isDataAvailable() {
        return newsObservable.get() != null;
    }

    @Bindable
    public boolean isDataLoading() {
        return isDataLoading;
    }

    // This could be an observable, but we save a call to Article.getTitleForList() if not needed.
    @Bindable
    public String getTitleForList() {
        if (newsObservable.get() == null) {
            return "No data";
        }
        return newsObservable.get().getTitleForList();
    }

    public String getSnackbarText() {
        return snackbarText.get();
    }

    @Nullable
    protected String getItemId() {
        return newsObservable.get().getPublishedAt();
    }

}
