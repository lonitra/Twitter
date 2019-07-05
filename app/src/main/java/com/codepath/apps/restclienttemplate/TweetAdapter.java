package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    List<Tweet> mTweets;
    Context context;

    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    //populates info on timeline  based on position
    @Override
    public void onBindViewHolder(@NonNull TweetAdapter.ViewHolder viewHolder, final int position) {
        final Tweet tweet = mTweets.get(position);
        viewHolder.tvBody.setText(tweet.body);
        viewHolder.tvUsername.setText(tweet.user.name);
        viewHolder.tvCreatedAt.setText(getRelativeTimeAgo(tweet.createdAt));
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(viewHolder.ivProfileImage);
        viewHolder.fabReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyTweet = new Intent(context, ReplyActivity.class);
                replyTweet.putExtra("Tweet", Parcels.wrap(tweet));
                context.startActivity(replyTweet);
            }
        });
        if(!tweet.media.equals("")) {
            Glide.with(context)
                    .load(tweet.media)
                    .into(viewHolder.ivMedia);
        }
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //converts time tweet was created at and converts the time relative to the user
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvCreatedAt;
        public FloatingActionButton fabReply;
        public ImageView ivMedia;


        public ViewHolder(View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            fabReply = itemView.findViewById(R.id.fabReply);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            itemView.setOnClickListener(this); //sets onClickListener to viewholder itself
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                Tweet tweet = mTweets.get(position);
                Intent details = new Intent(context, TweetDetailActivity.class);
                details.putExtra("TweetDetails", Parcels.wrap(tweet));
                context.startActivity(details);
            }
        }


    }

}
