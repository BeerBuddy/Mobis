package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;

/**
 * Created by David on 30.11.2015.
 */
public class DrinkingInvitationDAOLocal extends DrinkingInvitationDAO {

    BeerBuddyDbHelper dbHelper;

    public DrinkingInvitationDAOLocal(BeerBuddyActivity context) {
        super(context);
        dbHelper = BeerBuddyDbHelper.getInstance(context);
    }

    @Override
    public void insertOrUpdate(DrinkingInvitation i, RequestListener<DrinkingInvitation> listener) {
        try {
            if (i.getId() != 0) {
                listener.onRequestSuccess(update(i));
            } else {
                listener.onRequestSuccess(insert(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    private DrinkingInvitation insert(DrinkingInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("einladerId",  i.getEinladerId());
            values.put("drinkingSpotId", i.getDrinkingSpotId());
            values.put("eingeladenerId", i.getEingeladenerId());
            values.put("freitext", i.getFreitext());
            values.put("version", i.getVersion());

            i.setId(database.insert("drinkinginvitation", null, values));
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert Person", e);
        } finally {
            database.close();
        }
    }

    private DrinkingInvitation update(DrinkingInvitation i) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("einladerId", i.getEinladerId());
            values.put("drinkingSpotId", i.getDrinkingSpotId());
            values.put("eingeladenerId", i.getEingeladenerId());
            values.put("freitext", i.getFreitext());
            values.put("version", i.getVersion());

            database.update("drinkinginvitation",  values, "id=?",new String[]{i.getId()+""});
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert Person", e);
        } finally {
            database.close();
        }
    }

    private DrinkingInvitation getById(long id) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("drinkinginvitation", new String[]{"id", "einladerId", "drinkingSpotId", "eingeladenerId", "freitext", "version"}, " id = ?", new String[]{id + ""}, null, null, null);
            List<DrinkingInvitation> list = new LinkedList<DrinkingInvitation>();
            while (dbCursor.moveToNext()) {
                return getDrinkingInvitation(dbCursor);
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
    public void getAllFor(long personid, RequestListener<DrinkingInvitation[]> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("drinkinginvitation", new String[]{"id", "einladerId", "drinkingSpotId", "eingeladenerId", "freitext", "version"}, " eingeladenerId = ?", new String[]{personid + ""}, null, null, null);
            List<DrinkingInvitation> list = new LinkedList<DrinkingInvitation>();
            while (dbCursor.moveToNext()) {
                DrinkingInvitation di = getDrinkingInvitation(dbCursor);
                list.add(di);
            }
            listener.onRequestSuccess(list.toArray(new DrinkingInvitation[]{}));
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

    private DrinkingInvitation getDrinkingInvitation(Cursor dbCursor) {
        DrinkingInvitation di = new DrinkingInvitation();
        di.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
        di.setEinladerId(dbCursor.getLong(dbCursor.getColumnIndex("einladerId")));
        di.setDrinkingSpotId(dbCursor.getLong(dbCursor.getColumnIndex("drinkingSpotId")));
        di.setEingeladenerId(dbCursor.getLong(dbCursor.getColumnIndex("eingeladenerId")));
        di.setVersion(dbCursor.getLong(dbCursor.getColumnIndex("version")));
        di.setFreitext(dbCursor.getString(dbCursor.getColumnIndex("freitext")));
        return di;
    }


    @Override
    public void getAllFrom(long personid, RequestListener<DrinkingInvitation[]> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("drinkinginvitation", new String[]{"id", "einladerId", "drinkingSpotId", "eingeladenerId", "freitext", "version"}, " einladerId = ?", new String[]{personid + ""}, null, null, null);
            List<DrinkingInvitation> list = new LinkedList<DrinkingInvitation>();
            while (dbCursor.moveToNext()) {
                DrinkingInvitation di = getDrinkingInvitation(dbCursor);
                list.add(di);
            }
            listener.onRequestSuccess(list.toArray(new DrinkingInvitation[]{}));
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
    public void accept(DrinkingInvitation friendInvitation, RequestListener<Void> listener) {
        try {
            //Eingeladener joined dem drinking Spot
            DrinkingSpotDAOLocal dao = new DrinkingSpotDAOLocal(context);
            dao.join(friendInvitation.getDrinkingSpotId(),friendInvitation.getEingeladenerId());
            //löschen der Einladung
            delete(friendInvitation);
            listener.onRequestSuccess(null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }

    }

    public void delete(DrinkingInvitation drinkingInvitation) throws DataAccessException{
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            database.delete("drinkinginvitation", "id = ?", new String[]{drinkingInvitation.getId() + ""});
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to delete DrinkingInvitation",e);
        } finally {
            database.close();
        }
    }

    @Override
    public void decline(DrinkingInvitation invitation, RequestListener<Void> listener) {
        try {
            //löschen der Einladung
            delete(invitation);
            listener.onRequestSuccess(null);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }
    }


}
