package de.fh_dortmund.beerbuddy_44.listener.rest;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.PersonList;

//inner class of your spiced Activity
public abstract class ListPersonRequestListener implements RequestListener<PersonList> {

  @Override
  public abstract void onRequestFailure(SpiceException e);

  @Override
  public abstract  void onRequestSuccess(PersonList listFollowers) ;
}