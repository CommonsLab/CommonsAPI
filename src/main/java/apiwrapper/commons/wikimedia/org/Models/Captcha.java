package apiwrapper.commons.wikimedia.org.Models;

/**
 * Created by Valdio Veliu on 06/01/2017.
 */

public class Captcha {
    private String captchaURL;
    private String captchaId;

    public String getCaptchaURL() {
        return captchaURL;
    }

    public void setCaptchaURL(String captchaURL) {
        this.captchaURL = captchaURL;
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }
}
