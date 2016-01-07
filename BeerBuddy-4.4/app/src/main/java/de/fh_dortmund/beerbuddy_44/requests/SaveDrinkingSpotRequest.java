package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class SaveDrinkingSpotRequest extends SpringAndroidSpiceRequest<Void> {


  private final DrinkingSpot person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public SaveDrinkingSpotRequest(DrinkingSpot person) {
    super(Void.class);
    this.person = person;
  }

  @Override
  public Void loadDataFromNetwork() throws Exception {
    getRestTemplate().postForObject(ServerUtil.getHost()+"/drinkingspot/save", person, Void.class, Collections.EMPTY_MAP);
    return null;
  }

  public String createCacheKey() {
      return "/drinkingspot/save."+person;
  }


}