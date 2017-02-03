package apiwrapper.commons.wikimedia.org.Interfaces;

/**
 * Created by Valdio Veliu on 03/02/2017.
 */

public interface UploadCallback {

    void onMediaUploadedSuccessfully();

    /**
     * @param errorMessage Error, what went wrong
     */
    void onFailure(String errorMessage);
}
