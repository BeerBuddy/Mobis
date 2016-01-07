package de.fh_dortmund.beerbuddy_44.requests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.ServerUtil;

public class GetAllPersonsRequest  extends SpringAndroidSpiceRequest<Person[]> {

  public GetAllPersonsRequest() {
    super(Person[].class);
  }

  @Override
  public Person[] loadDataFromNetwork() throws Exception {
    String url = String.format( ServerUtil.getHost()+"/person/all");
       return getRestTemplate().getForObject(url, Person[].class);
  }

  /**
   * This method generates a unique cache key for this request. In this case
   * our cache key depends just on the keyword.
   * @return
   */
  public String createCacheKey() {
      return "getAllPersons";
  }


}