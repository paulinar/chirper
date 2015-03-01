package com.codepath.apps.twitterclient.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.adapters.CurrentUserInfoArrayAdapter;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComposeActivity extends ActionBarActivity {

    private EditText etTweetBody;
    private ImageView ivCancel;
    private Button btnTweet;
    private TextView tvCharCount;

    private TwitterClient client;
    private ArrayList<User> userInfo;
    private CurrentUserInfoArrayAdapter aUserInfo;
    private ListView lvUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        getSupportActionBar().hide();

        etTweetBody = (EditText) findViewById(R.id.etTweetBody);
        ivCancel = (ImageView) findViewById(R.id.ivCancel);
        btnTweet = (Button) findViewById(R.id.btnTweet);
        tvCharCount = (TextView) findViewById(R.id.tvCharCount);
        tvCharCount.setText("0");

        lvUserInfo = (ListView) findViewById(R.id.lvUserInfo); // Find the listview
        userInfo = new ArrayList<>();  // Create the arraylist (data source)
        aUserInfo = new CurrentUserInfoArrayAdapter(this, userInfo); // Construct adapter from data source
        lvUserInfo.setAdapter(aUserInfo); // Connect adapter to listview

        client = TwitterApplication.getRestClient(); // singleton class; using same client across all activities
        populateCurrentUserInfo();

        etTweetBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) { }

            @Override
            public void beforeTextChanged(CharSequence charSeq, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSeq, int start, int before, int count) {
                if (charSeq.length() > 140) {
                    tvCharCount.setTextColor(Color.RED);
                    btnTweet.setEnabled(false);
                } else {
                    tvCharCount.setTextColor(getResources().getColor(R.color.darkgray));
                    btnTweet.setEnabled(true);
                }
                tvCharCount.setText(String.valueOf(charSeq.length()));
            }
        });
    }

    public void submitTweet(View v) {
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "No network connection available",
                    Toast.LENGTH_SHORT).show();
        } else {
            String tweetBody = etTweetBody.getText().toString();
            TwitterApplication.getRestClient().postTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                    Log.d("DEBUG", throwable.toString());
                }
            }, tweetBody);
        }
    }


    public void cancelTweet(View v) {
        finish();
    }

    private void populateCurrentUserInfo() {
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "No network connection available",
                    Toast.LENGTH_SHORT).show();
        } else {
            client.getCurrentUserInfo(new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    User userInfo = User.fromJSON(response);
                    aUserInfo.add(userInfo);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                    Log.d("DEBUG", throwable.toString());
                }
            });
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
