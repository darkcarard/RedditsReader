package com.test.darkcarard.rlist;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class RedditDetailActivityFragment extends Fragment {

    public RedditDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_item_layout, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            final Reddit reddit = (Reddit) intent.getSerializableExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.tv_created)).setText(reddit.getCreationDate());
            ((TextView) rootView.findViewById(R.id.tv_description)).setText(reddit.getDescription());
            DecimalFormat myDecimalFormat = new DecimalFormat("###,###.###");
            String suscribers = myDecimalFormat.format(reddit.getSuscribers());
            ((TextView) rootView.findViewById(R.id.tv_suscribers)).setText(suscribers);
            ((TextView) rootView.findViewById(R.id.tv_language)).setText(reddit.getLanguage());
            ((TextView) rootView.findViewById(R.id.tv_category)).setText(reddit.getCategory());
            ImageView image = (ImageView) rootView.findViewById(R.id.iv_icon);
            String imageURL;
            if (reddit.getImage().isEmpty()) {
                imageURL = reddit.getImageHeader();
            } else {
                imageURL = reddit.getImage();
            }
            Picasso.with(getActivity()).load(imageURL).into(image);

            rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = reddit.getUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

        }
        return rootView;
    }
}
