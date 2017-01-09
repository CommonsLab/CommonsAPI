package apiwrapper.commons.wikimedia.org.Interfaces;

import java.util.ArrayList;

import apiwrapper.commons.wikimedia.org.Models.FeedItem;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public interface RSS_FeedCallback {
    void onFeedReceived(ArrayList<FeedItem> feedItems);

    void onError(Exception error);
}
