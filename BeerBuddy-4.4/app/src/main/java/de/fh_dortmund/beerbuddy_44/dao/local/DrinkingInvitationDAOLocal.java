package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 30.11.2015.
 */
public class DrinkingInvitationDAOLocal extends DrinkingInvitationDAO {

    BeerBuddyDbHelper dbHelper;

    public DrinkingInvitationDAOLocal(Context context) {
        super(context);
        dbHelper = new BeerBuddyDbHelper(context);
    }

    @Override
    public DrinkingInvitation insertOrUpdate(DrinkingInvitation i) throws DataAccessException {
        if (getById(i.getId()) != null) {
            return update(i);
        } else {
            return insert(i);
        }
    }

    public DrinkingInvitation insert(DrinkingInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            SQLiteStatement stmt = database.compileStatement("INSERT INTO drinkinginvitation (einladerId,drinkingSpotId,eingeladenerId,freitext) VALUES (?,?,?,?)");
            stmt.bindLong(1, i.getEinladerId());
            stmt.bindLong(2, i.getDrinkingSpotId());
            stmt.bindLong(3, i.getEingeladenerId());
            stmt.bindString(4, i.getFreitext());
            i.setId(stmt.executeInsert());
            return i;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.close();
        }
    }

    public DrinkingInvitation update(DrinkingInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            SQLiteStatement stmt = database.compileStatement("UPDATE  drinkinginvitation SET einladerId = ? , drinkingSpotId = ?, eingeladenerId=?,freitext=?) WHERE id = ?  ");
            stmt.bindLong(1, i.getEinladerId());
            stmt.bindLong(2, i.getDrinkingSpotId());
            stmt.bindLong(3, i.getEingeladenerId());
            stmt.bindLong(3, i.getId());
            stmt.bindString(4, i.getFreitext());
            stmt.executeInsert();
            return i;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.close();
        }
    }


    public DrinkingInvitation getById(long id) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("drinkinginvitation", new String[]{"id", "einladerId", "drinkingSpotId", "eingeladenerId", "freitext"}, " id = ?", new String[]{id + ""}, null, null, null);
            List<DrinkingInvitation> list = new LinkedList<DrinkingInvitation>();
            while (dbCursor.moveToNext()) {
                return getDrinkingInvitation(dbCursor);
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
    public List<DrinkingInvitation> getAllFor(long personid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("drinkinginvitation", new String[]{"id", "einladerId", "drinkingSpotId", "eingeladenerId", "freitext"}, " eingeladenerId = ?", new String[]{personid + ""}, null, null, null);
            List<DrinkingInvitation> list = new LinkedList<DrinkingInvitation>();
            while (dbCursor.moveToNext()) {
                DrinkingInvitation di = getDrinkingInvitation(dbCursor);
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
    private DrinkingInvitation getDrinkingInvitation(Cursor dbCursor) {
        DrinkingInvitation di = new DrinkingInvitation();
        di.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
        di.setEinladerId(dbCursor.getLong(dbCursor.getColumnIndex("einladerId")));
        di.setDrinkingSpotId(dbCursor.getLong(dbCursor.getColumnIndex("drinkingSpotId")));
        di.setEingeladenerId(dbCursor.getLong(dbCursor.getColumnIndex("eingeladenerId")));
        di.setFreitext(dbCursor.getString(dbCursor.getColumnIndex("freitext")));
        return di;
    }


    @Override
    public List<DrinkingInvitation> getAllFrom(long personid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("drinkinginvitation", new String[]{"id", "einladerId", "drinkingSpotId", "eingeladenerId", "freitext"}, " einladerId = ?", new String[]{personid + ""}, null, null, null);
            List<DrinkingInvitation> list = new LinkedList<DrinkingInvitation>();
            while (dbCursor.moveToNext()) {
                DrinkingInvitation di = getDrinkingInvitation(dbCursor);
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

    @Override
    public void accept(DrinkingInvitation friendInvitation) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            //Eingeladener joined dem drinking Spot
            DrinkingSpotDAOLocal dao = new DrinkingSpotDAOLocal(context);
            dao.join(friendInvitation.getEingeladenerId(), friendInvitation.getDrinkingSpotId());

            //löschen der Einladung
            SQLiteStatement stmt = database.compileStatement("DELETE FROM drinkinginvitation WHERE id = ? ");
            stmt.bindLong(1, friendInvitation.getId());
            stmt.executeInsert();
        } catch (Exception e) {
            throw new DataAccessException("Failed to accept DrinkingInvitation", e);
        } finally {
            database.endTransaction();
            database.close();
        }

    }

    @Override
    public void decline(DrinkingInvitation invitation) throws BeerBuddyException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            //löschen der Einladung
            SQLiteStatement stmt = database.compileStatement("DELETE FROM drinkinginvitation WHERE id = ? ");
            stmt.bindLong(1, invitation.getId());
            stmt.executeInsert();
        } catch (Exception e) {
            throw new DataAccessException("Failed to accept DrinkingInvitation", e);
        } finally {
            database.endTransaction();
            database.close();
        }
    }


}
