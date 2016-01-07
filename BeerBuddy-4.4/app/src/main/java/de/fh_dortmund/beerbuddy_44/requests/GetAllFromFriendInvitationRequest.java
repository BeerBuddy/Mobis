package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetAllFromFriendInvitationRequest extends SpringAndroidSpiceRequest<FriendInvitation[]> {


  private final long person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public GetAllFromFriendInvitationRequest(long person) {
    super(FriendInvitation[].class);
    this.person = person;
  }

  @Override
  public FriendInvitation[] loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/friendinvitation/getallfrom/" + person, FriendInvitation[].class);
  }

  public String createCacheKey() {
      return "friendinvitation/getallfrom/"+person;
  }


}