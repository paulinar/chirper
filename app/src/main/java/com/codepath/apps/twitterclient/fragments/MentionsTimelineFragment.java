package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by paulina on 2/26/15.
 */
public class MentionsTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the client
        client = TwitterApplication.getRestClient(); // singleton class; using same client across all activities
        populateTimeline(0, false);
    }

    // Send API request to get timeline json and fill listview by creating tweet objects from json
    private void populateTimeline(int page, final boolean clear) {
        client.getMentionsTimeline(page, new JsonHttpResponseHandler() {

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
