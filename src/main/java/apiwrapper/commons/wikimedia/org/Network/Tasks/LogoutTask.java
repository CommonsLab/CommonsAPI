package apiwrapper.commons.wikimedia.org.Network.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import apiwrapper.commons.wikimedia.org.Interfaces.LogoutCallback;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import apiwrapper.commons.wikimedia.org.R;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 09/01/2017.
 */

public class LogoutTask extends AsyncTask<Void, Void, Boolean> {
    private OkHttpClient client;
    private LogoutCallback callback;
    private Context context;

    public LogoutTask(Context context, OkHttpClient client, LogoutCallback callback) {
        this.client = client;
        this.callback = callback;
        this.context = context;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            String response = API.POST(
                    client,
                    context.getString(R.string.WIKIMEDIA_COMMONS_API),
                    RequestBuilder.LogoutBody());
            if (response.equals("{}") && !response.contains("error"))
                return true;
            else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean loggedOut) {
        super.onPostExecute(loggedOut);
        if (loggedOut) {
            callback.onLogoutSuccessful();
        } else {
            callback.onFailure();
        }
    }
}
