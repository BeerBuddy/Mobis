package de.fh_dortmund.beerbuddy_44.requests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy.PersonList;

public class GetAllPersonsRequest extends SpringAndroidSpiceRequest<PersonList> {


  private  int size =20;
  private  int page = 0;
  private  String sort = null;

  public GetAllPersonsRequest() {
    super(PersonList.class);
  }

  /**
   *
   * @param page - the page number to access (0 indexed, defaults to 0).
   * @param size - the page size requested (defaults to 20).
   *
   */
  public GetAllPersonsRequest(int page, int size) {
    super(PersonList.class);
    this.size = size;
    this.page = page;
  }

  /**
   *
   * @param page - the page number to access (0 indexed, defaults to 0).
   * @param size - the page size requested (defaults to 20).
   * @param sort - a collection of sort directives in the format ($propertyname,)+[asc|desc]?.
   *
   */
  public GetAllPersonsRequest(int page, int size, String sort) {
    super(PersonList.class);
    this.size = size;
    this.page = page;
    this.sort = sort;
  }

  @Override
  public PersonList loadDataFromNetwork() throws Exception {
    String url = String.format("http://localhost:8080/persons?page=%d,size=%d",page,size);
    if(sort != null)
    {
      url = url + String.format(",sort=%s",sort);
    }


    return getRestTemplate().getForObject(url, PersonList.class);
  }

  /**
   * This method generates a unique cache key for this request. In this case
   * our cache key depends just on the keyword.
   * @return
   */
  public String createCacheKey() {
      return String.format("getAllPersons{?page=%d,size=%d,sort=%s}",page,size,sort);
  }


}