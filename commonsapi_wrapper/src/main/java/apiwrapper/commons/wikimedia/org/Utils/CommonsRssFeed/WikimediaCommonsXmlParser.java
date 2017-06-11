package apiwrapper.commons.wikimedia.org.Utils.CommonsRssFeed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import apiwrapper.commons.wikimedia.org.Models.FeedItem;
import apiwrapper.commons.wikimedia.org.Enums.MediaType;

/**
 * Created by Valdio Veliu on 05/01/2017.
 * <p/>
 * This class parses XML feeds from commons.wikimedia.org/
 * for both RSS feeds:
 * 1. Picture of the day
 * 2. Media of the day
 */


public class WikimediaCommonsXmlParser {
    private static final String namespace = null;
    private static final String RSS = "rss";
    private static final String CHANNEL = "channel";
    private static final String ITEM = "item";

    //HTML tags
    private static final String DIV = "div";
    private static final String TABLE = "table";
    private static final String TABLE_ROW = "tr";
    private static final String TABLE_DATA = "td";
    private static final String HYPERLINK = "a";
    private static final String HYPERLINK_REFERENCE = "href";
    private static final String IMG = "img";
    private static final String ALT = "alt";
    private static final String SRC = "src";

    //Item values
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String GUID = "guid";
    private static final String DESCRIPTION = "description";
    private static final String PUBLICATION_DATE = "pubDate";

    private MediaType mediaType;

    public ArrayList<FeedItem> parse(InputStream stream, MediaType mediaType) throws XmlPullParserException, IOException {
        this.mediaType = mediaType;

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
        return parseRSS(parser);
    }

