package de.fh_dortmund.beerbuddy_44.test.dao;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import junit.framework.Assert;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.remote.PersonDAORemote;

/**
 * Created by dagri001 on 26.10.2015.
 */
@LargeTest
public class RemotePersonDaoTest extends ActivityInstrumentationTestCase2<MainViewActivity> {

    private PersonDAORemote dao ;
    public RemotePersonDaoTest() {
        super(MainViewActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = new PersonDAORemote(getActivity());
        Person p = new Person();
        p.setEmail("test@test.de");
        p.setPassword("password");
        dao.insertOrUpdate(p);
    }

    public void test_getAll() throws Exception {
        List<Person> persons = dao.getAll();
        assertTrue(persons.size() > 0);
    }

    public void test_getByEmail() throws Exception {
        assertNotNull(dao.getByEmail("test@test.de"));
    }

    public void test_getById() throws Exception {
        Person person = dao.getAll().get(1);
        Assert.assertEquals(dao.getById(person.getId()), person);
    }
}
