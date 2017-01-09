package apiwrapper.commons.wikimedia.org.Interfaces;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public interface ThumbnailCallback {
    void onThumbnailAvailable(String thumbnailURL, String thumbwidth, String thumbheight);

    void onError();
}
