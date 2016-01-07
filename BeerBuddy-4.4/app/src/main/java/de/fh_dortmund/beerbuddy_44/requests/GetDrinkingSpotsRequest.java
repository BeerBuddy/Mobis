package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetDrinkingSpotsRequest extends SpringAndroidSpiceRequest<DrinkingSpot[]> {


  public GetDrinkingSpotsRequest() {
    super(DrinkingSpot[].class);
  }

  @Override
  public DrinkingSpot[] loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/drinkingspot/getall",  DrinkingSpot[].class);
  }

  public String createCacheKey() {
      return "/drinkingspot/getall" ;
  }


}