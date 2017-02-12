package apiwrapper.commons.wikimedia.org.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Valdio Veliu on 06/01/2017.
 */

public class Thumbnail implements Parcelable {
    private String thumbnailURL;
    private int thumbnailWidth;
    private int thumbnailHeight;

    public Thumbnail(String thumbnailURL, int thumbnailWidth, int thumbnailHeight) {
        this.thumbnailURL = thumbnailURL;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
    }

    protected Thumbnail(Parcel in) {
        thumbnailURL = in.readString();
        thumbnailWidth = in.readInt();
        thumbnailHeight = in.readInt();
    }

    public static final Creator<Thumbnail> CREATOR = new Creator<Thumbnail>() {
        @Override
        public Thumbnail createFromParcel(Parcel in) {
            return new Thumbnail(in);
        }

        @Override
        public Thumbnail[] newArray(int size) {
            return new Thumbnail[size];
        }
    };

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(thumbnailURL);
        parcel.writeInt(thumbnailWidth);
        parcel.writeInt(thumbnailHeight);
    }
}
