package apiwrapper.commons.wikimedia.org.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Valdio Veliu on 16-11-28.
 */

public class User {

    private String username;
    private String password;
    private String userID;
    private String lgtoken;
    private String cookieprefix;
    private String sessionid;
    private String editToken;

    public User() {
    }

    public User(JSONObject user) {
        try {
            userID = user.getString("lguserid");
            username = user.getString("lgusername");
            lgtoken = user.getString("lgtoken");
        } catch (JSONException e) {
        }
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getLgtoken() {
        return lgtoken;
    }

    public void setLgtoken(String lgtoken) {
        this.lgtoken = lgtoken;
    }

    public String getCookieprefix() {
        return cookieprefix;
    }

    public void setCookieprefix(String cookieprefix) {
        this.cookieprefix = cookieprefix;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEditToken() {
        return editToken;
    }

    public void setEditToken(String editToken) {
        this.editToken = editToken;
    }
}