package apiwrapper.commons.wikimedia.org;

import android.content.Context;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;

import apiwrapper.commons.wikimedia.org.Enums.ContributionType;
import apiwrapper.commons.wikimedia.org.Interfaces.CaptchaCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.ContributionsCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.CreateAccountCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.LoginCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.LogoutCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.RSS_FeedCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.ResetPasswordCallback;
import apiwrapper.commons.wikimedia.org.Interfaces.ThumbnailCallback;
import apiwrapper.commons.wikimedia.org.Models.User;
import apiwrapper.commons.wikimedia.org.Network.Tasks.CreateAccountTask;
import apiwrapper.commons.wikimedia.org.Network.Tasks.GetCaptchaTask;
import apiwrapper.commons.wikimedia.org.Network.Tasks.LoadContributionsTask;
import apiwrapper.commons.wikimedia.org.Network.Tasks.LogoutTask;
import apiwrapper.commons.wikimedia.org.Network.Tasks.RSS_FeedTask;
import apiwrapper.commons.wikimedia.org.Network.Tasks.ResetPasswordTask;
import apiwrapper.commons.wikimedia.org.Network.Tasks.SearchContributionsTask;
import apiwrapper.commons.wikimedia.org.Network.Tasks.ThumbnailLoadTask;
import apiwrapper.commons.wikimedia.org.Network.Tasks.UploadContributionTask;
import apiwrapper.commons.wikimedia.org.Network.Tasks.UserLoginTask;
import apiwrapper.commons.wikimedia.org.Enums.MediaType;
import apiwrapper.commons.wikimedia.org.Utils.NetworkStatus;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public class Commons {

    private Context context;
    private OkHttpClient client;

    /**
     * Commons class constructor
     *
     * @param context
     */
    public Commons(Context context) {
        this.context = context;

        if (client == null)
            initOkHttpClient();
    }


    /**
     * Method to initiate the OkHttpClient
     * Init ClearableCookieJar to store Cookies based on SharedPreferences
     */
    private void initOkHttpClient() {
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
    }


    /**
     * Show a toast notification to the user
     *
     * @param message Notification text
     */
    private void showToastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Login call.
     * Perform the complete login operations
     *
     * @param username user credential
     * @param password user credential
     * @param callBack callback to return the login status, success or failure
     */
    public void userLogin(String username, String password, LoginCallback callBack) {
        if (NetworkStatus.networkAvailable(context))
            new UserLoginTask(context, client, username, password, callBack).execute();
        else
            showToastMessage("Network Offline");
    }

    /**
     * Logout call to clear session
     */
    public void userLogout(LogoutCallback callBack) {
        if (NetworkStatus.networkAvailable(context))
            new LogoutTask(context, client, callBack).execute();
        else
            showToastMessage("Network Offline");
    }

    /**
     * {@link ResetPasswordTask} reset the username
     *
     * @param email    Email of the account
     * @param callBack
     */
    public void resetPassword(String email, ResetPasswordCallback callBack) {
        if (NetworkStatus.networkAvailable(context))
            new ResetPasswordTask(context, client, email, callBack).execute();
        else
            showToastMessage("Network Offline");
    }

    /**
     * CreateAccount call.
     * Perform the complete createAccount operations
     *
     * @param username       user credential
     * @param password       user credential
     * @param retypePassword user credential
     * @param email          user credential
     * @param captchaWord    captcha String entered by the user
     * @param captchaId      the Id of the captchaWord that needs to be verified in the API
     * @param callBack       callback to return the account creation status, account created or not
     */
    public void createAccount(String username, String password, String retypePassword, String email, String captchaWord, String captchaId, CreateAccountCallback callBack) {
        if (NetworkStatus.networkAvailable(context))
            new CreateAccountTask(context, client, username, password, retypePassword, email, captchaWord, captchaId, callBack).execute();
        else
            showToastMessage("Network Offline");
    }


    /**
     * Method to load a captcha item form WikimediaCommons
     * Necessary in case of account creation
     *
     * @param callback callback to return a {@link apiwrapper.commons.wikimedia.org.Models.Captcha} item
     */
    public void getCaptcha(CaptchaCallback callback) {
        if (NetworkStatus.networkAvailable(context))
            new GetCaptchaTask(client, callback).execute();
        else
            showToastMessage("Network Offline");
    }


    /**
     * Load the Contributions of a user
     *
     * @param username Currently logged in user
     * @param limit    the number of Contributions to be loaded,
     *                 0 - 500, set limit to `max` to load 500 Contributions
     * @param callback callback to return the list of Contributions
     */
    public void loadContributions(String username, String limit, ContributionsCallback callback) {
        if (NetworkStatus.networkAvailable(context))
            new LoadContributionsTask(client, username, limit, callback).execute();
        else
            showToastMessage("Network Offline");
    }


    /**
     * Search for Contributions based on a searchString
     *
     * @param searchString what to search for in WikimediaCommons?
     * @param limit        the number of Contributions to be loaded,
     *                     0 - 500, set limit to `max` to load 500 Contributions
     * @param callback     callback to return the list of Contributions
     */
    public void searchInCommons(String searchString, String limit, ContributionsCallback callback) {
        if (NetworkStatus.networkAvailable(context))
            new SearchContributionsTask(client, searchString, limit, callback).execute();
        else
            showToastMessage("Network Offline");
    }


    /**
     * Method to load the RSS Feed from WikimediaCommons
     * Get the last 10 items of the RSS feed of WikimediaCommons
     * The last 10 pictures of the day form the feed
     *
     * @param rssFeedCallback callback to return the list of {@link apiwrapper.commons.wikimedia.org.Models.FeedItem}
     */
    public void getPictureOfTheDay(RSS_FeedCallback rssFeedCallback) {
        if (NetworkStatus.networkAvailable(context))
            new RSS_FeedTask(MediaType.PICTURE, rssFeedCallback).execute();
        else
            showToastMessage("Network Offline");
    }


    /**
     * Method to load the RSS Feed from WikimediaCommons
     * Get the last 10 items of the RSS feed of WikimediaCommons
     * The last 10 medias of the day form the feed
     *
     * @param rssFeedCallback - callback to return the list of {@link apiwrapper.commons.wikimedia.org.Models.FeedItem}
     */
    public void getMediaOfTheDay(RSS_FeedCallback rssFeedCallback) {
        if (NetworkStatus.networkAvailable(context))
            new RSS_FeedTask(MediaType.MEDIA, rssFeedCallback).execute();
        else
            showToastMessage("Network Offline");
    }


    /**
     * Load the thumbnail of a specified media
     * Assign the height and width of the requested thumbnail
     * Width and Height might come slightly changed from the required
     *
     * @param FileName        The name of Contribution whose thumbnail is required
     *                        //FileName -> File:Some_Image/Video_Name.png
     * @param thumbnailWidth  width of the requested thumbnail
     * @param thumbnailHeight height of the requested thumbnail
     * @param callback        callback to return a {@link apiwrapper.commons.wikimedia.org.Models.Thumbnail} item
     */
    public void loadThumbnail(String FileName, int thumbnailWidth, int thumbnailHeight, ThumbnailCallback callback) {
        if (NetworkStatus.networkAvailable(context))
            new ThumbnailLoadTask(client, FileName, thumbnailWidth, thumbnailHeight, callback).execute();
        else
            showToastMessage("Network Offline");
    }


    /**
     * Upload method for uploading Contribution files
     * The following methods can be used to upload files with a upload progress Notification
     *
     * @param file             Contribution file to be uploaded
     * @param user             The currently logged in user
     * @param title            Title of the Contribution media
     * @param comment          A comment on the Contribution
     * @param descriptionText  A description on the what is being uploaded
     * @param contributionType The type of the media that is being uploaded IMAGE, VIDEO or AUDIO
     * @param license          The license string under which the Contribution will be uploaded
     */
    public void uploadContribution(File file, User user, String title, String comment, String descriptionText, ContributionType contributionType, String license, int uploadIconResourceId) {
        if (NetworkStatus.networkAvailable(context))
            new UploadContributionTask(context, client, file, user, title, comment, descriptionText, contributionType, license, uploadIconResourceId).execute();
        else
            showToastMessage("Network Offline");
    }


    /**
     * Upload method for uploading Contribution files without showing the upload progress
     *
     * @param file             Contribution file to be uploaded
     * @param user             The currently logged in user
     * @param title            Title of the Contribution media
     * @param comment          A comment on the Contribution
     * @param descriptionText  A description on the what is being uploaded
     * @param contributionType The type of the media that is being uploaded IMAGE, VIDEO or AUDIO
     * @param license          The license string under which the Contribution will be uploaded
     */
    public void uploadContributionWithoutProgress(File file, User user, String title, String comment, String descriptionText, ContributionType contributionType, String license) {
        if (NetworkStatus.networkAvailable(context))
            new UploadContributionTask(context, client, file, user, title, comment, descriptionText, contributionType, license, false).execute();
        else
            showToastMessage("Network Offline");
    }
}
