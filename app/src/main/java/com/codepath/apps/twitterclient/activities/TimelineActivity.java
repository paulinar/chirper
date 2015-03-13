package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.models.Tweet;

public class TimelineActivity extends ActionBarActivity {

    private final int REQUEST_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // ActionBar configuration
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_twitter_bird);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);

        // 1. Get the viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);

        // 2. Set the viewpager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));

        // 3. Find the pager sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        // 4. Attach the tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_compose) {
            Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
            startActivityForResult(i, REQUEST_CODE);
        } else if (id == R.id.action_profile) {
            onProfileView(item);
        }
        return super.onOptionsItemSelected(item);
    }

    // return the order of the fragments in the ViewPager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = { "Home", "Mentions" };

        // adapter gets the manager to insert or remove fragments from the activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // controls order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            }
            return null;
        }

        // returns tab title at top
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        // how many fragments to swipe between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    public void onProfileView(MenuItem mi) {
        if (mi.getItemId() == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet newTweet = (Tweet) i.getSerializableExtra("newTweet");

            HomeTimelineFragment fragmentTweetList = (HomeTimelineFragment) getSupportFragmentManager()
                    .findFragmentByTag(getFragmentName(R.id.viewpager, 0));
            fragmentTweetList.appendTweet(newTweet);
            fragmentTweetList.populateTimeline(0, false);
        }
    }

    private static String getFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
}
