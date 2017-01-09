package apiwrapper.commons.wikimedia.org.Network.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import apiwrapper.commons.wikimedia.org.Interfaces.CreateAccountCallback;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import apiwrapper.commons.wikimedia.org.R;
import apiwrapper.commons.wikimedia.org.Enums.LoginResponseStatus;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 06/01/2017.
 */

public class CreateAccountTask extends AsyncTask<Void, Void, Boolean> {

    private String username;
    private String password;
    private String retypePassword;
    private String email;
    private String captchaWord;
    private String captchaId;

    private Context context;
    private OkHttpClient client;
    private CreateAccountCallback callback;

    private String errorMessage = "Something went wrong...";

    public CreateAccountTask(Context context, OkHttpClient client, String username, String password, String retypePassword, String email, String captchaWord, String captchaId, CreateAccountCallback callback) {
        this.username = username;
        this.password = password;
        this.retypePassword = retypePassword;
        this.email = email;
        this.captchaWord = captchaWord;
        this.captchaId = captchaId;
        this.context = context;
        this.client = client;
        this.callback = callback;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean accountCreated = false;
        try {
            String createAccountToken = getCreateAccountToken();
//            Log.d("createAccount Token", createAccountToken);
            if (createAccountToken != null && !createAccountToken.equals("")) {
                accountCreated = CreateAccount(username, password, retypePassword, email, createAccountToken, captchaWord, captchaId);
                return accountCreated;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return accountCreated;
    }

    @Override
    protected void onPostExecute(Boolean loggedIn) {
        if (loggedIn) {
            callback.onAccountCreatedSuccessful();
        } else {
            callback.onFailure(errorMessage);
        }
    }


    private String getCreateAccountToken() throws IOException, JSONException {
        String response = API.POST(
                client,
                context.getString(R.string.WIKIMEDIA_COMMONS_API),
                RequestBuilder.createAccountToken());

        /**
         * Response format
         *
         {
         "batchcomplete": "",
         "query": {
         "tokens": {
         "createaccounttoken": "e7658d8fba1fce67dfc8c72dc742887b5870d4d1+\\"
         }
         }
         }
         */

        //Parse JSON response to extract login token
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject jsonQuery = jsonResponse.getJSONObject("query");
        JSONObject jsonTokens = jsonQuery.getJSONObject("tokens");
        String token = jsonTokens.getString("createaccounttoken");
        return token;
    }


    //Login method, retrieve  users information
    private boolean CreateAccount(String username, String password, String retypePassword, String email, String token, String captchaWord, String captchaId) throws IOException {
        String response = API.POST( //POST network call to create account
                client,
                context.getString(R.string.WIKIMEDIA_COMMONS_API),
                RequestBuilder.CreateAccountBody(username, password, retypePassword, email, token, captchaWord, captchaId));

        Log.d("Response", response);

        //check response string is correct
        if (response.contains(LoginResponseStatus.PASS.toString())) {
            // account created
            return true;
        }
        //failed to create account
        extractErrorMessage(response);
        return false;
    }

    private void extractErrorMessage(String errorResponse) {
        try {
            JSONObject response = new JSONObject(errorResponse);
            errorMessage = response
                    .getJSONObject("createaccount")
                    .getString("message")
                    .replaceAll("\\<.*?>", "");// extract from html response
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
