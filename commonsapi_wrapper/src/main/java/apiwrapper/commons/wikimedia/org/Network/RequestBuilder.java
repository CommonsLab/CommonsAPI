package apiwrapper.commons.wikimedia.org.Network;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public class RequestBuilder {

    //Construct the request URL

    /**
     * Login functions
     */
    //Login token
    public static HttpUrl LoginToken() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("commons.wikimedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "query")
                .addQueryParameter("format", "json")
                .addQueryParameter("meta", "tokens")
                .addQueryParameter("type", "login")
                .build();
    }

    //Login request
    public static RequestBody LoginBody(String username, String password, String token) {
        return new FormBody.Builder()
                .add("action", "clientlogin")
                .add("format", "json")
                .add("loginreturnurl", "https://commons.wikimedia.org/")
                .add("username", username)
                .add("password", password)
                .add("logintoken", token)
                .build();
    }

    ///w/api.php?action=logout&format=json
    //Logout request
    public static RequestBody LogoutBody() {
        return new FormBody.Builder()
                .add("action", "logout")
                .add("format", "json")
                .build();
    }


    /**
     * Create account methods
     */
    //Create Account token
    public static RequestBody createAccountToken() {
        return new FormBody.Builder()
                .add("action", "query")
                .add("format", "json")
                .add("meta", "tokens")
                .add("type", "createaccount")
                .build();
    }

    //Create Account Request
    public static RequestBody CreateAccountBody(String username, String password, String retypePassword, String email, String token, String captchaWord, String captchaId) {
        return new FormBody.Builder()
                .add("action", "createaccount")
                .add("format", "json")
                .add("createreturnurl", "https://commons.wikimedia.org/")
                .add("username", username)
                .add("password", password)
                .add("retype", retypePassword)
                .add("email", email)
                .add("createtoken", token)
                .add("captchaWord", captchaWord)
                .add("captchaId", captchaId)
                .build();
    }

    //ResetPassword request
    public static RequestBody ResetPassword(String email) {
        return new FormBody.Builder()
                .add("action", "resetpassword")
                .add("format", "json")
                .add("email", email)
                .add("token", "+\\")
                .build();
    }

    //Get Captcha
    public static HttpUrl getCaptchaRequest() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("commons.wikimedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "query")
                .addQueryParameter("format", "json")
                .addQueryParameter("meta", "authmanagerinfo")
                .addQueryParameter("amirequestsfor", "create")
                .build();
    }


    /**
     * RSS Feed methods
     */
    // Picture of the day RSS feed
    public static HttpUrl pictureOfTheDay() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("commons.wikimedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "featuredfeed")
                .addQueryParameter("format", "xml")
                .addQueryParameter("feed", "potd")
                .build();
    }

    // Media of the day RSS feed
    public static HttpUrl mediaOfTheDay() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("commons.wikimedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "featuredfeed")
                .addQueryParameter("format", "xml")
                .addQueryParameter("feed", "motd")
                .build();
    }

    /**
     * Load the user contributions method
     *
     * @param username - currently logged in user
     * @param limit    - max number of Contributions to load
     */
    //Users contributions
    public static HttpUrl userContributions(String username, String limit) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("commons.wikimedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "query")
                .addQueryParameter("format", "json")
                .addQueryParameter("list", "allimages")
                .addQueryParameter("aisort", "timestamp")
                .addQueryParameter("aidir", "descending")
                .addEncodedQueryParameter("aiprop", "url%7Cmediatype%7Cdimensions")
                .addQueryParameter("aiuser", username)
                .addQueryParameter("ailimit", limit)
                .build();
    }

    /**
     * Build the API call to search for a list of Contributions
     *
     * @param searchString - String to load from Commons
     * @param limit        - max number of Contributions to load
     */
    public static HttpUrl searchCommons(String searchString, String limit) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("commons.wikimedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "query")
                .addQueryParameter("format", "json")
                .addQueryParameter("list", "allimages")
                .addEncodedQueryParameter("aiprop", "url%7Cmediatype%7Cdimensions")
                .addQueryParameter("aiprefix", searchString)
                .addQueryParameter("ailimit", limit)
                .build();
    }


    /**
     * Load thumbnail
     * Build the API call to load a media thumbnail
     *
     * @param fileName        - The name of Contribution whose thumbnail is required
     * @param thumbnailWidth  - width of the requested thumbnail
     * @param thumbnailHeight - height of the requested thumbnail
     */
    public static HttpUrl thumbnailRequest(String fileName, int thumbnailWidth, int thumbnailHeight) {
        //Construct the request URL
        return new HttpUrl.Builder()
                .scheme("https")
                .host("commons.wikimedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "query")
                .addQueryParameter("prop", "imageinfo")
                .addQueryParameter("format", "json")
                .addQueryParameter("iiprop", "url")
                .addQueryParameter("iiurlwidth", String.valueOf(thumbnailWidth))
                .addQueryParameter("iiurlheight", String.valueOf(thumbnailHeight))
                .addQueryParameter("titles", fileName)
                .build();
    }


    /**
     * Upload methods
     */

    //User needs edit token to upload files to Commons
    public static HttpUrl editToken() {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("commons.wikimedia.org")
                .addPathSegment("w")
                .addPathSegment("api.php")
                .addQueryParameter("action", "query")
                .addQueryParameter("format", "json")
                .addQueryParameter("meta", "tokens")
                .build();
    }

    //Upload request body
    public static MultipartBody uploadRequestBody(String username, String title, String comment, String descriptionText, String license, String imageFormat, String token, MediaType MEDIA_TYPE, File file) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("action", "upload")
                .addFormDataPart("format", "json")
                .addFormDataPart("filename", title + "." + imageFormat) //e.g. title.jpg
                .addFormDataPart("comment", comment)
                .addFormDataPart("text", buildUploadString(username, descriptionText, license))
                .addFormDataPart("ignorewarnings", "1")
                .addFormDataPart("file", "...", RequestBody.create(MEDIA_TYPE, file))
                .addFormDataPart("token", token)
                .build();
    }

    //Build a text string to add necessary info to upload Contribution
    //description, license. Contribution uploaded to `app_name` Category
    //Need other ideas on this solution....
    //Other solutions to attach license to upload ???
    private static String buildUploadString(String username, String descriptionText, String license) {
        String dateStr = "|date=" + uploadDate() + "\n";  //date  "yyyy-MM-dd"
        String fileDescHeader = "=={{int:filedesc}}==\n{{Information\n";
        String description = "|description={{en|1=" + descriptionText + "}} \n";
        String source = "|source={{own}}\n";
        String author = "|author=[[" + username + /*"|" + username + */ "]]\n";
        String permission = "|permission=\n";
        String otherVersions = "|other versions=\n";
        String fileDescEnd = "}}\n\n";
        String licenseStr = "== {{int:license-header}} ==\n{{self|" + license + "}}\n\n";

        String textStr = fileDescHeader + description + dateStr + source + author + permission + otherVersions + fileDescEnd + licenseStr;

        textStr += "[[Category:" + "Uploaded from CommonsLab Android" + "]]\n";
        return textStr;
    }

    //Returns the current date
    //date used to upload media files
    private static String uploadDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
        return formattedDate;
    }
}
