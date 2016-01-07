package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class JoinDrinkingSpotRequest extends SpringAndroidSpiceRequest<Void> {


  private final long person1;
  private final long person2;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public JoinDrinkingSpotRequest(long person1 , long person2) {
    super(Void.class);
    this.person1 = person1;
    this.person2 = person2;
  }

  @Override
  public Void loadDataFromNetwork() throws Exception {
     getRestTemplate().postForObject(ServerUtil.getHost()+"drinkingspot/join/"+person1+"/"+person2, "", Void.class, Collections.EMPTY_MAP);
    return null;
  }

  public String createCacheKey() {
      return "drinkingspot/join/"+person1+"/"+person2;
  }


}