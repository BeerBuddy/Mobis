package de.fh_dortmund.beerbuddy_44.requests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.json.JSONObject;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.Person;

public class UpdatePersonRequest extends SpringAndroidSpiceRequest<Person> {


  private final Person person;

  /**
   *
   * @param person - the Person you are going to update
   *
   */
  public UpdatePersonRequest(Person person) {
    super(Person.class);
    this.person = person;
  }

  @Override
  public Person loadDataFromNetwork() throws Exception {
    return getRestTemplate().postForObject("http://localhost:8080/persons", person, Person.class, Collections.EMPTY_MAP);

  }

  public String createCacheKey() {
      return "updatePerson."+person;
  }


}