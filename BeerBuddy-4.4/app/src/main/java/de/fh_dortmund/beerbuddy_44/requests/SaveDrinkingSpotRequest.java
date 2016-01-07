package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class SaveDrinkingSpotRequest extends SpringAndroidSpiceRequest<DrinkingSpot> {


  private final DrinkingSpot person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public SaveDrinkingSpotRequest(DrinkingSpot person) {
    super(DrinkingSpot.class);
    this.person = person;
  }

  @Override
  public DrinkingSpot loadDataFromNetwork() throws Exception {
    return getRestTemplate().postForObject(ServerUtil.getHost()+"/drinkingspot/save", person, DrinkingSpot.class);
  }

  public String createCacheKey() {
      return "/drinkingspot/save."+person;
  }


}