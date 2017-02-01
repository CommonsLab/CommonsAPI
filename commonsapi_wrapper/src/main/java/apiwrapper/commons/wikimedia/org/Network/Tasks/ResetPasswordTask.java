package apiwrapper.commons.wikimedia.org.Network.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import apiwrapper.commons.wikimedia.org.Interfaces.ResetPasswordCallback;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import apiwrapper.commons.wikimedia.org.R;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 30/01/2017.
 */

public class ResetPasswordTask extends AsyncTask<Void, Void, Boolean> {

    private String email;
    private Context context;
    private OkHttpClient client;
    private ResetPasswordCallback callback;

    private String responseString;
    private String errorMessage;

    public ResetPasswordTask(Context context, OkHttpClient client, String email, ResetPasswordCallback callback) {
        this.email = email;
        this.context = context;
        this.client = client;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            responseString = API.POST(client, context.getString(R.string.WIKIMEDIA_COMMONS_API), RequestBuilder.ResetPassword(email));
            /**
             * {
             "resetpassword": {
             "status": "success"
             }
             }
             */

            //Parse JSON response to extract login token
            JSONObject jsonResponse = new JSONObject(responseString);
            JSONObject resetPasswordObject = jsonResponse.getJSONObject("resetpassword");
            String response = resetPasswordObject.getString("status");

            if (response.equals("success"))
                return true;


        } catch (IOException | JSONException e) {
            e.printStackTrace();

            extractErrorMessage();
            return false;
        }

        return false;
    }


    @Override
    protected void onPostExecute(Boolean resetSuccessful) {
        super.onPostExecute(resetSuccessful);
        if (resetSuccessful) {
            callback.onRequestSuccess();
        } else {
            callback.onFailure(errorMessage);
        }
    }

    private void extractErrorMessage() {
        try {
            //Extract error message
            JSONObject errorResponse = new JSONObject(responseString);
            JSONObject errorObject = errorResponse.getJSONObject("error");
            errorMessage = errorObject.getString("code")
                    + "\n"
                    + errorObject.getString("info");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
