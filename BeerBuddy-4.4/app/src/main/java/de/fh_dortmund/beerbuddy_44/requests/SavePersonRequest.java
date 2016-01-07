package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class SavePersonRequest extends SpringAndroidSpiceRequest<Person> {


  private final Person person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public SavePersonRequest(Person person) {
    super(Person.class);
    this.person = person;
  }

  @Override
  public Person loadDataFromNetwork() throws Exception {
     getRestTemplate().postForObject(ServerUtil.getHost()+"/person/save", person, Person.class, Collections.EMPTY_MAP);
    return null;
  }

  public String createCacheKey() {
      return "/person/save"+person;
  }


}