package apiwrapper.commons.wikimedia.org.Network.Tasks;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import apiwrapper.commons.wikimedia.org.Models.FeedItem;
import apiwrapper.commons.wikimedia.org.Utils.CommonsRssFeed.WikimediaCommonsXmlParser;
import apiwrapper.commons.wikimedia.org.Interfaces.RSS_FeedCallback;
import apiwrapper.commons.wikimedia.org.Network.API;
import apiwrapper.commons.wikimedia.org.Network.RequestBuilder;
import apiwrapper.commons.wikimedia.org.Enums.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public class RSS_FeedTask extends AsyncTask<Void, Void, String> {

    private String response;
    private MediaType mediaType;
    private ArrayList<FeedItem> items;
    private RSS_FeedCallback callback = null;

    public RSS_FeedTask(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public RSS_FeedTask(MediaType mediaType, RSS_FeedCallback callback) {
        this.mediaType = mediaType;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            if (mediaType == MediaType.PICTURE)
                response = API.GET(new OkHttpClient(), RequestBuilder.pictureOfTheDay());
            else
                response = API.GET(new OkHttpClient(), RequestBuilder.mediaOfTheDay());

        } catch (IOException e) {
        }

        if (response == null) return null;

        WikimediaCommonsXmlParser xmlParser = new WikimediaCommonsXmlParser();
        try {
            InputStream stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
            items = xmlParser.parse(stream, mediaType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (XmlPullParserException | IOException e) {
            if (callback != null)
                callback.onError(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (items != null) {
            if (callback != null)
                callback.onFeedReceived(reverse(items));
        }
    }

    //Feed items are parsed at reverse order
    public ArrayList<FeedItem> reverse(ArrayList<FeedItem> list) {
        for (int i = 0, j = list.size() - 1; i < j; i++) {
            list.add(i, list.remove(j));
        }
        return list;
    }
}