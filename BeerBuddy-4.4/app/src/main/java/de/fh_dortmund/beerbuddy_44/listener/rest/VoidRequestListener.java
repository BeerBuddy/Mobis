package de.fh_dortmund.beerbuddy_44.listener.rest;


import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

//inner class of your spiced Activity
public abstract class VoidRequestListener implements RequestListener<Void> {

  @Override
  public abstract void onRequestFailure(SpiceException e);

  @Override
  public void onRequestSuccess(Void nothing) {

  };
}