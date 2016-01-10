package de.fh_dortmund.beerbuddy_44.requests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

/**
 * Created by grimm on 07.01.2016.
 */
public class AcceptDrinkingInvitationRequest extends SpringAndroidSpiceRequest<Void> {

    private final DrinkingInvitation person;
    public AcceptDrinkingInvitationRequest(DrinkingInvitation person) {
        super(Void.class);
        this.person = person;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        getRestTemplate().postForObject(ServerUtil.getHost()+"/drinkinginvitation/accept", person, Void.class, Collections.EMPTY_MAP);
        return null;
    }

    public String createCacheKey() {
        return "/drinkinginvitation/accept/"+person.getId();
    }

}
