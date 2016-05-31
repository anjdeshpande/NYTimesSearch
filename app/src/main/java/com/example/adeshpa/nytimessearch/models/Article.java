package com.example.adeshpa.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by adeshpa on 5/28/16.
 */
public class Article implements Parcelable{
    String webUrl;
    String headline;
    String thumbnail;

    protected Article(Parcel in) {
        webUrl = in.readString();
        headline = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Article (JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                JSONObject multimediajson = multimedia.getJSONObject(0);
                this.thumbnail = "http://www.nytimes.com/" + multimediajson.getString("url");
            } else {
                this.thumbnail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJsonArray (JSONArray jsonArray) {
        ArrayList<Article> results = new ArrayList<Article>();
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                results.add (new Article(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {

            }
        }
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webUrl);
        dest.writeString(headline);
        dest.writeString(thumbnail);
    }
}
