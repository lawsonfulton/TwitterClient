package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.EndlessScrollListener;
import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lawson on 2/27/16.
 */
public class HomeTimeLineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        client = TwitterApp.getRestClient();
        populateTimeline();

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeline();
                return true;
            }
        });

        return v;
    }



    public void populateTimeline() {
        long olderThanId = 0;

        if(!aTweets.isEmpty()) {
            Tweet lastTweet = aTweets.getItem(aTweets.getCount() - 1);
            olderThanId = lastTweet.getUid();
        }

        client.getHomeTimeline(olderThanId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());

                ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);
                aTweets.addAll(newTweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
