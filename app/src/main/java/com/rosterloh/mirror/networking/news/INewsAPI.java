package com.rosterloh.mirror.networking.news;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface INewsAPI {

    @GET("articles")
    Observable<NewsResponse> getNews(@Query("source") String source,
                                     @Query("sortBy") String sort,
                                     @Query("apiKey") String key);
}
