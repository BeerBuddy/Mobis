package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class SaveFriendListRequest extends SpringAndroidSpiceRequest<FriendList> {


  private final FriendList person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public SaveFriendListRequest(FriendList person) {
    super(FriendList.class);
    this.person = person;
  }

  @Override
  public FriendList loadDataFromNetwork() throws Exception {
     getRestTemplate().postForObject(ServerUtil.getHost()+"/friendlist/save", person, FriendList.class, Collections.EMPTY_MAP);
    return null;
  }

  public String createCacheKey() {
      return "/friendlist/save"+ person;
  }


}