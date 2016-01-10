package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class SaveDrinkingInvitationRequest extends SpringAndroidSpiceRequest<DrinkingInvitation> {


  private final DrinkingInvitation person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public SaveDrinkingInvitationRequest(DrinkingInvitation person) {
    super(DrinkingInvitation.class);
    this.person = person;
  }

  @Override
  public DrinkingInvitation loadDataFromNetwork() throws Exception {
    return getRestTemplate().postForObject(ServerUtil.getHost()+"/drinkinginvitation/save", person, DrinkingInvitation.class);
  }

  public String createCacheKey() {
      return "/drinkinginvitation/save/"+person.getId();
  }


}