package de.fh_dortmund.beerbuddy_44.requests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy.PersonList;

public class InsertPersonRequest extends SpringAndroidSpiceRequest<Void> {


  private final Person person;

  /**
   *
   * @param person - the Person you are going to insert
   *
   */
  public InsertPersonRequest(Person person) {
    super(Void.class);
    this.person = person;
  }

  @Override
  public Void loadDataFromNetwork() throws Exception {
    getRestTemplate().put("http://localhost:8080/persons", person);
      return null;

  }

  public String createCacheKey() {
      return "insertPerson."+person;
  }


}