package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ReplyActivity extends AppCompatActivity {
    TextView tvBodyReply;
    EditText etReply;
    ImageView ivProfileReply;
    TextView tvCharactersLeft;
    TwitterClient client;
    Button btnSendReply;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        tvBodyReply = findViewById(R.id.tvBodyReply);
        etReply = findViewById(R.id.etReply);
        ivProfileReply = findViewById(R.id.ivProfileReply);
        tvCharactersLeft = findViewById(R.id.tvCharactersLeft);
        btnSendReply = findViewById(R.id.btnSendReply);
        client = TwitterApp.getRestClient(this);
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

        btnSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReply();
            }
        });

    }

    public void sendReply() {
        client.sendTweet(etReply.getText().toString(), new AsyncHttpResponseHandler() {
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
                        Log.e("ReplyActivity", "Error parsing response", e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

}
