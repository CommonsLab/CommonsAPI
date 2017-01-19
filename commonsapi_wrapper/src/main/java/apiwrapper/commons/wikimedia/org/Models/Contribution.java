package apiwrapper.commons.wikimedia.org.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Valdio Veliu on 06/01/2017.
 */

public class Contribution implements Parcelable {

    private String title;
    private String url;
    private String descriptionurl;
    private String mediatype;
    private String ns;
    private String duration; // used for Audio, Video duration.
                             // Don't use for images, throws Exception
    private int width;
    private int height;

    protected Contribution(Parcel in) {
        title = in.readString();
        url = in.readString();
        descriptionurl = in.readString();
        mediatype = in.readString();
        duration = in.readString();
        ns = in.readString();
        width = in.readInt();
        height = in.readInt();
    }

    public static final Creator<Contribution> CREATOR = new Creator<Contribution>() {
        @Override
        public Contribution createFromParcel(Parcel in) {
            return new Contribution(in);
        }

        @Override
        public Contribution[] newArray(int size) {
            return new Contribution[size];
        }
    };

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescriptionurl() {
        return descriptionurl;
    }

    public void setDescriptionurl(String descriptionurl) {
        this.descriptionurl = descriptionurl;
    }

    public String getMediatype() {
        return mediatype;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Contribution() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(descriptionurl);
        dest.writeString(mediatype);
        dest.writeString(ns);
        dest.writeString(duration);
        dest.writeInt(width);
        dest.writeInt(height);
    }
}