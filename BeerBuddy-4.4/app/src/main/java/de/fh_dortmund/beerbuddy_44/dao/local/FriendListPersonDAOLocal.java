package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by grimm on 07.01.2016.
 */
public class FriendListPersonDAOLocal {
    BeerBuddyDbHelper dbHelper;
    PersonDAOLocal personDAO;
    public FriendListPersonDAOLocal(BeerBuddyActivity context) {
        dbHelper = BeerBuddyDbHelper.getInstance(context);
        personDAO = new PersonDAOLocal(context);
    }

    public void saveAll(long l, List<Person> friends,SQLiteDatabase database ) throws DataAccessException {
        try {
            for(Person p: friends)
            {

                ContentValues values = new ContentValues();
                values.put("personid", p.getId());
                values.put("friendlistid", l);
                database.insert("friendlistperson", null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert friendlistperson", e);
        }
    }

    public void deleteAll(long id,SQLiteDatabase database ) throws DataAccessException {
        try {
            database.delete("friendlistperson", "friendlistid = ?", new String[]{id + ""});
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to delete all friendlistperson", e);
        }
    }
}
