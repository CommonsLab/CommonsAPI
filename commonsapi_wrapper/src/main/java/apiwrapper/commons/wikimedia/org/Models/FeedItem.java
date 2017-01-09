package apiwrapper.commons.wikimedia.org.Models;

import java.io.Serializable;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */
public class FeedItem implements Serializable {

    private String title;
    private String RSSlink;
    private String guid;
    private String pubdate;
    private String fileName;
    private String mediaLink;

    //only for media of the day. Crashes if you try to access for images.
    private String streamingURL;

    public FeedItem(String title, String RSSlink, String guid, String pubdate, String fileName, String mediaLink, String streamingURL) {
        this.title = title;
        this.RSSlink = RSSlink;
        this.guid = guid;
        this.pubdate = pubdate;
        this.fileName = fileName;
        this.mediaLink = mediaLink;
        this.streamingURL = streamingURL;
    }

    public FeedItem() {
        setTitle(null);
        setRSSlink(null);
        setGuid(null);
        setPubdate(null);
        setFileName(null);
        setMediaLink(null);
        setStreamingURL(null);

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }


    public String getRSSlink() {
        return RSSlink;
    }

    public void setRSSlink(String RSSlink) {
        this.RSSlink = RSSlink;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public String getStreamingURL() {
        return streamingURL;
    }

    public void setStreamingURL(String streamingURL) {
        this.streamingURL = streamingURL;
    }
}

