package apiwrapper.commons.wikimedia.org.Network.Tasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import apiwrapper.commons.wikimedia.org.Interfaces.ThumbnailCallback;
import apiwrapper.commons.wikimedia.org.Models.Thumbnail;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */
public class ThumbnailLoadTask extends AsyncTask<Void, Void, Boolean> {

    private String FileName;
    private String thumbnailURL = "";
    private String thumbwidth = "";
    private String thumbheight = "";
    private int thumbnailWidth;
    private int thumbnailHeight;
    private OkHttpClient client;
    private ThumbnailCallback callback;


    public ThumbnailLoadTask(OkHttpClient client, String FileName, int thumbnailWidth, int thumbnailHeight, ThumbnailCallback callback) {
        this.client = client;
        this.FileName = FileName;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String response = loadImageThumbnail(FileName, thumbnailWidth, thumbnailHeight);
            List<String> thumbnailDetails = extractThumbnailURL(response);
            thumbnailURL = thumbnailDetails.get(0);
            thumbwidth = thumbnailDetails.get(1);
            thumbheight = thumbnailDetails.get(2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            //extractThumbnailURL may generate NullPointerException
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success && !thumbnailURL.equals("")) {
            //Callback method to return thumbnail details
            callback.onThumbnailAvailable(new Thumbnail(thumbnailURL, Integer.parseInt(thumbwidth), Integer.parseInt(thumbheight)));
        } else {
            callback.onError();
        }
    }

    //Make a request to the API to retrieve a thumbnail for the Image
    private String loadImageThumbnail(String fileName, int thumbnailWidth, int thumbnailHeight) throws IOException {
        return API.GET(client, RequestBuilder.thumbnailRequest(fileName, thumbnailWidth, thumbnailHeight));
    }

    //Extract the thumbnail URL from JSON response
    private List<String> extractThumbnailURL(String response) {
        List<String> list = new ArrayList<>();
        try {
            JSONObject query = new JSONObject(response).getJSONObject("query");
            JSONObject pages = query.getJSONObject("pages");
            //The page ID isn't constant
            //get the keys inside the +pages+ JSONObject
            Iterator<String> keys = pages.keys();
            if (keys.hasNext()) {
                //get the object represented by the key
                JSONObject object = pages.getJSONObject(keys.next());
                //Use the first key, page Id to get the Array of images
                JSONArray array = object.getJSONArray("imageinfo");
                // The array has only one entry
                // get the first entry thumbnail URL
                String thumbnail = array.getJSONObject(0).getString("thumburl");
                String thumbwidth = array.getJSONObject(0).getString("thumbwidth");
                String thumbheight = array.getJSONObject(0).getString("thumbheight");

                list.add(thumbnail);
                list.add(thumbwidth);
                list.add(thumbheight);
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
