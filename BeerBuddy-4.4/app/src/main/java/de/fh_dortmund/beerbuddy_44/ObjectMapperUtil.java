package de.fh_dortmund.beerbuddy_44;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.fh_dortmund.beerbuddy_44.exceptions.CouldNotDownloadImageException;
import de.fh_dortmund.beerbuddy_44.exceptions.CouldNotParseDateException;

/**
 * Created by David on 19.11.2015.
 */
public final class ObjectMapperUtil {

    private ObjectMapperUtil() {
        //prevent Objects
    }

 /*   public static Person toPerson(com.google.android.gms.plus.model.people.Person p) throws BeerBuddyException {
        Person returnPerson = new Person();
        returnPerson.setDateOfBirth(parseDate(p.getBirthday()));
        returnPerson.setImage(convertImage(p.getImage()));
        returnPerson.setUsername(p.getDisplayName());
        returnPerson.setGender(p.getGender());
        return returnPerson;
    }

    private static byte[] convertImage(com.google.android.gms.plus.model.people.Person.Image image) throws CouldNotDownloadImageException {
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(image.getUrl());
            baos = new ByteArrayOutputStream();


            is = url.openStream();
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw new CouldNotDownloadImageException("Could not download Image from url: " + image.getUrl(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    //close silent
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    //close silent
                }
            }
        }
    }
*/
    private static Date parseDate(String birthday) throws CouldNotParseDateException {
        String formatString = "MMMM d, yyyy";
        try {
            DateFormat format = new SimpleDateFormat(formatString, Locale.ENGLISH);
            Date date = format.parse(birthday);
            return date;
        } catch (ParseException e) {
            throw new CouldNotParseDateException("Birthday could not be parsed: " + birthday + " format: " + formatString, e);
        }

    }

    public static int getAgeFromBirthday(Date d) {
        long ageInMillis = System.currentTimeMillis() - d.getTime();
        int ageinYears = (int)(ageInMillis / 1000 / 60 / 60 / 24 / 365);
        return ageinYears;
    }

    public static LatLng getLatLangFropmGPS(String gps) {
        String[] split = gps.split(";");
        LatLng sydney = new LatLng(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
        return sydney;
    }

    public static LatLng getLatLngFromLocation(Location l) {
        if(l != null)
        {
            LatLng lng = new LatLng(l.getLatitude(), l.getLongitude());
            return lng;
        }
        return null;
    }

    public static Location getLocationFromLatLang(LatLng l) {
        if(l != null)
        {
            Location loc = new Location("converted");
            loc.setLongitude(l.longitude);
            loc.setLatitude(l.latitude);
            loc.setTime(0l);
            return loc;
        }
        return null;

    }
}