    //Parse <rss></rss> tags
    private ArrayList<FeedItem> parseRSS(XmlPullParser parser) throws XmlPullParserException, IOException {
        //the list of rss Item objects
        ArrayList<FeedItem> items = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, namespace, RSS);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String parserName = parser.getName();
            if (parserName.equals(CHANNEL)) {
                items.addAll(parseChannel(parser));
            } else {
                skip(parser);
            }

        }
        return items;
    }

    //Parse <channel></channel> tags
    private ArrayList<FeedItem> parseChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<FeedItem> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, CHANNEL);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String parserName = parser.getName();
            if (parserName.equals(ITEM)) {
                items.add(parseItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    // Parses the contents of an Item.
    private FeedItem parseItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String guid = null;
        String pubdate = null;
        Description descriptionObject = null;

        parser.require(XmlPullParser.START_TAG, namespace, ITEM);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String parserName = parser.getName();
            switch (parserName) {
                case TITLE:
                    title = parseTag(parser, TITLE);
                    break;
                case LINK:
                    link = parseTag(parser, LINK);
                    break;
                case GUID:
                    guid = parseTag(parser, GUID);
                    break;
                case DESCRIPTION:
                    descriptionObject = parseDescription(parser);
                    break;
                case PUBLICATION_DATE:
                    pubdate = parseTag(parser, PUBLICATION_DATE);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        if (descriptionObject != null)
            return new FeedItem(title, link, guid, pubdate, descriptionObject.getFileName(), descriptionObject.getMediaLink(), descriptionObject.getStreamingURL());
        else return null;
    }

    // For the tags:
    // <title></title
    // <link></link>
    // <guid></guid>
    // <pubDate></pubDate>
    // extracts their text values.
    private String parseTag(XmlPullParser parser, String TAG) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, TAG);
        String tagValue = parseText(parser);
        parser.require(XmlPullParser.END_TAG, namespace, TAG);
        return tagValue;
    }

    // Processes <description></description> tags in the feed.
    private Description parseDescription(XmlPullParser initialParser) throws IOException, XmlPullParserException {

        //get the description data from the initial XML parser
        //Convert description to stream
        String description = parseTag(initialParser, DESCRIPTION);
        InputStream stream = new ByteArrayInputStream(description.getBytes("UTF-8"));

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
        //Parse description entry:
        return parseDIV(parser);
    }

    private Description parseDIV(XmlPullParser parser) throws IOException, XmlPullParserException {
        /**
         * Processes <div></div> tag
         */
        Description descriptionObject = null;
        parser.require(XmlPullParser.START_TAG, null, DIV);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String parserName = parser.getName();
            if (parserName.equals(DIV)) {
                descriptionObject = parseDIV(parser);
            } else if (parserName.equals(TABLE)) {
                descriptionObject = parseTABLE(parser);
            } else {
                skip(parser);
            }
        }
        return descriptionObject;
    }

    private Description parseTABLE(XmlPullParser parser) throws IOException, XmlPullParserException {
        /**
         *  Processes <table></table> tag
         */
        Description descriptionObject = null;
        parser.require(XmlPullParser.START_TAG, null, TABLE);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String parserName = parser.getName();
            if (parserName.equals(TABLE_ROW)) {
                descriptionObject = parseTableROW(parser);
            } else {
                skip(parser);
            }
        }
        return descriptionObject;
    }

    private Description parseTableROW(XmlPullParser parser) throws IOException, XmlPullParserException {
        /**
         *  Processes <tr></tr> tag
         */
        Description descriptionObject = null;
        parser.require(XmlPullParser.START_TAG, null, TABLE_ROW);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String parserName = parser.getName();
            if (parserName.equals(TABLE_DATA)) {
                descriptionObject = parseTableDATA(parser);

                //Interested only in one table data elements
                //Breat while() loop
                break;
            } else {
                skip(parser);
            }
        }
        return descriptionObject;
    }

    private Description parseTableDATA(XmlPullParser parser) throws IOException, XmlPullParserException {
        /**
         *  Processes <td></td> tag
         */
        Description descriptionObject = null;
        parser.require(XmlPullParser.START_TAG, null, TABLE_DATA);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String parserName = parser.getName();

            if (mediaType == MediaType.PICTURE) {
                //Picture of the day
                if (parserName.equals(HYPERLINK)) {
                    descriptionObject = parseHYPERLINK(parser);
                } else {
                    skip(parser);
                }

            } else if (mediaType == MediaType.MEDIA) {
                //Media of the day
                if (parserName.equals(DIV)) {
                    descriptionObject = parseMediaDIV(parser);
                } else {
                    skip(parser);
                }
            }

        }
        return descriptionObject;
    }

    //Media of the day <div></div> parser
    private Description parseMediaDIV(XmlPullParser parser) throws IOException, XmlPullParserException {
        /**
         *  Processes <div></div> tag
         */

        Description descriptionObject = new Description();
        parser.require(XmlPullParser.START_TAG, null, DIV);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String parserName = parser.getName();
            switch (parserName) {
                case IMG:
                    //This object contains the mediaName and media's thumbnail link
                    Description obj = parseIMG(parser);
                    descriptionObject.setFileName(obj.getFileName());
                    descriptionObject.setMediaLink(obj.getMediaLink());
                    break;
                case HYPERLINK:
                    //Returns the medias URL
                    //video  or audio streaming url
                    descriptionObject.setStreamingURL(parseStreamingURL(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return descriptionObject;

    }

    private String parseStreamingURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        /**
         *  Processes <a></a> tag
         *
         */
        String streamingURL = null;
        parser.require(XmlPullParser.START_TAG, null, HYPERLINK);
        String name = parser.getName();
        if (name.equals(HYPERLINK)) {
            streamingURL = parser.getAttributeValue(null, HYPERLINK_REFERENCE);
            parser.nextTag();
        }
        return streamingURL;

    }

    private Description parseHYPERLINK(XmlPullParser parser) throws IOException, XmlPullParserException {
        /**
         *  Processes <a></a> tag
         *
         */
        Description descriptionObject = null;
        parser.require(XmlPullParser.START_TAG, null, HYPERLINK);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String parserName = parser.getName();
            if (parserName.equals(IMG)) {
                descriptionObject = parseIMG(parser);
            } else {
                skip(parser);
            }
        }
        return descriptionObject;
    }

    private Description parseIMG(XmlPullParser parser) throws IOException, XmlPullParserException {
        /**
         *  Processes <img></img> tag
         *  Extract the data
         */
        Description descriptionObject = null;
        parser.require(XmlPullParser.START_TAG, null, IMG);
        String tag = parser.getName();
        if (tag.equals(IMG)) {
            descriptionObject = new Description();
            descriptionObject.setFileName(parser.getAttributeValue(null, ALT));
            descriptionObject.setMediaLink(parser.getAttributeValue(null, SRC));
            parser.nextTag();
        }

        return descriptionObject;
    }

    // Extracts the text value of the XmlPullParser's tag
    private String parseText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private static class Description {

        private String streamingURL;
        private String fileName;
        private String mediaLink;

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
}
