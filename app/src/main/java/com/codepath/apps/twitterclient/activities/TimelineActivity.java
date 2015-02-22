package com.codepath.apps.twitterclient.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.listeners.EndlessScrollListener;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    private SwipeRefreshLayout swipeContainer;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;

    private static final int COUNT = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView) findViewById(R.id.lvTweets); // Find the listview
        tweets = new ArrayList<>();  // Create the arraylist (data source)
        aTweets = new TweetsArrayAdapter(this, tweets); // Construct adapter from data source
        lvTweets.setAdapter(aTweets); // Connect adapter to listview

        client = TwitterApplication.getRestClient(); // singleton class; using same client across all activities
        populateTimeline(COUNT, 0, false);

        // Endless scroll: attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                populateTimeline(COUNT, page, false);
            }
        });

        // Pull-to-refresh: setup refresh listener which triggers new data loading
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(COUNT, 0, true);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    // Send API request to get timeline json and fill listview by creating tweet objects from json
    private void populateTimeline(int count, int page, final boolean clear) {
        client.getHomeTimeline(count, page, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                // DESERIALIZE JSON
                // CREATE MODELS AN ADD THEM TO THE ADAPTER
                // LOAD MODEL DATA INTO LISTVIEW
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);

                if (clear) {
                    aTweets.clear();
                    aTweets.addAll(tweets);
                    swipeContainer.setRefreshing(false);
                } else {
                    aTweets.addAll(tweets);
                }

//                if (clear) {
//                    // Remember to CLEAR OUT old items before appending in the new ones
//                    adapter.clear();
//                    // ...the data has come back, add new items to your adapter...
//                    adapter.addAll(...);
//                    // Now we call setRefreshing(false) to signal refresh has finished
//                    swipeContainer.setRefreshing(false);
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
                Log.d("DEBUG", throwable.toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
