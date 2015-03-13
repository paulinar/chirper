package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by paulina on 2/21/15.
 */

// Take Tweet objects and turn them into Views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private static class ViewHolder {
        ImageView ivProfilePic;
        TextView tvName;
        TextView tvUsername;
        TextView tvBody;
        TextView tvCreatedAt;
        TextView tvRetweets;
        TextView tvFavorites;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Get the tweet
        Tweet tweet = getItem(position);
        final ViewHolder viewHolder;

        // 2. Find or inflate the template
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet, parent, false); // parent = container, false = not yet insert into parent
            // 3. Find the subviews to fill with data in the template
            viewHolder.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);
            viewHolder.tvRetweets = (TextView) convertView.findViewById(R.id.tvRetweets);
            viewHolder.tvFavorites = (TextView) convertView.findViewById(R.id.tvFavorites);
            convertView.setTag(viewHolder);
        } else { // recycled view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 4. Populate data into subviews
        viewHolder.ivProfilePic.setImageResource(android.R.color.transparent); // clear out old image for recycled view
        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(viewHolder.ivProfilePic);
        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder.tvUsername.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvCreatedAt.setText(Tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.tvRetweets.setText(String.valueOf(tweet.getRetweetCount()));
        viewHolder.tvFavorites.setText(String.valueOf(tweet.getFavoriteCount()));

        viewHolder.ivProfilePic.setTag(tweet.getUser());
        viewHolder.ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", (User) viewHolder.ivProfilePic.getTag()); // so we can use the same OnClickListener for every profile pic
                getContext().startActivity(i);
            }
        });

        // 5. Return the view to be inserted into the list
        return convertView;
    }
}
