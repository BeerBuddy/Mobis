package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;


/**
 * Created by David on 09.11.2015.
 */
public class PersonDAOLocal extends PersonDAO {

    BeerBuddyDbHelper dbHelper;

    public PersonDAOLocal(BeerBuddyActivity context) {
        super(context);
        dbHelper = BeerBuddyDbHelper.getInstance(context);
    }


    @Override
    public void getAll(RequestListener<Person[]> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("person", new String[]{"id", "email", "username", "image", "password", "gender", "dateOfBirth", "interests", "prefers", "version"}, null, null, null, null, null);
            List<Person> list = new LinkedList<Person>();
            while (dbCursor.moveToNext()) {
                Person di = getPerson(dbCursor);
                list.add(di);
            }
            listener.onRequestSuccess(list.toArray(new Person[]{}));
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

    private Person getPerson(Cursor dbCursor) throws ParseException {
        Person p = new Person();
        p.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
        p.setEmail(dbCursor.getString(dbCursor.getColumnIndex("email")));
        p.setUsername(dbCursor.getString(dbCursor.getColumnIndex("username")));
        p.setImage(dbCursor.getBlob(dbCursor.getColumnIndex("image")));
        p.setPassword(dbCursor.getString(dbCursor.getColumnIndex("password")));
        p.setGender(dbCursor.getInt(dbCursor.getColumnIndex("gender")));
        String dateOfBirth = dbCursor.getString(dbCursor.getColumnIndex("dateOfBirth"));
        if (dateOfBirth != null)
            p.setDateOfBirth(BeerBuddyDbHelper.DATE_FORMAT.parse(dateOfBirth));
        p.setInterests(dbCursor.getString(dbCursor.getColumnIndex("interests")));
        p.setPrefers(dbCursor.getString(dbCursor.getColumnIndex("prefers")));
        p.setVersion(dbCursor.getLong(dbCursor.getColumnIndex("version")));
        return p;
    }

    public void getById(long id, RequestListener<Person> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("person", new String[]{"id", "email", "username", "image", "password", "gender", "dateOfBirth", "interests", "prefers", "version"}, "id=?", new String[]{id + ""}, null, null, null);
            List<Person> list = new LinkedList<Person>();
            while (dbCursor.moveToNext()) {
                Person di = getPerson(dbCursor);
                listener.onRequestSuccess(di);
                return;
            }
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
    public void getByEmail(String mail, RequestListener<Person> listener) {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("person", new String[]{"id", "email", "username", "image", "password", "gender", "dateOfBirth", "interests", "prefers", "version"}, "email=?", new String[]{mail}, null, null, null);
            List<Person> list = new LinkedList<Person>();
            while (dbCursor.moveToNext()) {
                Person di = getPerson(dbCursor);
                listener.onRequestSuccess(di);
            }
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
    public void insertOrUpdate(Person p, RequestListener<Person> listener) {
        try {
            if (p.getId() != 0) {
                listener.onRequestSuccess(update(p));
            } else {
                listener.onRequestSuccess(insert(p));
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            listener.onRequestFailure(new SpiceException(e));
        }
    }

    public Person insert(Person p) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();

        ContentValues values = new ContentValues();
        try {
            values.put("email", p.getEmail());
            values.put("password", p.getPassword());
            values.put("gender", p.getGender());
            values.put("version", p.getVersion());

            if (p.getUsername() != null)
                values.put("username", p.getUsername());
            if (p.getImage() != null)
                values.put("image", p.getImage());

            if (p.getDateOfBirth() != null) {
                values.put("dateOfBirth", BeerBuddyDbHelper.DATE_FORMAT.format(p.getDateOfBirth()));
            }
            if (p.getInterests() != null)
                values.put("interests", p.getInterests());

            if (p.getPrefers() != null)
                values.put("prefers", p.getPrefers());

            p.setId(database.insert("person", null, values));
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to insert Person", e);
        } finally {
            database.close();
        }
    }

    public Person update(Person p) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("email", p.getInterests());
            values.put("password", p.getInterests());
            values.put("gender", p.getInterests());
            values.put("version", p.getVersion());

            if (p.getUsername() != null)
                values.put("username", p.getUsername());
            if (p.getImage() != null)
                values.put("image", p.getImage());

            if (p.getDateOfBirth() != null) {
                values.put("dateOfBirth", BeerBuddyDbHelper.DATE_FORMAT.format(p.getDateOfBirth()));
            }
            if (p.getInterests() != null)
                values.put("interests", p.getInterests());

            if (p.getPrefers() != null)
                values.put("prefers", p.getPrefers());

            database.update("person", values, "id=?", new String[]{p.getId() + ""});
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("Failed to update Person", e);
        } finally {
            database.close();
        }

    }


    public Person getById(long id, SQLiteDatabase database) throws DataAccessException {
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("person", new String[]{"id", "email", "username", "image", "password", "gender", "dateOfBirth", "interests", "prefers", "version"}, "id=?", new String[]{id + ""}, null, null, null);
            List<Person> list = new LinkedList<Person>();
            while (dbCursor.moveToNext()) {
                Person di = getPerson(dbCursor);
                return di;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("", e);
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
        }
    }
}
