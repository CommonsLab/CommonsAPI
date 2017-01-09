package apiwrapper.commons.wikimedia.org.Network.Tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;

import apiwrapper.commons.wikimedia.org.Interfaces.ContributionsCallback;
import apiwrapper.commons.wikimedia.org.Models.Contribution;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import apiwrapper.commons.wikimedia.org.Utils.JsonParser;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public class LoadContributionsTask extends AsyncTask<Void, Void, Boolean> {

    private String username;
    private String limit;
    private String result;
    private OkHttpClient client;
    private ContributionsCallback callback;
    private ArrayList<Contribution> userContributions;


    public LoadContributionsTask(OkHttpClient client, String username, String limit, ContributionsCallback callback) {
        this.client = client;
        this.username = username;
        this.limit = limit;
        this.callback = callback;
        userContributions = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if ((result = getContent(username, limit)) == null) return false;
            userContributions = JsonParser.parseUsersContributions(result);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (userContributions != null && userContributions.size() > 0) {
            callback.onContributionsReceived(userContributions);
        } else {
            callback.onFailure();
        }
    }

    //Get the first +limit+ images from this specific username
    private String getContent(String username, String limit) throws IOException {
        return API.GET(client, RequestBuilder.userContributions(username, limit));
    }
}
