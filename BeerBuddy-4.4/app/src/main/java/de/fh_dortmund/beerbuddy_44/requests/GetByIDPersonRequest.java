package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetByIDPersonRequest extends SpringAndroidSpiceRequest<Person> {


  private final long person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public GetByIDPersonRequest(long person) {
    super(Person.class);
    this.person = person;
  }

  @Override
  public Person loadDataFromNetwork() throws Exception {
    return getRestTemplate().getForObject(ServerUtil.getHost() + "/person/get/"+person, Person.class);
  }

  public String createCacheKey() {
      return "/person/get/"+person;
  }


}