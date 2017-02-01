package apiwrapper.commons.wikimedia.org.Interfaces;

/**
 * Created by Valdio Veliu on 30/01/2017.
 */

public interface ResetPasswordCallback {
    void onRequestSuccess();

    void onFailure(String errorMessage);
}
