package apiwrapper.commons.wikimedia.org.Network.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import apiwrapper.commons.wikimedia.org.Interfaces.LoginCallback;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import apiwrapper.commons.wikimedia.org.Enums.LoginResponseStatus;
import apiwrapper.commons.wikimedia.org.R;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private String username;
    private String password;
    private Context context;
    private OkHttpClient client;
    private LoginCallback callback;

    public UserLoginTask(Context context, OkHttpClient client, String username, String password, LoginCallback callback) {
        this.context = context;
        this.client = client;
        this.password = password;
        this.username = username;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean loggedIn = false;
        // authenticate the user
        try {
            String loginToken = getLoginToken();
            if (loginToken != null && !loginToken.equals("")) {
                loggedIn = Login(username, password, loginToken);
                return loggedIn;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return loggedIn;
    }

    @Override
    protected void onPostExecute(Boolean loggedIn) {
        if (loggedIn) {
            callback.onLoginSuccessful();
        } else {
            callback.onFailure();
        }
    }

    private String getLoginToken() throws IOException, JSONException {
        String response = API.GET(client, RequestBuilder.LoginToken());

        /**
         * Response format
         *
         {
         "batchcomplete": "",
         "query": {
         "tokens": {
         "logintoken": "******************token************+\\"
         }
         }
         }
         */

        //Parse JSON response to extract login token
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject jsonQuery = jsonResponse.getJSONObject("query");
        JSONObject jsonTokens = jsonQuery.getJSONObject("tokens");
        String token = jsonTokens.getString("logintoken");
        return token;
    }

    //Login method, retrieve  users information
    private boolean Login(String username, String password, String token) throws IOException {
        String response = API.POST( //POST network call to perform login
                client,
                context.getString(R.string.WIKIMEDIA_COMMONS_API),
                RequestBuilder.LoginBody(username, password, token));

        //check response string is correct
        if (response.contains(LoginResponseStatus.PASS.toString())) {
            // logged in
            return true;
        }
        return false;
    }
}

