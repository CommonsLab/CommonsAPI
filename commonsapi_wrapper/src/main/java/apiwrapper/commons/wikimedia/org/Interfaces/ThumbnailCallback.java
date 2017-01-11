package apiwrapper.commons.wikimedia.org.Interfaces;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public interface ThumbnailCallback {
    /**
     * @param thumbnailURL URL to the thumbnail
     * @param thumbwidth   actual width of the thumbnail
     * @param thumbheight  actual height of the thumbnail
     */
    void onThumbnailAvailable(String thumbnailURL, String thumbwidth, String thumbheight);

    void onError();
}
