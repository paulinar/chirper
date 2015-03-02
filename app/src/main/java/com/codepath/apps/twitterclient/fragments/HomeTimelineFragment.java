package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.listeners.EndlessScrollListener;
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
public class HomeTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the client
        client = TwitterApplication.getRestClient(); // singleton class; using same client across all activities
        populateTimeline(0, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(this.aTweets);

        // endless scroll
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                populateTimeline(page, false);
            }
        });

        // Pull-to-refresh: setup refresh listener which triggers new data loading
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(0, true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    public static HomeTimelineFragment newInstance() {
        HomeTimelineFragment homeTimelineFragment = new HomeTimelineFragment();
        return homeTimelineFragment;
    }

    // Send API request to get timeline json and fill listview by creating tweet objects from json
    public void populateTimeline(int page, final boolean clear) {
        if (!isNetworkAvailable()) {
            Toast.makeText(getActivity().getApplicationContext(), "No network connection available",
                    Toast.LENGTH_SHORT).show();
            clear();
            addAll(Tweet.fromCache());
        } else {
            client.getHomeTimeline(page, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("DEBUG", response.toString());
                    // DESERIALIZE JSON
                    // CREATE MODELS AN ADD THEM TO THE ADAPTER
                    // LOAD MODEL DATA INTO LISTVIEW
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);

                    if (clear) {
                        deleteDbData();
                        clear();
                        addAll(tweets);
                        swipeContainer.setRefreshing(false);
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

}
