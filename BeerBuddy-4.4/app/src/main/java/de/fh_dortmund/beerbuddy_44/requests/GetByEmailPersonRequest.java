package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetByEmailPersonRequest extends SpringAndroidSpiceRequest<Person> {


  private final String person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public GetByEmailPersonRequest(String person) {
    super(Person.class);
    this.person = person;
  }

  @Override
  public Person loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/person/getbyemail/"+person, Person.class);
  }

  public String createCacheKey() {
      return "/person/getbyemail/"+person;
  }


}