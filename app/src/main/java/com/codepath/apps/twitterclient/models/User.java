package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lawson on 2/20/16.
 */
public class User implements Parcelable {
    private String name;
    private String screenName;
    private String profileImageUrl;
    private String tagLine;
    private int followersCount;
    private int followingCount;
    private long uid;

    public String getTagLine() {
        return tagLine;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public long getUid() {
        return uid;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();

        try {
            user.name = jsonObject.getString("name");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.uid = jsonObject.getLong("id");
            user.tagLine = jsonObject.getString("description");
            user.followersCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    //----- Parcelable Methods

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.tagLine);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.followingCount);
        dest.writeLong(this.uid);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.tagLine = in.readString();
        this.followersCount = in.readInt();
        this.followingCount = in.readInt();
        this.uid = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
