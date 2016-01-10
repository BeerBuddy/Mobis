package de.fh_dortmund.beerbuddy_44.dao.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 19.11.2015.
 */
public class DrinkingSpotDAOLocal extends DrinkingSpotDAO {

    BeerBuddyDbHelper dbHelper;
    DrinkingSpotPersonDAO drinkingSpotPersonDAO;
    PersonDAOLocal personDAOLocal;

    private final String SELECT = "SELECT" +
            " ds.id AS dsid, ds.version AS dsversion, ds.beschreibung AS dsbeschreibung, ds.startTime AS dsstartTime, ds.ageFrom AS dsageFrom, ds.ageTo AS dsageTo, ds.gps AS dsgps, ds.amountMaleWithoutBeerBuddy AS dsamountMaleWithoutBeerBuddy, ds.amountFemaleWithoutBeerBuddy AS dsamountFemaleWithoutBeerBuddy," +
            " p.id AS pid, p.version AS  pversion, p.email AS pemail,p.username AS pusername,p.image AS pimage ,p.password AS ppassword,p.gender AS pgender,p.dateOfBirth AS pdateOfBirth,p.interests AS pinterests ,p.prefers AS pprefers," +
            " creator.id AS creatorid, creator.email AS creatoremail ,creator.username AS creatorusername ,creator.image AS creatorimage ,creator.password AS creatorpassword ,creator.gender AS creatorgender ,creator.dateOfBirth AS creatordateOfBirth,creator.version AS creatorversion,creator.interests AS creatorinterests,creator.prefers AS creatorprefers" +
            " FROM drinkingspot AS ds LEFT JOIN person AS creator ON(ds.creatorId = creator.id) LEFT JOIN drinkingspotperson AS dsp ON (ds.id = dsp.drinkingSpotId) LEFT JOIN person AS p ON(dsp.personid = p.id) ";

    public DrinkingSpotDAOLocal(BeerBuddyActivity context) {
        super(context);
        dbHelper = BeerBuddyDbHelper.getInstance(context);
        drinkingSpotPersonDAO = new DrinkingSpotPersonDAO(context);
        personDAOLocal = new PersonDAOLocal(context);
    }

    @Override
    public void getAll(RequestListener<DrinkingSpot[]> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.rawQuery(SELECT+";", null);
            Collection<DrinkingSpot> drinkingSpot = getDrinkingSpot(dbCursor);
            if (!drinkingSpot.isEmpty()) {
                listener.onRequestSuccess(drinkingSpot.toArray(new DrinkingSpot[]{}));
            } else {
                listener.onRequestSuccess(null);
            }
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        } finally

