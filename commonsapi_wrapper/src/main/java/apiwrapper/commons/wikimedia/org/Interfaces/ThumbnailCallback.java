package apiwrapper.commons.wikimedia.org.Interfaces;

import apiwrapper.commons.wikimedia.org.Models.Thumbnail;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public interface ThumbnailCallback {
    /**
     * @param thumbnail {@link Thumbnail} object with the requested info, url, width, height
     */
    void onThumbnailAvailable(Thumbnail thumbnail);

    void onError();
}
