package de.fh_dortmund.beerbuddy_44.requests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Collections;

import de.fh_dortmund.beerbuddy.Person;

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
     getRestTemplate().delete("http://localhost:8080/persons", person);
    return null;
  }

  public String createCacheKey() {
      return "deletePerson."+person;
  }


}