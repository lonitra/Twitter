package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {
    public String body;
    public long uid; //database ID for tweet
    public User user;
    public String createdAt;
    public String media;

    public static Tweet fromJSON(JSONObject object)throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = object.getString("text");
        tweet.uid = object.getLong("id");
        tweet.createdAt = object.getString("created_at");
        tweet.user = User.fromJSON(object.getJSONObject("user"));
        JSONObject entities = object.getJSONObject("entities");
        tweet.media = "";
        if(entities.has("media")) {
            JSONArray medias = entities.getJSONArray("media");
            JSONObject object2 = (JSONObject) medias.get(0);
            tweet.media = object2.getString("media_url");
        }
        return tweet;
    }

    public Tweet () {

    }

}
