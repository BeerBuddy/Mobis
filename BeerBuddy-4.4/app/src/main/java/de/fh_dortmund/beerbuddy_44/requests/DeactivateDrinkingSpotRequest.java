package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class DeactivateDrinkingSpotRequest extends SpringAndroidSpiceRequest<Void> {


  private final long id;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public DeactivateDrinkingSpotRequest(long id) {
    super(Void.class);
    this.id = id;
  }

  @Override
  public Void loadDataFromNetwork() throws Exception {
    getRestTemplate().getForObject(ServerUtil.getHost() + "/drinkingspot/deactivate/" + id, Void.class);
    return null;
  }

  public String createCacheKey() {
      return "/drinkingspot/deactivate/"+id;
  }


}