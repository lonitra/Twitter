package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class ReplyActivity extends AppCompatActivity {
    TextView tvBodyReply;
    EditText etReply;
    ImageView ivProfileReply;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        tvBodyReply = findViewById(R.id.tvBodyReply);
        etReply = findViewById(R.id.etReply);
        ivProfileReply = findViewById(R.id.ivProfileReply);
        Tweet replyTweet = Parcels.unwrap(getIntent().getParcelableExtra("Tweet"));
        tvBodyReply.setText(replyTweet.body);
        etReply.setText("@" + replyTweet.user.screenName + " ");
        Glide.with(this)
                .load(replyTweet.user.profileImageUrl)
                .into(ivProfileReply);


    }

}
