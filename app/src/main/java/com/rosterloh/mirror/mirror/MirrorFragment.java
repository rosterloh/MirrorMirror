package com.rosterloh.mirror.mirror;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.rosterloh.mirror.SnackbarChangedCallback;
import com.rosterloh.mirror.databinding.MirrorFragBinding;
import com.rosterloh.mirror.databinding.NewsItemBinding;
import com.rosterloh.mirror.models.news.Article;
import com.rosterloh.mirror.networking.MirrorRequestsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Display current data.
 */
public class MirrorFragment extends Fragment {

    private MirrorViewModel mirrorViewModel;

    private SnackbarChangedCallback snackBarChangedCallback;

    private MirrorFragBinding mirrorFragBinding;

    public MirrorFragment() {
        // Requires empty public constructor
    }

    public static MirrorFragment newInstance() {
        return new MirrorFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mirrorViewModel.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mirrorFragBinding = MirrorFragBinding.inflate(inflater, container, false);
        mirrorFragBinding.setView(this);
        mirrorFragBinding.setViewmodel(mirrorViewModel);
        mirrorFragBinding.executePendingBindings();
        return mirrorFragBinding.getRoot();
    }

    void setupTextViews() {

        Typeface weatherFontIcon = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/weathericons-regular-webfont.ttf");
        Typeface robotoBlack = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Black.ttf");

        mirrorFragBinding.tvCurrentWeather.setTypeface(weatherFontIcon);
        mirrorFragBinding.tvForecastWeather1.setTypeface(weatherFontIcon);
        mirrorFragBinding.tvForecastWeather2.setTypeface(weatherFontIcon);
        mirrorFragBinding.tvForecastWeather3.setTypeface(weatherFontIcon);
        mirrorFragBinding.tvForecastWeather4.setTypeface(weatherFontIcon);
        mirrorFragBinding.tcTime.setTypeface(robotoBlack);
        mirrorFragBinding.tcDate.setTypeface(robotoBlack);
    }

    public void setViewModel(MirrorViewModel viewModel) {
        mirrorViewModel = viewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSnackbar();

        setupTextViews();

        setupListAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // If the ViewModel
        mirrorViewModel.snackbarText.removeOnPropertyChangedCallback(snackBarChangedCallback);
    }

    private void setupSnackbar() {
        snackBarChangedCallback = new SnackbarChangedCallback(getView(), mirrorViewModel);
        mirrorViewModel.snackbarText.addOnPropertyChangedCallback(snackBarChangedCallback);
    }

    private void setupListAdapter() {
        ListView listView =  mirrorFragBinding.lvNews;

        NewsAdapter listAdapter = new NewsAdapter(
                new ArrayList<Article>(0),
                (NewsItemNavigator) getActivity(),
                MirrorRequestsManager.getInstance(getContext().getApplicationContext()),
                mirrorViewModel);
        listView.setAdapter(listAdapter);
    }

    public static class NewsAdapter extends BaseAdapter {

        private final NewsItemNavigator newsItemNavigator;

        private final MirrorViewModel mirrorViewModel;

        private List<Article> news;

        private final MirrorRequestsManager mirrorRequestsManager;

        public NewsAdapter(List<Article> news, NewsItemNavigator newsItemNavigator,
                           MirrorRequestsManager mirrorRequestsManager,
                            MirrorViewModel mirrorViewModel) {

            this.newsItemNavigator = newsItemNavigator;
            this.mirrorRequestsManager = mirrorRequestsManager;
            this.mirrorViewModel= mirrorViewModel;
            setList(news);
        }

        public void replaceData(List<Article> news) {
            setList(news);
        }

        @Override
        public int getCount() {
            return news != null ? news.size() : 0;
        }

        @Override
        public Article getItem(int i) {
            return news.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Article item = getItem(i);
            NewsItemBinding binding;
            if (view == null) {
                // Inflate
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                // Create the binding
                binding = NewsItemBinding.inflate(inflater, viewGroup, false);
            } else {
                // Recycling view
                binding = DataBindingUtil.getBinding(view);
            }

            final NewsItemViewModel viewmodel = new NewsItemViewModel(
                    viewGroup.getContext().getApplicationContext(),
                    mirrorRequestsManager,
                    newsItemNavigator);

            binding.setViewmodel(viewmodel);
            // To save on PropertyChangedCallbacks, wire the item's snackbar text observable to the
            // fragment's.
            viewmodel.snackbarText.addOnPropertyChangedCallback(
                    new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable observable, int i) {
                            mirrorViewModel.snackbarText.set(viewmodel.getSnackbarText());
                        }
                    });
            viewmodel.setItem(item);

            return binding.getRoot();
        }

        private void setList(List<Article> news) {
            this.news = news;
            notifyDataSetChanged();
        }
    }
}
