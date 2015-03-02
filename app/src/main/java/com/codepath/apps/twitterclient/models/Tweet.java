package com.codepath.apps.twitterclient.models;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by paulina on 2/21/15.
 */

// Parse the JSON + store data + encapsulate state logic or display logic
@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {

    private static final long serialVersionUID = -2454839958978109603L;

    @Column(name = "body")
    private String body;

    @Column(name = "uid", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid; // unique uid for tweet

    // This is an association to another activeandroid model
    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "retweet_count")
    private int retweetCount;

    @Column(name = "favorite_count")
    private int favoriteCount;

    // Make sure to have a default constructor for every ActiveAndroid model
    public Tweet(){
        super();
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Deserialize the JSON and build Tweet objects from single JSON object
    // Tweet.fromJSON("{...}") => <Tweet>
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        // Extract values from the json and store them
        try {
            Long currentUid = jsonObject.getLong("id");
            Tweet existingTweet = new Select().from(Tweet.class)
                                              .where("uid = ?", currentUid)
                                              .executeSingle();
            if (existingTweet != null) {
                tweet = existingTweet;
            }
            tweet.body = jsonObject.getString("text");
            tweet.uid = currentUid;
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");

            tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    // Tweet.fromJSONArray( [ { ... }, { ... } ] ) => List<Tweet>
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        // iterate the json array and create tweets
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = fromJSON(tweetJson); // turn each tweet into an object
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    //  "created_at": "Wed Mar 07 22:23:19 +0000 2007"
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeTime = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeTime = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            String[] pieces = relativeTime.split(" ");
            if (pieces.length < 2) {
                Log.d("DEBUG", "No relative time to return.");
            } else {
                String number = pieces[0];
                char letter = pieces[1].charAt(0);
                relativeTime = number + Character.toString(letter);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeTime;
    }

    public static ArrayList<Tweet> fromCache() {
        ArrayList<Tweet> alTweets = new ArrayList<>();
        List<Tweet> lTweets = new Select()
                .from(Tweet.class)
                .execute();
        alTweets.addAll(lTweets);
        return alTweets;
    }

    public static void deleteAllTweets() {
        new Delete().from(Tweet.class).execute();
    }
}
