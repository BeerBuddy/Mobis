package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by grimm on 07.01.2016.
 */
public class DrinkingSpotPersonDAO {
    private final BeerBuddyActivity context;
    private final PersonDAOLocal personDAOLocal;
    BeerBuddyDbHelper dbHelper;


    public DrinkingSpotPersonDAO(BeerBuddyActivity context) {
        this.context = context;
        dbHelper = BeerBuddyDbHelper.getInstance(context);
         personDAOLocal = new PersonDAOLocal(context);
    }

    public void deleteAll(long id,SQLiteDatabase database) throws DataAccessException {
        try {
            SQLiteStatement stmt = database.compileStatement("DELETE FROM drinkingspotperson WHERE drinkingSpotId = ?");
            stmt.bindLong(1, id);
            stmt.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to delete all DrinkingSpotPerson", e);
        }
    }

    public void saveAll(long id, List<Person> persons, SQLiteDatabase database) throws DataAccessException {
        try {
            for(Person p: persons)
            {
                SQLiteStatement stmt = database.compileStatement("INSERT INTO drinkingspotperson (drinkingSpotId,personid) VALUES(?,?)");
                stmt.bindLong(1, id);
                stmt.bindLong(2, p.getId());
                stmt.executeInsert();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert DrinkingSpotPerson", e);
        }
    }

    public List<Person> getByDsId(long id, SQLiteDatabase database) throws DataAccessException {
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("drinkingspotperson", new String[]{"id", "drinkingSpotId", "personid"}, " id = ?", new String[]{id + ""}, null, null, null);
            List<Person> list = new LinkedList<Person>();
            
            while (dbCursor.moveToNext()) {
                list.add(personDAOLocal.getById(dbCursor.getLong(dbCursor.getColumnIndex("personid")),database));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to getByDsId DrinkingSpotPerson", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
        }
    }
}
