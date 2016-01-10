package de.fh_dortmund.beerbuddy_44.dao.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
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
    private final String SELECT = "SELECT" +
            " fl.id AS flid, fl.version AS flversion,fl.personid AS flpersonid," +
            " p.id AS pid, p.version AS pversion, p.email AS pemail,p.username AS pusername,p.image AS pimage,p.password AS ppassword,p.gender AS pgender,p.dateOfBirth AS pdateOfBirth,p.interests AS pinterests,p.prefers AS pprefers" +
            " FROM friendlist AS fl LEFT JOIN friendlistperson AS dsp ON (fl.id = dsp.friendlistid) LEFT JOIN person AS p ON(dsp.personid = p.id) ";


    public FriendListDAOLocal(BeerBuddyActivity context) {
        super(context);
        dbHelper = BeerBuddyDbHelper.getInstance(context);
        friendListPersonDAOLocal = new FriendListPersonDAOLocal(context);
    }

    @Override
    public void isFriendFromId(long personid, long friendid, RequestListener<Boolean> listener) {
        try {
            listener.onRequestSuccess(isFriendFromId(personid, friendid));
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    public boolean isFriendFromId(long personid, long friendid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            FriendList friendList = getFriendList(personid);
            if (friendList == null) {
                return false;
            } else {
                long friendlistid = friendList.getId();
                dbCursor = database.query("friendlistperson", new String[]{"id"}, " personid = ? and friendlistid=?", new String[]{personid + "", friendlistid + ""}, null, null, null);
                List<FriendInvitation> list = new LinkedList<FriendInvitation>();
                while (dbCursor.moveToNext()) {
                    return true;
                }
                return false;
            }
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    @Override
    public void getFriendList(long personid, RequestListener<FriendList> listener) {
        try {
            listener.onRequestSuccess(getFriendList(personid));
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    public FriendList getFriendList(long personid) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;
        try {
            dbCursor = database.rawQuery(SELECT + " WHERE flpersonid = ?;", new String[]{personid + ""});
            Collection<FriendList> entitys = getEntitys(dbCursor);
            if (!entitys.isEmpty()) {
                return entitys.toArray(new FriendList[]{})[0];
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DataAccessException("Failed to getFriendList", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
            database.close();
        }
    }

    private Collection<FriendList> getEntitys(Cursor dbCursor) throws DataAccessException, ParseException {
        Map<Long, FriendList> map = new HashMap<Long, FriendList>();
        while (dbCursor.moveToNext()) {
            long dsid = dbCursor.getLong(dbCursor.getColumnIndex("flid"));
            if (map.get(dsid) == null) {
                FriendList di = new FriendList();
                di.setVersion(dbCursor.getLong(dbCursor.getColumnIndex("flversion")));
                di.setId(dsid);
                di.setPersonid(dbCursor.getLong(dbCursor.getColumnIndex("flpersonid")));
                di.getFriends().add(getPerson(dbCursor));
                map.put(dsid, di);
            } else {
                map.get(dsid).getFriends().add(getPerson(dbCursor));
            }
        }
        return map.values();
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
    public void insertOrUpdate(FriendList friendList, RequestListener<FriendList> listener) {
        try {
            if (friendList.getId() != 0) {
                listener.onRequestSuccess(update(friendList));
            } else {
                listener.onRequestSuccess(insert(friendList));
            }
        } catch (DataAccessException e) {
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    public FriendList insertOrUpdate(FriendList friendList) throws DataAccessException {
        if (friendList.getId() != 0) {
            return (update(friendList));
        } else {
            return (insert(friendList));
        }
    }

    public FriendList insert(FriendList i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("INSERT INTO friendlist (personid,version) VALUES (?,?)");
            stmt.bindLong(1, i.getPersonid());
            stmt.bindLong(2, i.getVersion());
            long l = stmt.executeInsert();
            friendListPersonDAOLocal.saveAll(l, i.getFriends(), database);
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
            SQLiteStatement stmt = database.compileStatement("UPDATE friendlist SET personid = ? , version = ? WHERE id=? ");
            stmt.bindLong(1, i.getPersonid());
            stmt.bindLong(2, i.getVersion());
            stmt.bindLong(3, i.getId());
            stmt.executeUpdateDelete();
            friendListPersonDAOLocal.deleteAll(i.getId(), database);
            friendListPersonDAOLocal.saveAll(i.getId(), i.getFriends(), database);
            return i;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert or update DrinkingInvitation", e);
        } finally {
            database.endTransaction();
            database.close();

        }
    }
}
