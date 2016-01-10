package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class SaveDrinkingSpotRequest extends SpringAndroidSpiceRequest<DrinkingSpot> {


  private final DrinkingSpot drinkingSpot;

  /**
   *
   * @param drinkingSpot - the drinkingspot you are going to update
   *
   */
  public SaveDrinkingSpotRequest(DrinkingSpot drinkingSpot) {
    super(DrinkingSpot.class);
    this.drinkingSpot = drinkingSpot;
  }

  @Override
  public DrinkingSpot loadDataFromNetwork() throws Exception {
    return getRestTemplate().postForObject(ServerUtil.getHost() + "/drinkingspot/save", drinkingSpot, DrinkingSpot.class);
  }

  public String createCacheKey() {
    return "/drinkingspot/save/" + drinkingSpot.getId();
  }


}