package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
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

    public DrinkingSpotDAOLocal(Context context) {
        super(context);
        dbHelper = new BeerBuddyDbHelper(context);
        drinkingSpotPersonDAO= new DrinkingSpotPersonDAO(context);
        personDAOLocal = new PersonDAOLocal(context);
    }

    @Override
    public List<DrinkingSpot> getAll() throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("drinkingspot", new String[]{"id", "creatorId", "beschreibung", "startTime", "ageFrom", "ageTo", "gps", "amountMaleWithoutBeerBuddy", "amountFemaleWithoutBeerBuddy", "active"}, null, null, null, null, null);
            List<DrinkingSpot> list = new LinkedList<DrinkingSpot>();
            while (dbCursor.moveToNext()) {
                DrinkingSpot di = getDrinkingSpot(dbCursor);
                list.add(di);
            }
            return list;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    @NonNull
    private DrinkingSpot getDrinkingSpot(Cursor dbCursor) throws DataAccessException, ParseException {
        DrinkingSpot di = new DrinkingSpot();
        di.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
        di.setPersons(drinkingSpotPersonDAO.getByDsId(di.getId()));
        di.setCreator(personDAOLocal.getById(dbCursor.getLong(dbCursor.getColumnIndex("creatorId"))));
        di.setBeschreibung(dbCursor.getString(dbCursor.getColumnIndex("beschreibung")));
        di.setStartTime(BeerBuddyDbHelper.DATE_FORMAT.parse(dbCursor.getString(dbCursor.getColumnIndex("startTime"))));
        di.setAgeFrom(dbCursor.getInt(dbCursor.getColumnIndex("ageFrom")));
        di.setAgeTo(dbCursor.getInt(dbCursor.getColumnIndex("ageTo")));
        di.setGps(dbCursor.getString(dbCursor.getColumnIndex("gps")));
        di.setAmountMaleWithoutBeerBuddy(dbCursor.getInt(dbCursor.getColumnIndex("amountMaleWithoutBeerBuddy")));
        di.setAmountFemaleWithoutBeerBuddy(dbCursor.getInt(dbCursor.getColumnIndex("amountFemaleWithoutBeerBuddy")));
        di.setActive(dbCursor.getInt(dbCursor.getColumnIndex("active")) == BeerBuddyDbHelper.BOOLEAN_TRUE);
        return di;
    }


    @Override
    public DrinkingSpot getActiveByPersonId(long personId) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("drinkingspot", new String[]{"id", "creatorId", "beschreibung", "startTime", "ageFrom", "ageTo", "gps", "amountMaleWithoutBeerBuddy", "amountFemaleWithoutBeerBuddy", "active"}, "active=? and personid=?", new String[]{BeerBuddyDbHelper.BOOLEAN_TRUE+"", personId+""}, null, null, null);
            List<DrinkingSpot> list = new LinkedList<DrinkingSpot>();
            while (dbCursor.moveToNext()) {
               return getDrinkingSpot(dbCursor);
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }


    @Override
    public DrinkingSpot insertOrUpdate(DrinkingSpot drinkingSpot) throws DataAccessException {
        if (getById(drinkingSpot.getId()) != null) {
            return update(drinkingSpot);
        } else {
            return insert(drinkingSpot);
        }

    }

    public DrinkingSpot insert(DrinkingSpot drinkingSpot) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("INSERT INTO drinkingspot (creatorId,beschreibung,startTime,ageFrom,ageTo,gps,amountMaleWithoutBeerBuddy,amountFemaleWithoutBeerBuddy,active) VALUES (?,?,?,?,?,?,?,?,?)");
            stmt.bindLong(1, drinkingSpot.getCreator().getId());
            stmt.bindString(2, drinkingSpot.getBeschreibung());
            if(drinkingSpot.getStartTime() != null)
            {
                stmt.bindString(3, BeerBuddyDbHelper.DATE_FORMAT.format(drinkingSpot.getStartTime()));
            }
            stmt.bindLong(4, drinkingSpot.getAgeFrom());
            stmt.bindLong(5, drinkingSpot.getAgeTo());
            stmt.bindString(6, drinkingSpot.getGps());
            stmt.bindLong(7, drinkingSpot.getAmountMaleWithoutBeerBuddy());
            stmt.bindLong(8, drinkingSpot.getAmountFemaleWithoutBeerBuddy());
            stmt.bindLong(9, drinkingSpot.isActive() ? BeerBuddyDbHelper.BOOLEAN_TRUE : BeerBuddyDbHelper.BOOLEAN_FALSE);
            long dsid = stmt.executeInsert();
            drinkingSpotPersonDAO.saveAll(dsid, drinkingSpot.getPersons());
            drinkingSpot.setId(dsid);
            return drinkingSpot;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    public DrinkingSpot update(DrinkingSpot drinkingSpot) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("UPDATE drinkingspot SET creatorId=?,beschreibung=?,ageFrom=?,ageTo=?,gps=?,amountMaleWithoutBeerBuddy=?,amountFemaleWithoutBeerBuddy=?,active WHERE id = ?");
            stmt.bindLong(8, drinkingSpot.getCreator().getId());
            stmt.bindString(1, drinkingSpot.getBeschreibung());
            stmt.bindLong(2, drinkingSpot.getAgeFrom());
            stmt.bindLong(3, drinkingSpot.getAgeTo());
            stmt.bindString(4, drinkingSpot.getGps());
            stmt.bindLong(5, drinkingSpot.getAmountMaleWithoutBeerBuddy());
            stmt.bindLong(6, drinkingSpot.getAmountFemaleWithoutBeerBuddy());
            stmt.bindLong(7, drinkingSpot.isActive() ? BeerBuddyDbHelper.BOOLEAN_TRUE : BeerBuddyDbHelper.BOOLEAN_FALSE);
            stmt.executeInsert();
            drinkingSpotPersonDAO.deleteAll(drinkingSpot.getId());
            drinkingSpotPersonDAO.saveAll(drinkingSpot.getId(), drinkingSpot.getPersons());
            return drinkingSpot;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.endTransaction();
            database.close();

        }
    }

    @Override
    public DrinkingSpot getById(long dsid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("drinkingspot", new String[]{"id", "creatorId", "beschreibung", "startTime", "ageFrom", "ageTo", "gps", "amountMaleWithoutBeerBuddy", "amountFemaleWithoutBeerBuddy", "active"}, "id=?", new String[]{dsid+""}, null, null, null);
            List<DrinkingSpot> list = new LinkedList<DrinkingSpot>();
            while (dbCursor.moveToNext()) {
                return getDrinkingSpot(dbCursor);
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    @Override
    public void join(long dsid, long personId) throws DataAccessException {
        DrinkingSpot drinkingSpot = getById(dsid);
        drinkingSpot.getPersons().add(new PersonDAOLocal(context).getById(personId));
        insertOrUpdate(drinkingSpot);
    }

    @Override
    public void deactivate(long dsid) throws BeerBuddyException {
        DrinkingSpot drinkingSpot = getById(dsid);
        drinkingSpot.setActive(false);
        insertOrUpdate(drinkingSpot);
    }

}
