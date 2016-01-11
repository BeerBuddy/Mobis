package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 30.11.2015.
 *
 * Not working
 */

public class FriendInvitationDAOLocal extends FriendInvitationDAO {

    BeerBuddyDbHelper dbHelper;

    public FriendInvitationDAOLocal(BeerBuddyActivity context) {
        super(context);
        dbHelper = BeerBuddyDbHelper.getInstance(context);
    }

    @Override
    public void insertOrUpdate(FriendInvitation i, RequestListener<FriendInvitation> listener) {
        try {
            if (i.getId() != 0) {
                listener.onRequestSuccess(update(i));
            } else {
                listener.onRequestSuccess(insert(i));
            }
        } catch (DataAccessException e) {
            listener.onRequestFailure(new SpiceException(e));
        }

    }

    public FriendInvitation insert(FriendInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("einladerId", i.getEinladerId());
            if(i.getId() != 0)
            values.put("id", i.getId());
            values.put("eingeladenerId", i.getEingeladenerId());
            values.put("freitext", i.getFreitext());
            values.put("version", i.getVersion());
            i.setId( database.insert("friendinvitation", null, values));
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.close();
        }
    }

    public FriendInvitation update(FriendInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("einladerId", i.getEinladerId());
            values.put("eingeladenerId", i.getEingeladenerId());
            values.put("freitext", i.getFreitext());
            values.put("version", i.getVersion());
            database.update("friendinvitation", values, "id = ?", new String[]{i.getId() + ""});
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.close();
        }
    }

    public void delete(FriendInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            database.delete("friendinvitation", "id = ?", new String[]{i.getId() + ""});
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to delete FriendInvitation", e);
        } finally {
            database.close();
        }
    }


    private FriendInvitation getById(long id) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("drinkinginvitation", new String[]{"id", "einladerId", "drinkingSpotId", "eingeladenerId", "freitext", "version"}, " id = ?", new String[]{id + ""}, null, null, null);
            List<FriendInvitation> list = new LinkedList<FriendInvitation>();
            while (dbCursor.moveToNext()) {
                return getFriendInvitatio(dbCursor);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    @Override
    public void getAllFor(long personid, RequestListener<FriendInvitation[]> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("friendinvitation", new String[]{"id", "einladerId", "eingeladenerId", "freitext", "version"}, " eingeladenerId = ?", new String[]{personid + ""}, null, null, null);
            List<FriendInvitation> list = new LinkedList<FriendInvitation>();
            while (dbCursor.moveToNext()) {
                FriendInvitation di = getFriendInvitatio(dbCursor);
                list.add(di);
            }
            listener.onRequestSuccess(list.toArray(new FriendInvitation[]{}));
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

    private FriendInvitation getFriendInvitatio(Cursor dbCursor) {
        FriendInvitation di = new FriendInvitation();
        di.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
        di.setEinladerId(dbCursor.getLong(dbCursor.getColumnIndex("einladerId")));
        di.setEingeladenerId(dbCursor.getLong(dbCursor.getColumnIndex("eingeladenerId")));
        di.setVersion(dbCursor.getLong(dbCursor.getColumnIndex("version")));
        di.setFreitext(dbCursor.getString(dbCursor.getColumnIndex("freitext")));
        return di;
    }


    @Override
    public void getAllFrom(long personid, RequestListener<FriendInvitation[]> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("friendinvitation", new String[]{"id", "einladerId", "eingeladenerId", "freitext", "version"}, " einladerId = ?", new String[]{personid + ""}, null, null, null);
            List<FriendInvitation> list = new LinkedList<FriendInvitation>();
            while (dbCursor.moveToNext()) {
                list.add(getFriendInvitatio(dbCursor));
            }
            listener.onRequestSuccess(list.toArray(new FriendInvitation[]{}));
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
    public void accept(final FriendInvitation friendInvitation, final RequestListener<Void> listener) {
        try {
            accept(friendInvitation);
            listener.onRequestSuccess(null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    public void accept(FriendInvitation friendInvitation) throws DataAccessException{
        FriendListDAOLocal friendListDAOLocal = new FriendListDAOLocal(context);
        FriendList friendList = friendListDAOLocal.getFriendList(friendInvitation.getEingeladenerId());
        FriendList friendList1 = friendListDAOLocal.getFriendList(friendInvitation.getEinladerId());
        if(friendList == null)
        {
            friendList = new FriendList();
            friendList.setPersonid(friendInvitation.getEingeladenerId());
        }

        if(friendList1 == null)
        {
            friendList1 = new FriendList();
            friendList1.setPersonid(friendInvitation.getEinladerId());
        }
        friendList.getFriends().add(new Person(friendInvitation.getEinladerId()));
        friendList1.getFriends().add(new Person(friendInvitation.getEingeladenerId()));
        friendListDAOLocal.insertOrUpdate(friendList);
        friendListDAOLocal.insertOrUpdate(friendList1);
        delete(friendInvitation);
    }

    @Override
    public void decline(FriendInvitation invitation, RequestListener<Void> listener) {
        try {
            decline(invitation);
            listener.onRequestSuccess(null);
        } catch (DataAccessException e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }

    }


    public void decline(FriendInvitation invitation) throws DataAccessException {
        delete(invitation);
    }
}
