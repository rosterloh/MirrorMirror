package com.rosterloh.mirror.networking.news;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsAPIService {

    private INewsAPI newsAPI;
    private boolean isRequestingNews;

    public NewsAPIService(OkHttpClient client) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v1/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.newsAPI = retrofit.create(INewsAPI.class);
    }

    public boolean isRequestingNews() {
        return isRequestingNews;
    }

    public Flowable<NewsResponse> getNews(NewsRequest request) {

        return newsAPI.getNews(request.getSource(), request.getSort(), request.getKey())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        isRequestingNews = true;
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        isRequestingNews = false;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // do something useful
                    }
                })
                .toFlowable(BackpressureStrategy.BUFFER);
    }
}
