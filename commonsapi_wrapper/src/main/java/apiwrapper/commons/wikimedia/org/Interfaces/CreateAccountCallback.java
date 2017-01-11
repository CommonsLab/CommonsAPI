package apiwrapper.commons.wikimedia.org.Interfaces;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public interface CreateAccountCallback {
    void onAccountCreatedSuccessful();

    /**
     *
     * @param errorMessage Error, what went wrong
     */
    void onFailure(String errorMessage);
}
