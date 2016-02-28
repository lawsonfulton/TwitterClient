package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.ComposeDialog;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.fragments.HomeTimeLineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimeLineFragment;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends AppCompatActivity implements ComposeDialog.OnCompleteListener {
    HomeTimeLineFragment homeTimelineFragment;
    private TwitterClient client;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeDialog();
            }
        });

        client = TwitterApp.getRestClient();
        user = null;

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void showComposeDialog() {
        if (user != null) {
            startComposeDialogForUser();
        } else {
            client.getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    startComposeDialogForUser();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }
    }

    private void startComposeDialogForUser() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);

        FragmentManager fm = getSupportFragmentManager();
        ComposeDialog composeDialog = new ComposeDialog();
        composeDialog.setArguments(bundle);
        composeDialog.show(fm, "compose_fragment");
    }

    @Override
    public void onDialogComplete(String tweetBody) {
        client.postTweet(tweetBody, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                homeTimelineFragment.refresh();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    public void onProfileView(MenuItem mi) {
        if (user != null) {
            viewProfile();
        } else {
            client.getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    viewProfile();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            });
        }
    }

    public void viewProfile() {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("screenName", user.getScreenName());
        startActivity(i);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                homeTimelineFragment = new HomeTimeLineFragment();
                return homeTimelineFragment;
            } else if (position == 1) {
                return new MentionsTimeLineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
