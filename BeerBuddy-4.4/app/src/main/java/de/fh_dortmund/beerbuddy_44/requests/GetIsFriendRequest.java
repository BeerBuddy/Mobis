package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetIsFriendRequest extends SpringAndroidSpiceRequest<Boolean> {


  private final long person1;
  private final long person2;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public GetIsFriendRequest(long person1 , long person2) {
    super(Boolean.class);
    this.person1 = person1;
    this.person2 = person2;
  }

  @Override
  public Boolean loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/friendlist/isFriendFrom/"+person1+"/"+person2,  Boolean.class);
  }

  public String createCacheKey() {
      return "/friendlist/isFriendFrom/"+person1+"/"+person2;
  }


}