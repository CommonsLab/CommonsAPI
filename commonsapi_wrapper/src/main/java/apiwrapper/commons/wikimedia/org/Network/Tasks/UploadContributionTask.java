package apiwrapper.commons.wikimedia.org.Network.Tasks;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import apiwrapper.commons.wikimedia.org.Models.User;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.NetworkUtils.MonitoredUploadRequest;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import apiwrapper.commons.wikimedia.org.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 08/01/2017.
 */

public class UploadContributionTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private OkHttpClient client;
    private File file;
    private User user;
    private String title;
    private String comment;
    private String descriptionText;
    private String editToken;
    private String license;


    //Progress bar
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private int notificationId = 1001;
    private boolean showProgressNotification = true;


    public UploadContributionTask(Context context, OkHttpClient client, File file, User user, String title, String comment, String descriptionText, String license) {
        this.context = context;
        this.client = client;
        this.file = file;
        this.user = user;
        this.title = title;
        this.comment = comment;
        this.descriptionText = descriptionText;
        this.license = license;
    }

    public UploadContributionTask(Context context, OkHttpClient client, File file, User user, String title, String comment, String descriptionText, String license, boolean disableProgress) {
        this.context = context;
        this.client = client;
        this.file = file;
        this.user = user;
        this.title = title;
        this.comment = comment;
        this.descriptionText = descriptionText;
        this.license = license;
        //don't show progress notification
        showProgressNotification = false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showProgressNotification)
            showUploadProgress();
    }


    @Override
    protected Boolean doInBackground(Void... voids) {

        String editTokenResponse;
        try {
            //get edit token for user to upload to  WIKIMEDIA_COMMONS
            if ((editTokenResponse = API.GET(client, RequestBuilder.editToken())) == null) {
                //no token retrieved
                return false;
            }
            //Parse JSON
            JSONObject object = new JSONObject(editTokenResponse);
            JSONObject query = object.getJSONObject("query");
            JSONObject tokens = query.getJSONObject("tokens");
            editToken = tokens.getString("csrftoken");

            prepareForUpload(title, editToken);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * Notification progress dialog methods
     */
    private void showUploadProgress() {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Uploading...")
                .setContentText("Upload in progress")
                .setSmallIcon(R.drawable.upload_icon);

        // Displays the progress bar for the first time.
        builder.setProgress(100, 0, false);
        notificationManager.notify(notificationId, builder.build());
    }

    private void uploadStatusUpdate(int max, int progress) {
        builder.setProgress(max, progress, false);
        notificationManager.notify(notificationId, builder.build());
    }

    private void uploadSuccessful() {
        builder.setContentTitle("Upload successful")
                .setContentText("Thank you for your the contribution");
        // Removes the progress bar
        builder.setProgress(100, 100, false);
        notificationManager.notify(notificationId, builder.build());
    }

    private void uploadFailed() {
        builder.setContentTitle("Upload Failed")
                .setContentText("Failed to upload media...");
        // Removes the progress bar
        builder.setProgress(100, 100, false);
        notificationManager.notify(notificationId, builder.build());
    }


    public void prepareForUpload(String title, String token) {
        try {
            /** // Call example
             *
             *  /w/api.php?action=upload&format=json&filename=paint.png
             *  &comment=paint+drawing+&text=Uploading+a+painted+image+to+commons....&ignorewarnings=1
             *  &file=...&token=f992e93fe2fce96e6ab5e6e84deb881858723e10%2B%5C
             */

            //get image format
            String fileName = file.getName();
            String imageFormat = fileName.substring(fileName.lastIndexOf(".") + 1);
            imageFormat.toLowerCase();

            //MediaType MEDIA_TYPE = MediaType.parse("image/png");
            MediaType MEDIA_TYPE = MediaType.parse("image/" + imageFormat);

            boolean uploadedSuccessfully = UPLOAD(title, imageFormat, token, MEDIA_TYPE);

            if (showProgressNotification) {
                if (uploadedSuccessfully)
                    uploadSuccessful();
                else
                    uploadFailed();
            }
        } catch (Exception ex) {
            // Handle the error
        }
    }

    private boolean UPLOAD(String title, String imageFormat, String token, MediaType MEDIA_TYPE) {
        MonitoredUploadRequest.Listener listener = new MonitoredUploadRequest.Listener() {
            @Override
            public void onProgress(int progress) {
                Log.d("Upload Status", progress + "%");

                if (showProgressNotification) {
                    if (progress > 0)
                        uploadStatusUpdate(100, progress);
                    else
                        uploadStatusUpdate(0, 0);
                }
            }
        };

        MonitoredUploadRequest monitoredRequest = new MonitoredUploadRequest(
                RequestBuilder.uploadRequestBody(
                        user.getUsername(),
                        title,
                        comment,
                        descriptionText,
                        license,
                        imageFormat,
                        token,
                        MEDIA_TYPE,
                        file)
                , listener);


        try {
            String response = API.POST(client, context.getString(R.string.WIKIMEDIA_COMMONS_API), monitoredRequest);

            //Parse JSON response
            //check if successful upload
            JSONObject jsonResponse = new JSONObject(response);
            String responseString = jsonResponse.getJSONObject("upload").getString("result");
            if (responseString.equals("Success")) return true;   //Upload successful

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
