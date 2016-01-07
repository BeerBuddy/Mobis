package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetFriendListRequest extends SpringAndroidSpiceRequest<FriendList> {


  private final long person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public GetFriendListRequest(long person) {
    super(FriendList.class);
    this.person = person;
  }

  //@Override
  public FriendList loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/friendlist/get/"+person,  FriendList.class);
  }

  public String createCacheKey() {
      return "/friendlist/get/"+person;
  }


}