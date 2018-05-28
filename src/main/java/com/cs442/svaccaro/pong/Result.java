package com.cs442.svaccaro.pong;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable{

    String name;
    String releasedate;
    String websiteurl;
    String pegi;
    String imageurl;
    String console;
    String summary;
    long steamid;
    double criticrating;
    double rating;
    int dbID;

    public Result()
    {
        this.name = "N/A";
        this.releasedate = "N/A";
        this.websiteurl = "N/A";
        this.pegi = "N/A";
        this.imageurl = "N/A";
        this.console = "N/A";
        this.summary = "N/A";
        this.steamid = -1;
        this.criticrating = -1;
        this.rating = -1;
        this.dbID=-1;
    }
    public Result(String name, String releasedate, String websiteurl, String pegi, String imageurl, String console, String summary, long steamid, double criticrating, double rating,int newDB) {
        this.name = name;
        this.releasedate = releasedate;
        this.websiteurl = websiteurl;
        this.pegi = pegi;
        this.imageurl = imageurl;
        this.console = console;
        this.summary = summary;
        this.steamid = steamid;
        this.criticrating = criticrating;
        this.rating = rating;
        this.dbID=newDB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getWebsiteurl() {
        return websiteurl;
    }

    public void setWebsiteurl(String websiteurl) {
        this.websiteurl = websiteurl;
    }

    public String getPegi() {
        return pegi;
    }

    public void setPegi(String pegi) {
        this.pegi = pegi;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getSteamid() {
        return steamid;
    }

    public void setSteamid(long steamid) {
        this.steamid = steamid;
    }

    public double getCriticrating() {
        return criticrating;
    }

    public void setCriticrating(double criticrating) {
        this.criticrating = criticrating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.name);
        parcel.writeString(this.releasedate);
        parcel.writeString(this.websiteurl);
        parcel.writeString(this.pegi);
        parcel.writeString(this.imageurl);
        parcel.writeString(this.console);
        parcel.writeString(this.summary);
        parcel.writeLong(this.steamid);
        parcel.writeDouble(this.criticrating);
        parcel.writeDouble(this.rating);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public Result(Parcel in){
        this.name = in.readString();
        this.releasedate = in.readString();
        this.websiteurl = in.readString();
        this.pegi = in.readString();
        this.imageurl = in.readString();;
        this.console = in.readString();;
        this.summary = in.readString();;
        this.steamid = in.readLong();
        this.criticrating = in.readDouble();
        this.rating = in.readDouble();
    }
}
