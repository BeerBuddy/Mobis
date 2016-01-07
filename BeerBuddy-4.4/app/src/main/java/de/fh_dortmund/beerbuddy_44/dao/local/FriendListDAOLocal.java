package de.fh_dortmund.beerbuddy_44.dao.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 19.11.2015.
 */
public class FriendListDAOLocal extends FriendListDAO {

    BeerBuddyDbHelper dbHelper;
    FriendListPersonDAOLocal friendListPersonDAOLocal;
    public FriendListDAOLocal(BeerBuddyActivity context) {
        super(context);
        dbHelper = new BeerBuddyDbHelper(context);
        friendListPersonDAOLocal = new FriendListPersonDAOLocal(context);
    }

    @Override
    public void isFriendFromId(long personid, long friendid, RequestListener<Boolean> listener)  {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            long friendlistid = getFriendList(personid).getId();
            dbCursor = database.query("friendlistperson", new String[]{"id"}, " personid = ? and friendlistid=?", new String[]{personid + "", friendlistid+""}, null, null, null);
            List<FriendInvitation> list = new LinkedList<FriendInvitation>();
            while (dbCursor.moveToNext()) {
                listener.onRequestSuccess(true);
                return ;
            }
            listener.onRequestSuccess(false);
        } catch (Exception e) {
            listener.onRequestFailure(new SpiceException(e));
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    @Override
    public void getFriendList(long personid,  RequestListener<FriendList> listener)  {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("friendlist", new String[]{"id","personid"}, " personid = ?", new String[]{personid + ""}, null, null, null);
            while (dbCursor.moveToNext()) {
                FriendList friendList =new FriendList();
                friendList.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
                friendList.setFriends(friendListPersonDAOLocal.getAllFrom(friendList.getId()));
                friendList.setPersonid(dbCursor.getLong(dbCursor.getColumnIndex("personid")));
                listener.onRequestSuccess(friendList);
                return;
            }
        } catch (Exception e) {
            listener.onRequestFailure(new SpiceException(e));
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    public FriendList getFriendList(long personid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("friendlist", new String[]{"id","personid"}, " personid = ?", new String[]{personid + ""}, null, null, null);
            while (dbCursor.moveToNext()) {
                FriendList friendList =new FriendList();
                friendList.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
                friendList.setFriends(friendListPersonDAOLocal.getAllFrom(friendList.getId()));
                friendList.setPersonid(dbCursor.getLong(dbCursor.getColumnIndex("personid")));
                return friendList;
            }
            throw new DataAccessException("Failed to getFriendList");
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    public FriendList getFriendListById(long id) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.query("friendlist", new String[]{"id","personid"}, " id = ?", new String[]{id + ""}, null, null, null);
            while (dbCursor.moveToNext()) {
                FriendList friendList =new FriendList();
                friendList.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
                friendList.setFriends(friendListPersonDAOLocal.getAllFrom(friendList.getId()));
                friendList.setPersonid(dbCursor.getLong(dbCursor.getColumnIndex("personid")));
                return friendList;
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
    public void insertOrUpdate(FriendList friendList, RequestListener<FriendList> listener)  {
        try {
            if(friendList.getId() != 0)
            {
                listener.onRequestSuccess( update(friendList));
            }
            else
            {
                listener.onRequestSuccess( insert(friendList));
            }
        } catch (DataAccessException e) {
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    public FriendList insert(FriendList i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("INSERT INTO friendlist (personid) VALUES (?)");
            stmt.bindLong(1, i.getPersonid());
            long l = stmt.executeInsert();
            friendListPersonDAOLocal.saveAll(l, i.getFriends());
            i.setId(l);
            return i;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    public FriendList update(FriendList i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("UPDATE friendlist SET personid = ? WHERE id=? ");
            stmt.bindLong(1, i.getPersonid());
            stmt.bindLong(2, i.getId());
            stmt.executeInsert();
            friendListPersonDAOLocal.deleteAll(i.getId());
            friendListPersonDAOLocal.saveAll(i.getId(), i.getFriends());
            return i;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.endTransaction();
            database.close();

        }
    }
}
