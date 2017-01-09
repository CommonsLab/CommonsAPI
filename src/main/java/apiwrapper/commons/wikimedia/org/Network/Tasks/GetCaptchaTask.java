package apiwrapper.commons.wikimedia.org.Network.Tasks;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;

import apiwrapper.commons.wikimedia.org.Interfaces.CaptchaCallback;
import apiwrapper.commons.wikimedia.org.Models.Captcha;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import apiwrapper.commons.wikimedia.org.Utils.JsonParser;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 06/01/2017.
 */

public class GetCaptchaTask extends AsyncTask<Void, Void, Boolean> {

    private OkHttpClient client;
    private CaptchaCallback callback;
    private Captcha captcha = null;

    public GetCaptchaTask(OkHttpClient client, CaptchaCallback callback) {
        this.client = client;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String response = API.GET(client, RequestBuilder.getCaptchaRequest());
            Captcha captcha = JsonParser.parseCaptchaResponse(response);
            if (captcha != null) {
                this.captcha = captcha;
                return true;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean validCaptcha) {
        super.onPostExecute(validCaptcha);
        if (validCaptcha)
            callback.onCaptchaReceived(captcha);
        else
            callback.onFailure();
    }
}
