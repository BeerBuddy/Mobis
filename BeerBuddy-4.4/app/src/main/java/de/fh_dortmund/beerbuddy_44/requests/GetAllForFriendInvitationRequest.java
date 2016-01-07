package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetAllForFriendInvitationRequest extends SpringAndroidSpiceRequest<FriendInvitation[]> {


  private final long person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public GetAllForFriendInvitationRequest(long person) {
    super(FriendInvitation[].class);
    this.person = person;
  }

  @Override
  public FriendInvitation[] loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/friendinvitation/getallfor/" + person, FriendInvitation[].class);
  }

  public String createCacheKey() {
      return "friendinvitation/getallfor/"+person;
  }


}