        {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }

    }

    private Collection<DrinkingSpot> getDrinkingSpot(Cursor dbCursor) throws DataAccessException, ParseException {
        Map<Long, DrinkingSpot> map = new HashMap<Long, DrinkingSpot>();
        while (dbCursor.moveToNext()) {
            long dsid = dbCursor.getLong(dbCursor.getColumnIndex("ds.id"));
            if (map.get(dsid) == null) {
                DrinkingSpot di = new DrinkingSpot();
                di.setId(dsid);
                di.getPersons().add(getPerson(dbCursor));
                di.setCreator(getCreator(dbCursor));
                di.setBeschreibung(dbCursor.getString(dbCursor.getColumnIndex(" dsbeschreibung")));
                String string = dbCursor.getString(dbCursor.getColumnIndex("dsstartTime"));
                if (string != null)
                    di.setStartTime(BeerBuddyDbHelper.DATE_FORMAT.parse(string));
                di.setAgeFrom(dbCursor.getInt(dbCursor.getColumnIndex("dsageFrom")));
                di.setAgeTo(dbCursor.getInt(dbCursor.getColumnIndex("dsageTo")));
                di.setGps(dbCursor.getString(dbCursor.getColumnIndex(" dsgps")));
                di.setVersion(dbCursor.getLong(dbCursor.getColumnIndex(" dsversion")));
                di.setAmountMaleWithoutBeerBuddy(dbCursor.getInt(dbCursor.getColumnIndex("dsamountMaleWithoutBeerBuddy")));
                di.setAmountFemaleWithoutBeerBuddy(dbCursor.getInt(dbCursor.getColumnIndex("dsamountFemaleWithoutBeerBuddy")));
                di.setActive(dbCursor.getInt(dbCursor.getColumnIndex("dsactive")) == BeerBuddyDbHelper.BOOLEAN_TRUE);
                map.put(dsid, di);
            } else {
                map.get(dsid).getPersons().add(getPerson(dbCursor));
            }
        }
        return map.values();
    }

    private Person getCreator(Cursor dbCursor) throws ParseException {
        Person p = new Person();
        p.setId(dbCursor.getInt(dbCursor.getColumnIndex("creatorid")));
        p.setUsername(dbCursor.getString(dbCursor.getColumnIndex("creatorusername")));
        p.setEmail(dbCursor.getString(dbCursor.getColumnIndex("creatoremail")));
        p.setPassword(dbCursor.getString(dbCursor.getColumnIndex("creatorpassword")));
        String date = dbCursor.getString(dbCursor.getColumnIndex("creatordateOfBirth"));
        if (date != null)
            p.setDateOfBirth(BeerBuddyDbHelper.DATE_FORMAT.parse(date));
        p.setGender(dbCursor.getInt(dbCursor.getColumnIndex("creatorgender")));
        p.setImage(dbCursor.getBlob(dbCursor.getColumnIndex("creatorimage")));
        p.setInterests(dbCursor.getString(dbCursor.getColumnIndex("creatorinterests")));
        p.setPrefers(dbCursor.getString(dbCursor.getColumnIndex("creatorprefers")));
        p.setVersion(dbCursor.getInt(dbCursor.getColumnIndex("creatorversion")));
        return p;
    }

    private Person getPerson(Cursor dbCursor) throws ParseException {
        Person p = new Person();
        p.setId(dbCursor.getInt(dbCursor.getColumnIndex("pid")));
        p.setUsername(dbCursor.getString(dbCursor.getColumnIndex("pusername")));
        p.setEmail(dbCursor.getString(dbCursor.getColumnIndex("pemail")));
        p.setPassword(dbCursor.getString(dbCursor.getColumnIndex("ppassword")));
        String date = dbCursor.getString(dbCursor.getColumnIndex("pdateOfBirth"));
        if (date != null)
            p.setDateOfBirth(BeerBuddyDbHelper.DATE_FORMAT.parse(date));
        p.setGender(dbCursor.getInt(dbCursor.getColumnIndex("pgender")));
        p.setImage(dbCursor.getBlob(dbCursor.getColumnIndex("pimage")));
        p.setInterests(dbCursor.getString(dbCursor.getColumnIndex("pinterests")));
        p.setPrefers(dbCursor.getString(dbCursor.getColumnIndex("pprefers")));
        p.setVersion(dbCursor.getInt(dbCursor.getColumnIndex("pversion")));
        return p;
    }


    @Override
    public void getActiveByPersonId(long personId, RequestListener<DrinkingSpot> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.rawQuery(SELECT + " WHERE dscreatorId = ? and dsactive = ?;", new String[]{personId + "", BeerBuddyDbHelper.BOOLEAN_TRUE + ""});
            Collection<DrinkingSpot> drinkingSpot = getDrinkingSpot(dbCursor);
            if (!drinkingSpot.isEmpty()) {
                listener.onRequestSuccess(drinkingSpot.toArray(new DrinkingSpot[]{})[0]);
            } else {
                listener.onRequestSuccess(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }


    @Override
    public void insertOrUpdate(final DrinkingSpot drinkingSpot, final RequestListener<DrinkingSpot> listener) {
        try {

            if (drinkingSpot.getId() != 0) {
                listener.onRequestSuccess(update(drinkingSpot));
            } else {
                listener.onRequestSuccess(insert(drinkingSpot));
            }
        } catch (DataAccessException e) {
            listener.onRequestFailure(new SpiceException(e));
            e.printStackTrace();
        }


    }

    private DrinkingSpot insert(DrinkingSpot drinkingSpot) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("INSERT INTO drinkingspot (creatorId,beschreibung,startTime,ageFrom,ageTo,gps,amountMaleWithoutBeerBuddy,amountFemaleWithoutBeerBuddy,active, version) VALUES (?,?,?,?,?,?,?,?,?,?)");

            stmt.bindLong(1, drinkingSpot.getCreator().getId());
            if (drinkingSpot.getBeschreibung() != null)
                stmt.bindString(2, drinkingSpot.getBeschreibung());
            stmt.bindString(3, BeerBuddyDbHelper.DATE_FORMAT.format(drinkingSpot.getStartTime()));
            stmt.bindLong(4, drinkingSpot.getAgeFrom());
            stmt.bindLong(5, drinkingSpot.getAgeTo());
            stmt.bindString(6, drinkingSpot.getGps());
            stmt.bindLong(7, drinkingSpot.getAmountMaleWithoutBeerBuddy());
            stmt.bindLong(8, drinkingSpot.getAmountFemaleWithoutBeerBuddy());
            stmt.bindLong(9, drinkingSpot.isActive() ? BeerBuddyDbHelper.BOOLEAN_TRUE : BeerBuddyDbHelper.BOOLEAN_FALSE);
            stmt.bindLong(10, drinkingSpot.getVersion());
            drinkingSpot.setId(stmt.executeInsert());
            drinkingSpotPersonDAO.saveAll(drinkingSpot.getId(), drinkingSpot.getPersons(), database);
            return drinkingSpot;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);

        } finally {
            database.endTransaction();
            database.close();
        }
    }

    private DrinkingSpot update(DrinkingSpot drinkingSpot) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("UPDATE drinkingspot SET creatorId=?,beschreibung=?,ageFrom=?,ageTo=?,gps=?,amountMaleWithoutBeerBuddy=?,amountFemaleWithoutBeerBuddy=?,active=?,version=? WHERE id = ?");
            stmt.bindLong(1, drinkingSpot.getCreator().getId());
            stmt.bindString(2, drinkingSpot.getBeschreibung());
            stmt.bindLong(3, drinkingSpot.getAgeFrom());
            stmt.bindLong(4, drinkingSpot.getAgeTo());
            stmt.bindString(5, drinkingSpot.getGps());
            stmt.bindLong(6, drinkingSpot.getAmountMaleWithoutBeerBuddy());
            stmt.bindLong(7, drinkingSpot.getAmountFemaleWithoutBeerBuddy());
            stmt.bindLong(8, drinkingSpot.isActive() ? BeerBuddyDbHelper.BOOLEAN_TRUE : BeerBuddyDbHelper.BOOLEAN_FALSE);
            stmt.bindLong(9, drinkingSpot.getVersion());
            stmt.bindLong(10, drinkingSpot.getId());
            stmt.executeUpdateDelete();
            drinkingSpotPersonDAO.deleteAll(drinkingSpot.getId(), database);
            drinkingSpotPersonDAO.saveAll(drinkingSpot.getId(), drinkingSpot.getPersons(), database);
            return drinkingSpot;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.close();
            database.endTransaction();

        }
    }


    @Override
    public void join(long dsid, final long personId, final RequestListener<Void> listener) {
        try {
            join(dsid, personId);
            listener.onRequestSuccess(null);
        } catch (DataAccessException e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    public void join(long dsid, long personId) throws DataAccessException {
        DrinkingSpot drinkingSpot = getById(dsid);
        Person p = new Person();
        p.setId(personId);
        drinkingSpot.getPersons().add(p);
        insertOrUpdate(drinkingSpot);
    }

    public DrinkingSpot insertOrUpdate(DrinkingSpot drinkingSpot) throws DataAccessException {
        if (drinkingSpot.getId() != 0) {
            return update(drinkingSpot);
        } else {
            return insert(drinkingSpot);
        }

    }

    public DrinkingSpot getById(long dsid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.rawQuery(SELECT + " WHERE dsid = ?;", new String[]{dsid + ""});
            Collection<DrinkingSpot> drinkingSpot = getDrinkingSpot(dbCursor);
            if (!drinkingSpot.isEmpty()) {
                return drinkingSpot.toArray(new DrinkingSpot[]{})[0];
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to getById DrinkingSpot", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }


    @Override
    public void getById(long dsid, RequestListener<DrinkingSpot> listener) {
        try {
            listener.onRequestSuccess(getById(dsid));
        } catch (Exception e) {
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    @Override
    public void deactivate(long dsid, final RequestListener<Void> listener) {
        try {
            deactivate(dsid);
            listener.onRequestSuccess(null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    public void deactivate(long dsid) throws DataAccessException {
        DrinkingSpot byId = getById(dsid);
        byId.setActive(false);
        insertOrUpdate(byId);
    }

}
