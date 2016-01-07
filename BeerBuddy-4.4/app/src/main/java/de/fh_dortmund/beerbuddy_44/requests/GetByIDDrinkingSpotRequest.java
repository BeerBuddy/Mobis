package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetByIDDrinkingSpotRequest extends SpringAndroidSpiceRequest<DrinkingSpot> {


  private final long id;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public GetByIDDrinkingSpotRequest(long id) {
    super(DrinkingSpot.class);
    this.id = id;
  }

  @Override
  public DrinkingSpot loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/drinkingspot/get/"+id, DrinkingSpot.class);
  }

  public String createCacheKey() {
      return "/drinkingspot/get/"+id;
  }


}