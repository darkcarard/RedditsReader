package com.test.darkcarard.rlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by darkcarard on 19/02/17.
 */

public class RedditItemAdapter extends ArrayAdapter<Reddit> {

    public RedditItemAdapter(Context context, int resource, ArrayList<Reddit> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reddit reddit = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_reddit, parent, false);
        }
        if (reddit != null) {
            TextView tvRedditName = (TextView) convertView.findViewById(R.id.tv_reddit_name);
            TextView tvRedditSubscribers = (TextView) convertView.findViewById(R.id.tv_reddit_subscribers);
            tvRedditName.setText(reddit.getName());
            DecimalFormat myDecimalFormat = new DecimalFormat("###,###.###");
            String suscribersStr = myDecimalFormat.format(reddit.getSuscribers());
            tvRedditSubscribers.setText(suscribersStr);
        }
        return convertView;
    }
}
