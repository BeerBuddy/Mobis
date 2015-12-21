package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;
import android.location.Location;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.fh_dortmund.beerbuddy.DrinkingSpot;
import de.fh_dortmund.beerbuddy.FriendList;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.DatabaseHelper;
import de.fh_dortmund.beerbuddy_44.dao.util.MockUtil;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 19.11.2015.
 */
class DrinkingSpotDAOMock extends DrinkingSpotDAO {

    private String[] descriptions = new String[]{
            "Im drinking here with my friends come and join",
            "Only alcoholics drink alone. My friends say im one, proof them wrong",
            "Beer and cardgames join fast",
            "Join me im lonely",
            "There are only two real ways to get ahead today - sell liquor or drink it.",
            "First you take a drink, then the drink takes a drink, then the drink takes you",
            "I feel sorry for people who don't drink. When they wake up in the morning, that's as good as they're going to feel all day."
    };

    List<DrinkingSpot> spots = new ArrayList<DrinkingSpot>();

    public DrinkingSpotDAOMock(Context context) {
        super(context);

    }


    @Override
    public List<DrinkingSpot> getAll(Location l) throws BeerBuddyException {
        if (spots.isEmpty()) {
            for (int i = 0; i < 20; i++) {
                spots.add(createRandomDrinkingSpot(l));
            }
        }
        return spots;

    }

    private DrinkingSpot createRandomDrinkingSpot(Location l) {

        DrinkingSpot sp = new DrinkingSpot();
        sp.setId((long) (Math.random() * Long.MAX_VALUE));
        sp.setAgeFrom((int) (Math.random() * 20) + 16);
        sp.setAgeTo(sp.getAgeFrom() + (int) (Math.random() * 30));
        sp.setBeschreibung(descriptions[(int) Math.random() * descriptions.length]);
        sp.setGps(getLocationRandom(l, Math.random() * 10));
        sp.setCreator(MockUtil.createRandomPerson(sp.getId()));
        sp.setPersons(createRandomPersons((int) sp.getId()));
        sp.setStartTime(new Date());
        return sp;
    }

    @Override
    public DrinkingSpot getActiveByPersonId(long currentPersonId) throws BeerBuddyException {
        for (DrinkingSpot ds : spots) {
            if (ds.getCreator().getId() == currentPersonId)
                return ds;
        }

        DrinkingSpot ds = createRandomDrinkingSpot(DAOFactory.getLocationDAO(context).getCurrentLocation());
        ds.getCreator().setId(currentPersonId);
        spots.add(ds);
        return ds;
    }

    @Override
    public void insertOrUpdate(DrinkingSpot drinkingSpot) throws BeerBuddyException {
        spots.add(drinkingSpot);
    }

    @Override
    public DrinkingSpot getById(long dsid) throws BeerBuddyException {
        for (DrinkingSpot ds : spots) {
            if (ds.getId() == dsid)
                return ds;
        }
    DrinkingSpot ds = createRandomDrinkingSpot(DAOFactory.getLocationDAO(context).getCurrentLocation());
    ds.setId(dsid);
    spots.add(ds);
    return ds;
    }

    @Override
    public void join(long dsid, long currentPersonId) throws BeerBuddyException {
        for (DrinkingSpot ds : spots) {
            if (ds.getId() == dsid) {
                ds.getPersons().add(DAOFactory.getPersonDAO(context).getById(currentPersonId));
                return;
            }
        }

        throw new DataAccessException("DrinkingSpot with the id: " + dsid + " could not be found.");
    }


    private List<Person> createRandomPersons(int spid) {
        List<Person> list = MockUtil.createRandomPersons((int) (Math.random() * 10) + 1);
        for (Person p : list) {
            p.setId((long) (Math.random() * 10));
        }
        return list;
    }

    private String getLocationRandom(Location location, double radius) {
        if (location != null && radius != 0) {
            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(location.getLatitude());

            double foundLongitude = new_x + location.getLongitude();
            double foundLatitude = y + location.getLatitude();
            return foundLatitude + ";" + foundLongitude;
        } else {
            return "51.509981800747084;7.453107833862305";
        }

    }


}
