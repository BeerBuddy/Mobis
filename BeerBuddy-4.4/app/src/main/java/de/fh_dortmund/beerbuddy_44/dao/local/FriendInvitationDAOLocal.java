package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 30.11.2015.
 */
public class FriendInvitationDAOLocal extends FriendInvitationDAO {

    BeerBuddyDbHelper dbHelper;

    public FriendInvitationDAOLocal(Context context) {
        super(context);
        dbHelper = new BeerBuddyDbHelper(context);
    }

    @Override
    public FriendInvitation insertOrUpdate(FriendInvitation i) throws DataAccessException {
        if (getById(i.getId()) != null) {
           return update(i);
        } else {
            return insert(i);
        }
    }

    public FriendInvitation insert(FriendInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            SQLiteStatement stmt = database.compileStatement("INSERT INTO friendinvitation (einladerId,eingeladenerId,freitext) VALUES (?,?,?)");
            stmt.bindLong(1, i.getEinladerId());
            stmt.bindLong(2, i.getEingeladenerId());
            stmt.bindString(3, i.getFreitext());
            long l = stmt.executeInsert();
            i.setId(l);
            return i;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.close();
        }
    }

    public FriendInvitation update(FriendInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            SQLiteStatement stmt = database.compileStatement("UPDATE  friendinvitation SET einladerId = ? ,  eingeladenerId=?,freitext=?) WHERE id = ?  ");
            stmt.bindLong(1, i.getEinladerId());
            stmt.bindLong(2, i.getEingeladenerId());
            stmt.bindString(3, i.getFreitext());
            stmt.bindLong(2, i.getId());
            stmt.executeInsert();
            return  i;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.close();
        }
    }

    public void delete(FriendInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            SQLiteStatement stmt = database.compileStatement("DELETE FROM friendinvitation WHERE id = ?  ");
            stmt.bindLong(2, i.getId());
            stmt.executeInsert();
        } catch (Exception e) {
            throw new DataAccessException("Failed to delete FriendInvitation", e);
        } finally {
            database.close();
        }
    }


    public FriendInvitation getById(long id) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("drinkinginvitation", new String[]{"id", "einladerId", "drinkingSpotId", "eingeladenerId", "freitext"}, " id = ?", new String[]{id + ""}, null, null, null);
            List<FriendInvitation> list = new LinkedList<FriendInvitation>();
            while (dbCursor.moveToNext()) {
                return getFriendInvitatio(dbCursor);
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
    public List<FriendInvitation> getAllFor(long personid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("friendinvitation", new String[]{"id", "einladerId", "eingeladenerId", "freitext"}, " eingeladenerId = ?", new String[]{personid + ""}, null, null, null);
            List<FriendInvitation> list = new LinkedList<FriendInvitation>();
            while (dbCursor.moveToNext()) {
                FriendInvitation di = getFriendInvitatio(dbCursor);
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
    private FriendInvitation getFriendInvitatio(Cursor dbCursor) {
        FriendInvitation di = new FriendInvitation();
        di.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
        di.setEinladerId(dbCursor.getLong(dbCursor.getColumnIndex("einladerId")));
        di.setEingeladenerId(dbCursor.getLong(dbCursor.getColumnIndex("eingeladenerId")));
        di.setFreitext(dbCursor.getString(dbCursor.getColumnIndex("freitext")));
        return di;
    }


    @Override
    public List<FriendInvitation> getAllFrom(long personid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("friendinvitation", new String[]{"id", "einladerId",  "eingeladenerId", "freitext"}, " einladerId = ?", new String[]{personid + ""}, null, null, null);
            List<FriendInvitation> list = new LinkedList<FriendInvitation>();
            while (dbCursor.moveToNext()) {
                list.add(getFriendInvitatio(dbCursor));
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
    public void accept(FriendInvitation friendInvitation) throws DataAccessException {
        //einladung löschen und freund zur Freundesliste hinzufügen
        try {
            FriendListDAOLocal dao = new FriendListDAOLocal(context);
            PersonDAOLocal dao2 = new PersonDAOLocal(context);

            //Eingeladener FreindList den Einlader hinzufügen
            FriendList list = dao.getFriendList(friendInvitation.getEingeladenerId());
            list.getFriends().add(dao2.getById(friendInvitation.getEinladerId()));
            //und speichern
            dao.insertOrUpdate(list);

            //Einlader FreindList den Eingeladenen hinzufügen
            FriendList list1 = dao.getFriendList(friendInvitation.getEinladerId());
            list1.getFriends().add(dao2.getById(friendInvitation.getEingeladenerId()));
            //und speichern
            dao.insertOrUpdate(list1);

            //löschen der Einladung
            delete(friendInvitation);
        } catch (Exception e) {
            throw new DataAccessException("Failed to accept FriendInvitation",e);
        }

    }

    @Override
    public void decline(FriendInvitation invitation) throws BeerBuddyException {
        //FIXME
    }


}
