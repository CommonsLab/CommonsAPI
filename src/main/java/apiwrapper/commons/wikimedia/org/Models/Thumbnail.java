package apiwrapper.commons.wikimedia.org.Models;

/**
 * Created by Valdio Veliu on 06/01/2017.
 */

public class Thumbnail {
    private String thumbnailURL;
    private int thumbnailWidth;
    private int thumbnailHeight;

    public Thumbnail(String thumbnailURL, int thumbnailWidth, int thumbnailHeight) {
        this.thumbnailURL = thumbnailURL;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
    }

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
}
