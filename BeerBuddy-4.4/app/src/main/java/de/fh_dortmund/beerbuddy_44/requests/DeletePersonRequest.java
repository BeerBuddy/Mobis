package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class DeletePersonRequest extends SpringAndroidSpiceRequest<Void> {


  private final Person person;

  /**
   *
   * @param person - the Person you are going to delete
   *
   */
  public DeletePersonRequest(Person person) {
    super(Void.class);
    this.person = person;
  }

  @Override
  public Void loadDataFromNetwork() throws Exception {
     getRestTemplate().getForObject(ServerUtil.getHost()+"/person/remove/"+person.getId(), Void.class);
    return null;
  }

  public String createCacheKey() {
      return "/person/remove/"+person.getId();
  }


}