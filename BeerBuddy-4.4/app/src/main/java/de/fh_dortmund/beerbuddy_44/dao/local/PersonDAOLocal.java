package de.fh_dortmund.beerbuddy_44.dao.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;
import de.fh_dortmund.beerbuddy_44.dao.util.BeerBuddyDbHelper;
import de.fh_dortmund.beerbuddy_44.exceptions.DataAccessException;


/**
 * Created by David on 09.11.2015.
 */
public class PersonDAOLocal extends PersonDAO {

    BeerBuddyDbHelper dbHelper;

    public PersonDAOLocal(Context context) {
        super(context);
        dbHelper = new BeerBuddyDbHelper(context);
    }


    @Override
    public List<Person> getAll() throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("person", new String[]{"id", "email", "username", "image", "password", "gender", "dateOfBirth", "interests", "prefers"}, null, null, null, null, null);
            List<Person> list = new LinkedList<Person>();
            while (dbCursor.moveToNext()) {
                Person di = getPerson(dbCursor);
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

    private Person getPerson(Cursor dbCursor) throws ParseException {
        Person p = new Person();
        p.setId(dbCursor.getLong(dbCursor.getColumnIndex("id")));
        p.setEmail(dbCursor.getString(dbCursor.getColumnIndex("email")));
        p.setUsername(dbCursor.getString(dbCursor.getColumnIndex("username")));
        p.setImage(dbCursor.getBlob(dbCursor.getColumnIndex("image")));
        p.setPassword(dbCursor.getString(dbCursor.getColumnIndex("password")));
        p.setGender(dbCursor.getInt(dbCursor.getColumnIndex("gender")));
        p.setDateOfBirth(BeerBuddyDbHelper.DATE_FORMAT.parse(dbCursor.getString(dbCursor.getColumnIndex("dateOfBirth"))));
        p.setInterests(dbCursor.getString(dbCursor.getColumnIndex("interests")));
        p.setPrefers(dbCursor.getString(dbCursor.getColumnIndex("prefers")));
        return p;
    }

    @Override
    public Person getById(long id) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("person", new String[]{"id", "email", "username", "image", "password", "gender", "dateOfBirth", "interests", "prefers"}, "id=?", new String[]{id+""}, null, null, null);
            List<Person> list = new LinkedList<Person>();
            while (dbCursor.moveToNext()) {
                Person di = getPerson(dbCursor);
                return di;
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
    public Person getByEmail(String mail) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        Cursor dbCursor = null;

        try {
            dbCursor = database.query("person", new String[]{"id", "email", "username", "image", "password", "gender", "dateOfBirth", "interests", "prefers"}, "email=?", new String[]{mail}, null, null, null);
            List<Person> list = new LinkedList<Person>();
            while (dbCursor.moveToNext()) {
                Person di = getPerson(dbCursor);
                return di;
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
    public void insertOrUpdate(Person p) throws DataAccessException {
        if(getById(p.getId())!= null)
        {
            update(p);
        }else
        {
            insert(p);
        }
    }

    private void insert(Person p) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("INSERT INTO person (email,username,image,password,gender,dateOfBirth,interests,prefers) VALUES(?,?,?,?,?,?,?,?)");
            stmt.bindString(1, p.getEmail());
            stmt.bindString(2, p.getUsername());
            stmt.bindBlob(3, p.getImage());
            stmt.bindString(4, p.getPassword());
            stmt.bindLong(5, p.getGender());
            if(p.getDateOfBirth()!= null)
            {
                stmt.bindString(6, BeerBuddyDbHelper.DATE_FORMAT.format(p.getDateOfBirth()));
            }
            stmt.bindString(7, p.getInterests());
            stmt.bindString(8, p.getPrefers());
            stmt.executeInsert();
        } catch (Exception e) {
            throw new DataAccessException("Failed to accept DrinkingInvitation", e);
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    private void update(Person p) throws DataAccessException {
        SQLiteDatabase database = dbHelper.getDatabase();
        database.beginTransaction();
        try {
            SQLiteStatement stmt = database.compileStatement("UPDATE person SET email=?,username=?,image=?,password=?,gender=?,dateOfBirth=?,interests=?,prefers=? WHERE id=?");
            stmt.bindString(1, p.getEmail());
            stmt.bindString(2, p.getUsername());
            stmt.bindBlob(3, p.getImage());
            stmt.bindString(4, p.getPassword());
            stmt.bindLong(5, p.getGender());
            if(p.getDateOfBirth()!= null)
            {
                stmt.bindString(6, BeerBuddyDbHelper.DATE_FORMAT.format(p.getDateOfBirth()));
            }
            stmt.bindString(7, p.getInterests());
            stmt.bindString(8, p.getPrefers());
            stmt.bindLong(9,p.getId());
            stmt.executeInsert();
        } catch (Exception e) {
            throw new DataAccessException("Failed to accept DrinkingInvitation", e);
        } finally {
            database.endTransaction();
            database.close();
        }
    }


}
