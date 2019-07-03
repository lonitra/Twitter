package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
    TextView tvCharactersLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        tvBodyReply = findViewById(R.id.tvBodyReply);
        etReply = findViewById(R.id.etReply);
        ivProfileReply = findViewById(R.id.ivProfileReply);
        tvCharactersLeft = findViewById(R.id.tvCharactersLeft);
        Tweet replyTweet = Parcels.unwrap(getIntent().getParcelableExtra("Tweet"));
        tvBodyReply.setText(replyTweet.body);
        etReply.setText("@" + replyTweet.user.screenName + " ");
        tvCharactersLeft.setText(280 - etReply.getText().length() + " characters left");
        Glide.with(this)
                .load(replyTweet.user.profileImageUrl)
                .into(ivProfileReply);

        etReply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int characters = etReply.getText().length();
                tvCharactersLeft.setText((280 - s.length()) + " characters left");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

}
