package apiwrapper.commons.wikimedia.org.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import apiwrapper.commons.wikimedia.org.Models.Captcha;
import apiwrapper.commons.wikimedia.org.Models.Contribution;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public class JsonParser {

    //Parse the Users Contributions String to a JSON array
    public static ArrayList<Contribution> parseUsersContributions(String contributions) {

        ArrayList<Contribution> dataArrayList = new ArrayList<>();
        //get the DATA specifications in a JSONArray
        try {
            JSONObject jsonObject = new JSONObject(contributions);
            JSONObject queryResult = jsonObject.getJSONObject("query");
            JSONArray AllImages = queryResult.getJSONArray("allimages");

            for (int i = 0; i < AllImages.length(); i++) {
                Contribution data = new Contribution();
                data.setUrl(AllImages.getJSONObject(i).getString("url"));
                data.setDescriptionurl(AllImages.getJSONObject(i).getString("descriptionurl"));
                data.setMediatype(AllImages.getJSONObject(i).getString("mediatype"));
                data.setNs(AllImages.getJSONObject(i).getString("ns"));
                data.setTitle(AllImages.getJSONObject(i).getString("title"));
                dataArrayList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataArrayList;
    }

    //Parse the JSON response to extract the Captcha ID and URL
    public static Captcha parseCaptchaResponse(String captchaResponse) throws JSONException {
        Captcha captcha = new Captcha();

        Log.w("captcha", captchaResponse);
        String COMMONS_ENDPOINT = "https://commons.wikimedia.org";

        JSONObject responseJSON = new JSONObject(captchaResponse);
        JSONObject queryResult = responseJSON.getJSONObject("query");
        JSONObject authmanagerinfo = queryResult.getJSONObject("authmanagerinfo");
        JSONArray requests = authmanagerinfo.getJSONArray("requests");
        JSONObject captchaInfo = requests.getJSONObject(0);

        //Extract the values
        String ID = captchaInfo.getJSONObject("fields").getJSONObject("captchaId").getString("value");
        String URL = COMMONS_ENDPOINT + captchaInfo.getJSONObject("fields").getJSONObject("captchaInfo").getString("value");

        if (ID == null || URL == null || ID == "" || URL == "")
            return null;
        captcha.setCaptchaId(ID);
        captcha.setCaptchaURL(URL);
        return captcha;
    }
}