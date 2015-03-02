package com.codepath.apps.twitterclient.models;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by paulina on 2/21/15.
 */
@Table(name = "User")
public class User extends Model implements Serializable {

    private static final long serialVersionUID = 2075340111410854058L;

    @Column(name = "name")
    private String name;

    @Column(name = "uid", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private Long uid;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "tagline")
    private String tagline;

    @Column(name = "followers_count")
    private int followersCount;

    @Column(name = "friends_count")
    private int friendsCount;

    // Make sure to have a default constructor for every ActiveAndroid model
    public User(){
        super();
    }

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public String getName() {
        return name;
    }

    public Long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            Long currentUid = jsonObject.getLong("id");
            User existingUser = new Select().from(User.class)
                                            .where("uid = ?", currentUid)
                                            .executeSingle();
            if (existingUser != null) {
                user = existingUser;
            }
            user.name = jsonObject.getString("name");
            user.uid = currentUid;
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.tagline = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.friendsCount = jsonObject.getInt("friends_count");
            user.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void deleteAllUsers() {
        new Delete().from(User.class).execute();
    }
}
