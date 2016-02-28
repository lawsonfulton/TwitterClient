package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lawson on 2/27/16.
 */
abstract public class TweetsListFragment extends Fragment {

    protected ArrayList<Tweet> tweets;
    protected TweetsArrayAdapter aTweets;
    protected ListView lvTweets;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        lvTweets.setAdapter(aTweets);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    abstract public void populateTimeline();

    public void refresh() {
        aTweets.clear();
        populateTimeline();
    }
    public ListView getLvTweets() {
        return lvTweets;
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public TweetsArrayAdapter getAdapter() {
        return aTweets;
    }

    public void clear() {
        aTweets.clear();
    }
}
