package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by paulina on 2/21/15.
 */

// Take Tweet objects and turn them into Views displayed in the list
public class CurrentUserInfoArrayAdapter extends ArrayAdapter<User> {

    private static class ViewHolder {
        ImageView ivProfilePic;
        TextView tvUsername;
        TextView tvName;
    }

    public CurrentUserInfoArrayAdapter(Context context, List<User> users) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Get the tweet
        User user = getItem(position);
        ViewHolder viewHolder;

        // 2. Find or inflate the template
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.current_user_info, parent, false); // parent = container, false = not yet insert into parent
            // 3. Find the subviews to fill with data in the template
            viewHolder.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(viewHolder);
        } else { // recycled view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 4. Populate data into subviews
        viewHolder.ivProfilePic.setImageResource(android.R.color.transparent);
        Picasso.with(getContext())
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(viewHolder.ivProfilePic);
        viewHolder.tvName.setText(user.getName());
        viewHolder.tvUsername.setText("@" + user.getScreenName());

        // 5. Return the view to be inserted into the list
        return convertView;
    }
}
