package de.fh_dortmund.beerbuddy_44.test.dao.common;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import junit.framework.Assert;

import java.util.Arrays;
import java.util.Date;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;

/**
 * Created by grimm on 08.01.2016.
 */
public abstract class DrinkingInvitationDAOTest extends ActivityInstrumentationTestCase2<MainViewActivity> {


    private boolean testDone;

    public DrinkingInvitationDAOTest() {
        super(MainViewActivity.class);
    }

    public abstract DrinkingSpotDAO getDrinkingSpotDAO();

    public abstract DrinkingInvitationDAO getDrinkingInvitationDAO();

    public abstract PersonDAO getPersonDAO();

    public void test_Insert_Get() {

        Person p = new Person();
        p.setEmail("test@Insert.de");
        p.setPassword("test_Insert_Get1");

        getPersonDAO().insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                testDone = true;
                fail(spiceException.getMessage());
            }

            @Override
            public void onRequestSuccess(final Person p1) {
                Person p2 = new Person();
                p2.setEmail("test2@Insert.de");
                p2.setPassword("test_Insert_Get2");
                getPersonDAO().insertOrUpdate(p2, new RequestListener<Person>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        testDone = true;
                        fail(spiceException.getMessage());
                    }

                    @Override
                    public void onRequestSuccess(final Person p2) {
                        DrinkingSpot ds = new DrinkingSpot();
                        ds.setCreator(p1);
                        final String gps = "-33.865143;151.209900";
                        ds.setGps(gps);
                        ds.setStartTime(new Date());
                        getDrinkingSpotDAO().insertOrUpdate(ds, new RequestListener<DrinkingSpot>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                testDone = true;
                                fail(spiceException.getMessage());
                                spiceException.printStackTrace();
                            }

                            @Override
                            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                                assertFalse(drinkingSpot.getId() == 0);
                                assertEquals(p1, drinkingSpot.getCreator());
                                assertEquals(gps, drinkingSpot.getGps());
                                assertNotNull(drinkingSpot.getStartTime());
                                DrinkingInvitation drinkingInvitation = new DrinkingInvitation();
                                drinkingInvitation.setDrinkingSpotId(drinkingSpot.getId());
                                drinkingInvitation.setEingeladenerId(p2.getId());
                                drinkingInvitation.setEinladerId(p1.getId());

                                getDrinkingInvitationDAO().insertOrUpdate(drinkingInvitation, new RequestListener<DrinkingInvitation>() {
                                    @Override
                                    public void onRequestFailure(SpiceException spiceException) {
                                        testDone = true;
                                        fail(spiceException.getMessage());
                                    }

                                    @Override
                                    public void onRequestSuccess(final DrinkingInvitation drinkingInvitation) {
                                        getDrinkingInvitationDAO().getAllFor(p2.getId(), new RequestListener<DrinkingInvitation[]>() {
                                            @Override
                                            public void onRequestFailure(SpiceException spiceException) {
                                                testDone = true;
                                                fail(spiceException.getMessage());
                                            }

                                            @Override
                                            public void onRequestSuccess(DrinkingInvitation[] di) {
                                                Assert.assertTrue(Arrays.asList(di).contains(drinkingInvitation));
                                                testDone = true;
                                            }
                                        });

                                        getDrinkingInvitationDAO().getAllFrom(p1.getId(), new RequestListener<DrinkingInvitation[]>() {
                                            @Override
                                            public void onRequestFailure(SpiceException spiceException) {
                                                testDone = true;
                                                fail(spiceException.getMessage());
                                            }

                                            @Override
                                            public void onRequestSuccess(DrinkingInvitation[] di) {
                                                Assert.assertTrue(Arrays.asList(di).contains(drinkingInvitation));
                                                testDone = true;
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        while (!testDone) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public void test_Accept() {

        Person p = new Person();
        p.setEmail("test@Insert.de");
        p.setPassword("test_Insert_Get1");

        getPersonDAO().insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                testDone = true;
                fail(spiceException.getMessage());
            }

            @Override
            public void onRequestSuccess(final Person p1) {
                Person p2 = new Person();
                p2.setEmail("test2@Insert.de");
                p2.setPassword("test_Insert_Get2");
                getPersonDAO().insertOrUpdate(p2, new RequestListener<Person>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        testDone = true;
                        fail(spiceException.getMessage());
                    }

                    @Override
                    public void onRequestSuccess(final Person p2) {
                        DrinkingSpot ds = new DrinkingSpot();
                        ds.setCreator(p1);
                        final String gps = "-33.865143;151.209900";
                        ds.setGps(gps);
                        ds.setStartTime(new Date());
                        getDrinkingSpotDAO().insertOrUpdate(ds, new RequestListener<DrinkingSpot>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                testDone = true;
                                fail(spiceException.getMessage());
                                spiceException.printStackTrace();
                            }

                            @Override
                            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                                assertTrue(drinkingSpot.getId()  != -1);
                                assertEquals(p1, drinkingSpot.getCreator());
                                assertEquals(gps, drinkingSpot.getGps());
                                assertNotNull(drinkingSpot.getStartTime());
                                DrinkingInvitation drinkingInvitation = new DrinkingInvitation();
                                drinkingInvitation.setDrinkingSpotId(drinkingSpot.getId());
                                drinkingInvitation.setEingeladenerId(p2.getId());
                                drinkingInvitation.setEinladerId(p1.getId());

                                getDrinkingInvitationDAO().insertOrUpdate(drinkingInvitation, new RequestListener<DrinkingInvitation>() {
                                    @Override
                                    public void onRequestFailure(SpiceException spiceException) {
                                        testDone = true;
                                        fail(spiceException.getMessage());
                                    }

                                    @Override
                                    public void onRequestSuccess(final DrinkingInvitation drinkingInvitation) {
                                        getDrinkingInvitationDAO().accept(drinkingInvitation, new RequestListener<Void>() {
                                            @Override
                                            public void onRequestFailure(SpiceException spiceException) {
                                                testDone = true;
                                                fail(spiceException.getMessage());
                                            }

                                            @Override
                                            public void onRequestSuccess(Void aVoid) {
                                                getDrinkingSpotDAO().getActiveByPersonId(p1.getId(), new RequestListener<DrinkingSpot>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                                                        Assert.assertTrue(drinkingSpot.getPersons().contains(p2));
                                                        testDone = true;
                                                    }
                                                });
                                                getDrinkingInvitationDAO().getAllFor(p2.getId(), new RequestListener<DrinkingInvitation[]>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(DrinkingInvitation[] di) {
                                                        Assert.assertFalse(Arrays.asList(di).contains(drinkingInvitation));
                                                        testDone = true;
                                                    }
                                                });

                                                getDrinkingInvitationDAO().getAllFrom(p1.getId(), new RequestListener<DrinkingInvitation[]>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(DrinkingInvitation[] di) {
                                                        Assert.assertFalse(Arrays.asList(di).contains(drinkingInvitation));
                                                        testDone = true;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        while (!testDone) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public void test_Decline() {

        Person p = new Person();
        p.setEmail("test@Insert.de");
        p.setPassword("test_Insert_Get1");

        getPersonDAO().insertOrUpdate(p, new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                testDone = true;
                fail(spiceException.getMessage());
            }

            @Override
            public void onRequestSuccess(final Person p1) {
                Person p2 = new Person();
                p2.setEmail("test2@Insert.de");
                p2.setPassword("test_Insert_Get2");
                getPersonDAO().insertOrUpdate(p2, new RequestListener<Person>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        testDone = true;
                        fail(spiceException.getMessage());
                    }

                    @Override
                    public void onRequestSuccess(final Person p2) {
                        DrinkingSpot ds = new DrinkingSpot();
                        ds.setCreator(p1);
                        final String gps = "-33.865143;151.209900";
                        ds.setGps(gps);
                        ds.setStartTime(new Date());
                        getDrinkingSpotDAO().insertOrUpdate(ds, new RequestListener<DrinkingSpot>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                testDone = true;
                                fail(spiceException.getMessage());
                                spiceException.printStackTrace();
                            }

                            @Override
                            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                                assertFalse(drinkingSpot.getId() == -1);
                                assertEquals(p1, drinkingSpot.getCreator());
                                assertEquals(gps, drinkingSpot.getGps());
                                assertNotNull(drinkingSpot.getStartTime());
                                DrinkingInvitation drinkingInvitation = new DrinkingInvitation();
                                drinkingInvitation.setDrinkingSpotId(drinkingSpot.getId());
                                drinkingInvitation.setEingeladenerId(p2.getId());
                                drinkingInvitation.setEinladerId(p1.getId());

                                getDrinkingInvitationDAO().insertOrUpdate(drinkingInvitation, new RequestListener<DrinkingInvitation>() {
                                    @Override
                                    public void onRequestFailure(SpiceException spiceException) {
                                        testDone = true;
                                        fail(spiceException.getMessage());
                                    }

                                    @Override
                                    public void onRequestSuccess(final DrinkingInvitation drinkingInvitation) {
                                        getDrinkingInvitationDAO().decline(drinkingInvitation, new RequestListener<Void>() {
                                            @Override
                                            public void onRequestFailure(SpiceException spiceException) {
                                                testDone = true;
                                                fail(spiceException.getMessage());
                                            }

                                            @Override
                                            public void onRequestSuccess(Void aVoid) {
                                                getDrinkingSpotDAO().getActiveByPersonId(p1.getId(), new RequestListener<DrinkingSpot>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                                                        Assert.assertFalse(drinkingSpot.getPersons().contains(drinkingInvitation));
                                                        testDone = true;
                                                    }
                                                });
                                                getDrinkingInvitationDAO().getAllFor(p2.getId(), new RequestListener<DrinkingInvitation[]>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(DrinkingInvitation[] di) {
                                                        Assert.assertFalse(Arrays.asList(di).contains(drinkingInvitation));
                                                        testDone = true;
                                                    }
                                                });

                                                getDrinkingInvitationDAO().getAllFrom(p1.getId(), new RequestListener<DrinkingInvitation[]>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(DrinkingInvitation[] di) {
                                                        Assert.assertFalse(Arrays.asList(di).contains(drinkingInvitation));
                                                        testDone = true;
                                                    }
                                                });
                                            }
                                        });


                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        while (!testDone) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
