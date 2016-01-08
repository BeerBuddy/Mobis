package de.fh_dortmund.beerbuddy_44.dao.local;

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

    public List<Person> getAllFrom(long friendlistid,SQLiteDatabase database ) throws DataAccessException {
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("friendlistperson", new String[]{"id","friendlistid","personid"}, " friendlistid = ?", new String[]{friendlistid + ""}, null, null, null);
            List<Person> list = new LinkedList<Person>();
            while (dbCursor.moveToNext()) {
                list.add(personDAO.getById(dbCursor.getLong(dbCursor.getColumnIndex("personid")), database));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
        }
    }

    public void saveAll(long l, List<Person> friends,SQLiteDatabase database ) throws DataAccessException {
        try {
            for(Person p: friends)
            {
                SQLiteStatement stmt = database.compileStatement("INSERT INTO friendlistperson (friendlistid,personid) VALUES(?,?)");
                stmt.bindLong(1, l);
                stmt.bindLong(2, p.getId());
                stmt.executeInsert();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert DrinkingSpotPerson", e);
        } finally {
        }
    }

    public void deleteAll(long id,SQLiteDatabase database ) throws DataAccessException {
        try {
            SQLiteStatement stmt = database.compileStatement("DELETE FROM friendlistperson WHERE friendlistid = ?");
            stmt.bindLong(1, id);
            stmt.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to delete all DrinkingSpotPerson", e);
        } finally {
        }
    }
}
