package de.fh_dortmund.beerbuddy_44.dao.local;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.LocationDAO;
import de.fh_dortmund.beerbuddy_44.exceptions.MissingPermissionException;

/**
 * Created by David on 26.11.2015.
 */
public class LocationDAOLocal extends LocationDAO implements LocationListener {

    static final int TWO_MINUTES = 1000 * 60 * 2;
    private final LocationManager locationManager;
    private Location currentBestLocation = null;

    public LocationDAOLocal(BeerBuddyActivity context) throws BeerBuddyException{
        super(context);
        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        turnGPSOn(context);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TWO_MINUTES, 10, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TWO_MINUTES, 10, this);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, TWO_MINUTES, 10, this);
        } catch (SecurityException e) {
            throw new MissingPermissionException("No Permission to ACCESS_FINE_LOCATION or  ACCESS_COARSE_LOCATION", e);
        }

    }

    public void addLocationListener(LocationListener l)throws BeerBuddyException
    {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TWO_MINUTES, 10, l);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TWO_MINUTES, 10, l);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, TWO_MINUTES, 10, l);
        } catch (SecurityException e) {
            throw new MissingPermissionException("No Permission to ACCESS_FINE_LOCATION or  ACCESS_COARSE_LOCATION", e);
        }
    }

    private void turnGPSOn(final Context context) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.gps_disabled_message)
                    .setCancelable(false)
                    .setPositiveButton(context.getResources().getText(R.string.mainview_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton(context.getResources().getText(R.string.mainview_no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    @Override
    public Location getCurrentLocation() throws BeerBuddyException {
        Location loc = getLastBestLocation();
        if (!isBetterLocation(loc, currentBestLocation)) {
            loc = currentBestLocation;
        }

        if(loc == null)
        {

        }

        return loc;
    }

    private Location getLastBestLocation() throws BeerBuddyException {
        try {


            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            long GPSLocationTime = 0;
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime();
            }

            long NetLocationTime = 0;

            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }

            if (0 < GPSLocationTime - NetLocationTime) {
                return locationGPS;
            } else {
                return locationNet;
            }
        } catch (SecurityException e) {
            throw new MissingPermissionException("No Permission to ACCESS_FINE_LOCATION or  ACCESS_COARSE_LOCATION", e);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        makeUseOfNewLocation(location);

    }

    /**
     * This method modify the last know good location according to the arguments.
     *
     * @param location The possible new location.
     */
    private void makeUseOfNewLocation(Location location) {
        if (currentBestLocation == null) {
            currentBestLocation = location;
        } else if (isBetterLocation(location, currentBestLocation)) {
            currentBestLocation = location;
        }
    }

    /**
     * Determines whether one location reading is better than the current location fix
     *
     * @param location            The new location that you want to evaluate
     * @param currentBestLocation The current location fix, to which you want to compare the new one.
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location,
        // because the user has likely moved.
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse.
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

}
