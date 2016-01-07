package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class SaveFriendInvitationRequest extends SpringAndroidSpiceRequest<FriendInvitation> {


  private final FriendInvitation person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public SaveFriendInvitationRequest(FriendInvitation person) {
    super(FriendInvitation.class);
    this.person = person;
  }

  @Override
  public FriendInvitation loadDataFromNetwork() throws Exception {
    return getRestTemplate().postForObject(ServerUtil.getHost()+"/friendinvitation/save", person, FriendInvitation.class);
  }

  public String createCacheKey() {
      return "saveFriendInvitation."+person;
  }


}