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
 * Created by Valdio Veliu on 09/01/2017.
 */

public class SearchContributionsTask extends AsyncTask<Void, Void, Boolean> {

    private String searchString;
    private String limit;
    private OkHttpClient client;
    private ContributionsCallback callback;
    private ArrayList<Contribution> userContributions;
    private String result;

    public SearchContributionsTask(OkHttpClient client, String searchString, String limit, ContributionsCallback callback) {
        this.searchString = searchString;
        this.limit = limit;
        this.client = client;
        this.callback = callback;
        userContributions = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            if ((result = loadContributions(searchString, limit)) == null) return false;
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

    //Search for first +limit+ images based on the searchString
    private String loadContributions(String searchString, String limit) throws IOException {
        return API.GET(client, RequestBuilder.searchCommons(searchString, limit));
    }
}
