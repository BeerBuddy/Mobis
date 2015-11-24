package de.fh_dortmund.beerbuddy_44;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.CouldNotDownloadImageException;
import de.fh_dortmund.beerbuddy_44.exceptions.CouldNotParseDateException;

/**
 * Created by David on 19.11.2015.
 */
public final class ObjectMapperUtil {

    private ObjectMapperUtil(){
        //prevent Objects
    }

    public static Person toPerson(com.google.android.gms.plus.model.people.Person p) throws BeerBuddyException {
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
            throw new CouldNotDownloadImageException("Could not download Image from url: " + image.getUrl(),e);
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

    private static Date parseDate(String birthday) throws CouldNotParseDateException {
        String formatString = "MMMM d, yyyy";
        try {
            DateFormat format = new SimpleDateFormat(formatString, Locale.ENGLISH);
            Date date = format.parse(birthday);
            return date;
        } catch (ParseException e) {
            throw new CouldNotParseDateException("Birthday could not be parsed: " +birthday +" format: "+ formatString,e);
        }

    }
}
