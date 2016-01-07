package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class SaveFriendListRequest extends SpringAndroidSpiceRequest<Void> {


  private final FriendList person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public SaveFriendListRequest(FriendList person) {
    super(Void.class);
    this.person = person;
  }

  @Override
  public Void loadDataFromNetwork() throws Exception {
     getRestTemplate().postForObject(ServerUtil.getHost()+"/friendlist/save", person, Void.class, Collections.EMPTY_MAP);
    return null;
  }

  public String createCacheKey() {
      return "/friendlist/save"+ person;
  }


}