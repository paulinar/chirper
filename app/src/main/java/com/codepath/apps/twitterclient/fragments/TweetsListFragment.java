package com.codepath.apps.twitterclient.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paulina on 2/26/15.
 */
public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    protected TweetsArrayAdapter aTweets;
    protected ListView lvTweets;
    protected SwipeRefreshLayout swipeContainer;

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);  // doesn't add itself to view immediately; do that later

        // goes into onCreateView because it REQUIRES the view
        // now looking up the listview within the FRAGMENT rather than the ACTIVITY
        lvTweets = (ListView) v.findViewById(R.id.lvTweets); // Find the listview

        lvTweets.setAdapter(aTweets); // Connect adapter to listview

        // Pull-to-refresh: setup refresh listener which triggers new data loading
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        return v;
    }

    // creation lifecycle event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // these don't require the view
        tweets = new ArrayList<>();  // Create the arraylist (data source)
        aTweets = new TweetsArrayAdapter(getActivity(), tweets); // Construct adapter from data source

    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void appendTweet(Tweet newTweet) {
        aTweets.insert(newTweet, 0);
    }

    public void clear() {
        aTweets.clear();
    }

    protected void deleteDbData() {
        Tweet.deleteAllTweets();
        User.deleteAllUsers();
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
