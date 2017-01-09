package apiwrapper.commons.wikimedia.org.Interfaces;

import apiwrapper.commons.wikimedia.org.Models.Captcha;

/**
 * Created by Valdio Veliu on 06/01/2017.
 */

public interface CaptchaCallback {

    void onCaptchaReceived(Captcha captcha);

    void onFailure();
}
