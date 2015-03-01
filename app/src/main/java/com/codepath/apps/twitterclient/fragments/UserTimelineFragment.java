package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by paulina on 2/26/15.
 */
public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the client
        client = TwitterApplication.getRestClient(); // singleton class; using same client across all activities
        populateTimeline(false);
    }

    // Creates a new UserTimelineFragment
    // UserTimelineFragment.newInstance("my_screenname")
    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", user.getScreenName());
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    // Send API request to get timeline json and fill listview by creating tweet objects from json
    private void populateTimeline(final boolean clear) {

        String screenName = getArguments().getString("screen_name");

        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                // DESERIALIZE JSON
                // CREATE MODELS AN ADD THEM TO THE ADAPTER
                // LOAD MODEL DATA INTO LISTVIEW
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);

                if (clear) {
                    clear();
                    addAll(tweets);
//                        swipeContainer.setRefreshing(false); // TODO: REIMPLEMENT!
                } else {
                    addAll(tweets);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Log.d("DEBUG", throwable.toString());
            }
        });

    }
}
