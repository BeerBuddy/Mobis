package de.fh_dortmund.beerbuddy_44.dao.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;

/**
 * Created by David on 30.11.2015.
 */
public final class MockUtil {

    private static String[] name = {"Bob", "Jill", "Tom", "Brandon", "Jamie", "Pamela", "Louis", "David", "Jack", "Dexter"};
    private static String[] lastnames = {"Cosby", "Jones", "Miller", "Cooper", "Snow", "Cruz", "Sanchez", "Morgan"};

    private static String[] interests = {"Baseball", "Sports", "TV", "Church", "Drinking", "Freinds", "Cars", "Travelling", "Reading", "Cooking"};
    private static String[] prefers = {"Guiness", "Cocktails", "Disco", "Bar", "Pints"};

    private MockUtil()
    {
    }

    public static ArrayList<Person> createRandomPersons(int size) {
        ArrayList<Person> persons = new ArrayList<Person>();
        for(int i = 0 ; i< size; i++)
        {
            persons.add(createRandomPerson((long)(Math.random() * 10)));
        }
        return persons;
    }

    private static Date getrandomDateOfBirth()
    {
        GregorianCalendar gc = new GregorianCalendar();

        int year = randBetween(1900, 2010);

        gc.set(gc.YEAR, year);

        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));

        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return gc.getTime();
    }

    private static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }


    public static Person createRandomPerson(long id) {
        Person p = new Person();
        p.setPassword("test");
        p.setId(id);
        p.setGender((int) (Math.random() * 2) == 1 ? Person.Gender.MALE : Person.Gender.FEMALE);
        p.setUsername(name[(int) (Math.random() * name.length)] + " " + lastnames[(int) (Math.random() * lastnames.length)]);
        p.setEmail(p.getUsername().replace(" ", ".") + "@beerbuddy.com");
        p.setDateOfBirth(getrandomDateOfBirth());
        p.setInterests(interests[(int) (Math.random() * interests.length)] + ", " + interests[(int) (Math.random() * interests.length)]);
        p.setPrefers(prefers[(int) (Math.random() * prefers.length)]);
        return p;
    }
}
