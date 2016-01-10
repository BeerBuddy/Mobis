package de.fh_dortmund.beerbuddy_44.test.dao.common;

import android.test.ActivityInstrumentationTestCase2;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import junit.framework.Assert;

import java.util.Arrays;
import java.util.Date;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.acitvitys.MainViewActivity;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingSpotDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendListDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.PersonDAO;

/**
 * Created by grimm on 08.01.2016.
 */
public abstract class FriendInvitationDAOTest extends ActivityInstrumentationTestCase2<MainViewActivity> {


    private boolean testDone;

    public FriendInvitationDAOTest() {
        super(MainViewActivity.class);
    }

    public abstract FriendListDAO getFriendListDAO();

    public abstract FriendInvitationDAO getFriendInvitationDAO();

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
                        FriendList fl = new FriendList();
                        fl.setPersonid(p1.getId());
                        getFriendListDAO().insertOrUpdate(fl, new RequestListener<FriendList>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                testDone = true;
                                fail(spiceException.getMessage());
                                spiceException.printStackTrace();
                            }

                            @Override
                            public void onRequestSuccess(FriendList drinkingSpot) {
                                assertFalse(drinkingSpot.getId() == 0);
                                assertEquals(p1.getId(), drinkingSpot.getPersonid());
                                FriendInvitation drinkingInvitation = new FriendInvitation();
                                drinkingInvitation.setEingeladenerId(p2.getId());
                                drinkingInvitation.setEinladerId(p1.getId());

                                getFriendInvitationDAO().insertOrUpdate(drinkingInvitation, new RequestListener<FriendInvitation>() {
                                    @Override
                                    public void onRequestFailure(SpiceException spiceException) {
                                        testDone = true;
                                        fail(spiceException.getMessage());
                                    }

                                    @Override
                                    public void onRequestSuccess(final FriendInvitation drinkingInvitation) {
                                        getFriendInvitationDAO().getAllFor(p2.getId(), new RequestListener<FriendInvitation[]>() {
                                            @Override
                                            public void onRequestFailure(SpiceException spiceException) {
                                                testDone = true;
                                                fail(spiceException.getMessage());
                                            }

                                            @Override
                                            public void onRequestSuccess(FriendInvitation[] di) {
                                                Assert.assertTrue(Arrays.asList(di).contains(drinkingInvitation));
                                                testDone = true;
                                            }
                                        });

                                        getFriendInvitationDAO().getAllFrom(p1.getId(), new RequestListener<FriendInvitation[]>() {
                                            @Override
                                            public void onRequestFailure(SpiceException spiceException) {
                                                testDone = true;
                                                fail(spiceException.getMessage());
                                            }

                                            @Override
                                            public void onRequestSuccess(FriendInvitation[] di) {
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
                        FriendList fl = new FriendList();
                        fl.setPersonid(p1.getId());
                        getFriendListDAO().insertOrUpdate(fl, new RequestListener<FriendList>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                testDone = true;
                                fail(spiceException.getMessage());
                                spiceException.printStackTrace();
                            }

                            @Override
                            public void onRequestSuccess(FriendList drinkingSpot) {
                                assertFalse(drinkingSpot.getId() == 0);
                                assertEquals(p1.getId(), drinkingSpot.getPersonid());
                                FriendInvitation drinkingInvitation = new FriendInvitation();
                                drinkingInvitation.setEingeladenerId(p2.getId());
                                drinkingInvitation.setEinladerId(p1.getId());

                                getFriendInvitationDAO().insertOrUpdate(drinkingInvitation, new RequestListener<FriendInvitation>() {
                                    @Override
                                    public void onRequestFailure(SpiceException spiceException) {
                                        testDone = true;
                                        fail(spiceException.getMessage());
                                    }

                                    @Override
                                    public void onRequestSuccess(final FriendInvitation drinkingInvitation) {
                                        getFriendInvitationDAO().accept(drinkingInvitation, new RequestListener<Void>() {
                                            @Override
                                            public void onRequestFailure(SpiceException spiceException) {
                                                testDone = true;
                                                fail(spiceException.getMessage());
                                            }

                                            @Override
                                            public void onRequestSuccess(Void aVoid) {
                                                getFriendInvitationDAO().getAllFor(p2.getId(), new RequestListener<FriendInvitation[]>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(FriendInvitation[] di) {
                                                        Assert.assertFalse(Arrays.asList(di).contains(drinkingInvitation));
                                                        testDone = true;
                                                    }
                                                });

                                                getFriendInvitationDAO().getAllFrom(p1.getId(), new RequestListener<FriendInvitation[]>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(FriendInvitation[] di) {
                                                        Assert.assertFalse(Arrays.asList(di).contains(drinkingInvitation));
                                                        testDone = true;
                                                    }
                                                });

                                                getFriendListDAO().getFriendList(p2.getId(), new RequestListener<FriendList>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        spiceException.printStackTrace();
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(FriendList friendList) {
                                                        assertTrue(friendList.getFriends().contains(p1));
                                                        testDone = true;
                                                    }
                                                });
                                                getFriendListDAO().isFriendFromId(p1.getId(), p2.getId(), new RequestListener<Boolean>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(Boolean aBoolean) {
                                                        assertTrue(aBoolean);
                                                        testDone = true;
                                                    }
                                                });
                                                getFriendListDAO().isFriendFromId(p2.getId(), p1.getId(), new RequestListener<Boolean>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(Boolean aBoolean) {
                                                        assertTrue(aBoolean);
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
                        FriendList fl = new FriendList();
                        fl.setPersonid(p1.getId());
                        getFriendListDAO().insertOrUpdate(fl, new RequestListener<FriendList>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                testDone = true;
                                fail(spiceException.getMessage());
                                spiceException.printStackTrace();
                            }

                            @Override
                            public void onRequestSuccess(FriendList drinkingSpot) {
                                assertFalse(drinkingSpot.getId() == 0);
                                assertEquals(p1.getId(), drinkingSpot.getPersonid());
                                FriendInvitation drinkingInvitation = new FriendInvitation();
                                drinkingInvitation.setEingeladenerId(p2.getId());
                                drinkingInvitation.setEinladerId(p1.getId());

                                getFriendInvitationDAO().insertOrUpdate(drinkingInvitation, new RequestListener<FriendInvitation>() {
                                    @Override
                                    public void onRequestFailure(SpiceException spiceException) {
                                        testDone = true;
                                        fail(spiceException.getMessage());
                                    }

                                    @Override
                                    public void onRequestSuccess(final FriendInvitation drinkingInvitation) {
                                        getFriendInvitationDAO().decline(drinkingInvitation, new RequestListener<Void>() {
                                            @Override
                                            public void onRequestFailure(SpiceException spiceException) {
                                                testDone = true;
                                                fail(spiceException.getMessage());
                                            }

                                            @Override
                                            public void onRequestSuccess(Void aVoid) {
                                                getFriendInvitationDAO().getAllFor(p2.getId(), new RequestListener<FriendInvitation[]>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(FriendInvitation[] di) {
                                                        Assert.assertFalse(Arrays.asList(di).contains(drinkingInvitation));
                                                        testDone = true;
                                                    }
                                                });

                                                getFriendInvitationDAO().getAllFrom(p1.getId(), new RequestListener<FriendInvitation[]>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(FriendInvitation[] di) {
                                                        Assert.assertFalse(Arrays.asList(di).contains(drinkingInvitation));
                                                        testDone = true;
                                                    }
                                                });

                                                getFriendListDAO().getFriendList(p2.getId(), new RequestListener<FriendList>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(FriendList friendList) {
                                                        if(friendList!=null)
                                                        assertFalse(friendList.getFriends().contains(p1));
                                                        testDone = true;
                                                    }
                                                });
                                                getFriendListDAO().isFriendFromId(p1.getId(), p2.getId(), new RequestListener<Boolean>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(Boolean aBoolean) {
                                                        assertFalse(aBoolean);
                                                        testDone = true;
                                                    }
                                                });
                                                getFriendListDAO().isFriendFromId(p2.getId(), p1.getId(), new RequestListener<Boolean>() {
                                                    @Override
                                                    public void onRequestFailure(SpiceException spiceException) {
                                                        testDone = true;
                                                        fail(spiceException.getMessage());
                                                    }

                                                    @Override
                                                    public void onRequestSuccess(Boolean aBoolean) {
                                                        assertFalse(aBoolean);
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
