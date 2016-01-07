package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetAllForDrinkingInvitationRequest extends SpringAndroidSpiceRequest<DrinkingInvitation[]> {


  private final long person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public GetAllForDrinkingInvitationRequest(long person) {
    super(DrinkingInvitation[].class);
    this.person = person;
  }

  @Override
  public DrinkingInvitation[] loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/drinkinginvitation/getallfor/" + person,  DrinkingInvitation[].class);
  }

  public String createCacheKey() {
      return "/drinkinginvitation/getallfor/" + person;
  }


}