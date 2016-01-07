package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetAllFromDrinkingInvitationRequest extends SpringAndroidSpiceRequest<DrinkingInvitation[]> {


  private final long person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public GetAllFromDrinkingInvitationRequest(long person) {
    super(DrinkingInvitation[].class);
    this.person = person;
  }

  @Override
  public DrinkingInvitation[] loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/drinkinginvitation/getallfrom/" + person,  DrinkingInvitation[].class);
  }

  public String createCacheKey() {
      return "/drinkinginvitation/getallfor/" + person;
  }


}