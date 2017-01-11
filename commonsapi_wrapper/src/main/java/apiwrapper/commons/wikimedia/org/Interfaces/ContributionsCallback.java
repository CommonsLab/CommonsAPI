package apiwrapper.commons.wikimedia.org.Interfaces;

import java.util.ArrayList;

import apiwrapper.commons.wikimedia.org.Models.Contribution;

/**
 * Created by Valdio Veliu on 05/01/2017.
 */

public interface ContributionsCallback {
    /**
     *
     * @param contributions list of {@link Contribution} objects
     */
    void onContributionsReceived(ArrayList<Contribution> contributions);

    void onFailure();
}
