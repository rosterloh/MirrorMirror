package com.rosterloh.mirror.services;

import com.rosterloh.mirror.models.RedditPost;
import com.rosterloh.mirror.models.reddit.Data_;
import com.rosterloh.mirror.models.reddit.RedditResponse;
import com.rosterloh.mirror.util.Constants;

import java.util.Random;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public class RedditService {

    private RedditApi mRedditApi;

    public RedditService() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REDDIT_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRedditApi = retrofit.create(RedditApi.class);
    }

    public Observable<RedditPost> getRedditPost(RedditResponse response) {

        //Get random post from list of top reddit posts
        Random r = new Random();
        int randomNumber = r.nextInt(Constants.REDDIT_LIMIT);
        Data_ postData = response.getData().getChildren().get(randomNumber).getData();

        return Observable.just(new RedditPost(postData.getTitle(), postData.getAuthor(),
                postData.getUps()));
    }

    public RedditApi getApi() {

        return mRedditApi;
    }

    public interface RedditApi {

        @GET("{subreddit}/top.json")
        Observable<RedditResponse> getTopRedditPostForSubreddit(@Path("subreddit") String subreddit, @Query("limit") int limit);
    }
}
