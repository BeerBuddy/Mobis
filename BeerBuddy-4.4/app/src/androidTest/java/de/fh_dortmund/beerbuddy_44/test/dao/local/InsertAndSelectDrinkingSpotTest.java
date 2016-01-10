package de.fh_dortmund.beerbuddy_44.test.dao.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import junit.framework.Assert;

import java.util.Date;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.local.DrinkingSpotDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.local.PersonDAOLocal;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 10.01.2016.
 */
public class InsertAndSelectDrinkingSpotTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    BeerBuddyDbHelper db;
    private DrinkingSpotDAOLocal spotDAOLocal;
    private PersonDAOLocal personDAOLocal;

    public InsertAndSelectDrinkingSpotTest() {
        super(MainViewActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        db = BeerBuddyDbHelper.getInstance(getActivity());
        spotDAOLocal = new DrinkingSpotDAOLocal(getActivity());
        personDAOLocal = new PersonDAOLocal(getActivity());
    }

    public void testInsert() throws DataAccessException {
        Person p = insertPerson();
        DrinkingSpot ds = new DrinkingSpot();
        ds.setCreator(p);
        ds.setStartTime(new Date());
        ds.setGps("asd");
        ds = spotDAOLocal.insert(ds);
        Assert.assertTrue(ds.getId() != -1);
        Assert.assertTrue(ds.getId() != 0);
        Log.i("BeerBuddy", ds.toString());
        //select it
        SQLiteDatabase writableDatabase = db.getWritableDatabase();

        Cursor dbCursor
                = writableDatabase.rawQuery("Select * from person", null);
        while (dbCursor.moveToNext()) {
            for (String colname : dbCursor.getColumnNames()) {
                Log.i("BeerBuddy", colname + ": " + dbCursor.getString(dbCursor.getColumnIndex(colname)));
                ;
            }
        }
        dbCursor.close();
        assertTrue(dbCursor.getCount() > 0);

        dbCursor = writableDatabase.rawQuery("Select * from drinkingspot where id = ?", new String[]{ds.getId()+""});
        while (dbCursor.moveToNext()) {
            for (String colname : dbCursor.getColumnNames()) {
                Log.i("BeerBuddy", colname + ": " + dbCursor.getString(dbCursor.getColumnIndex(colname)));
                ;
            }

            assertEquals(ds.getId(), dbCursor.getLong(dbCursor.getColumnIndex("id")));
        }
        assertTrue(dbCursor.getCount() > 0);
        dbCursor.close();

        DrinkingSpot byId = spotDAOLocal.getById(ds.getId());
        assertNotNull(byId);

        byId = spotDAOLocal.getActiveByPersonId(p.getId());
        assertNotNull(byId);
    }

    private Person insertPerson() throws DataAccessException {
        Person p = new Person();
        p.setEmail("Test@test.de");
        p.setPassword("asdasd");
        p = personDAOLocal.insert(p);
        Assert.assertTrue(p.getId() != -1);
        Assert.assertTrue(p.getId() != 0);
        Log.i("BeerBuddy", p.toString());
        return p;
    }
}
