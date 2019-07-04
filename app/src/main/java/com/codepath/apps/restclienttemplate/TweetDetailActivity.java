package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.TimelineActivity.RESULT_KEY;

public class TweetDetailActivity extends AppCompatActivity {
    TextView tvUser;
    TextView tvBody;
    ImageView ivProfile;
    FloatingActionButton fabFavorite;
    FloatingActionButton fabRetweet;
    Boolean clicked;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        tvBody = findViewById(R.id.tvBody);
        tvUser = findViewById(R.id.tvUser);
        ivProfile = findViewById(R.id.ivProfileDetail);
        fabFavorite = findViewById(R.id.fabFavorite);
        fabRetweet = findViewById(R.id.fabRetweet);
        clicked = false;
        client = TwitterApp.getRestClient(this);
        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("TweetDetails"));
        tvBody.setText(tweet.body);
        tvUser.setText(tweet.user.name);
        Glide.with (this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfile);

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clicked) {
                    fabFavorite.setColorFilter(getResources().getColor(R.color.medium_red));
                    clicked = true;
                } else {
                    fabFavorite.setColorFilter(Color.DKGRAY);
                    clicked = false;
                }
            }
        });

        fabRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabRetweet.setColorFilter(getResources().getColor(R.color.medium_green));
                sendRetweet();
            }
        });
    }

    public void sendRetweet() {
        client.sendTweet(tvBody.getText().toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200) {
                    try {
                        //parsing response
                        JSONObject response = new JSONObject(new String(responseBody));
                        Tweet result = Tweet.fromJSON(response);
                        //return result to calling activity
                        Intent resultData = new Intent();
                        resultData.putExtra(RESULT_KEY, Parcels.wrap(result)); //wrap tweet in parcel
                        setResult(RESULT_OK, resultData);
                        finish(); //closes original activity
                    } catch (JSONException e) {
                        Log.e("RetweetActivity", "Error parsing response", e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
