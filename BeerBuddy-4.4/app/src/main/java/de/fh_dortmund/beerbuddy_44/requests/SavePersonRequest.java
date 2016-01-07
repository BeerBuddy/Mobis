package de.fh_dortmund.beerbuddy_44.requests;


import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.codehaus.jackson.map.ObjectMapper;

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
    return getRestTemplate().postForObject(ServerUtil.getHost() + "/person/save", new ObjectMapper().writeValueAsString(person), Person.class);
  }

  public String createCacheKey() {
      return "/person/save"+person.getId();
  }


}