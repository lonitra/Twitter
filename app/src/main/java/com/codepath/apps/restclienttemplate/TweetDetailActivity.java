package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {
    TextView tvUser;
    TextView tvBody;
    ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        tvBody = findViewById(R.id.tvBody);
        tvUser = findViewById(R.id.tvUser);
        ivProfile = findViewById(R.id.ivProfileDetail);
        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("TweetDetails"));
        tvBody.setText(tweet.body);
        tvUser.setText(tweet.user.name);
        Glide.with (this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfile);
    }
}
