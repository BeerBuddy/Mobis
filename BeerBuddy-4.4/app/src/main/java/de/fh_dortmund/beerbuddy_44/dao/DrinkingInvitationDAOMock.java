package de.fh_dortmund.beerbuddy_44.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fh_dortmund.beerbuddy.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.FriendInvitation;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.DrinkingInvitationDAO;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 30.11.2015.
 */
class DrinkingInvitationDAOMock extends DrinkingInvitationDAO {

    private String[] text = new String[]{"Add me plz", "We meet last week in Moe's", "Your Profil sounds intresting", "Nice Pic", "I want to be your freind", "lets go for a drink"};

    Map<Long, List<DrinkingInvitation>> mapEinlader = new HashMap<Long, List<DrinkingInvitation>>();
    Map<Long, List<DrinkingInvitation>> mapEingeladener = new HashMap<Long, List<DrinkingInvitation>>();

    public DrinkingInvitationDAOMock(Context context) {
        super(context);
    }

    @Override
    public void insertOrUpdate(DrinkingInvitation i) {
        if(mapEinlader.get(i.getEinladerId()) == null)
        {
            mapEinlader.put(i.getEinladerId(), new ArrayList<DrinkingInvitation>());
        }
        mapEinlader.get(i.getEinladerId()).add(i);

        if(mapEingeladener.get(i.getEingeladenerId()) == null)
        {
            mapEingeladener.put(i.getEingeladenerId(), new ArrayList<DrinkingInvitation>());
        }
        mapEingeladener.get(i.getEingeladenerId()).add(i);
    }

    @Override
    public  List<DrinkingInvitation>getAllFor(long personid) {

        List<DrinkingInvitation> friendInvitations = mapEingeladener.get(personid);
        if(friendInvitations == null || friendInvitations.isEmpty())
        {
            friendInvitations = generateRandomFor(personid);

        }
        return friendInvitations;
    }

    private List<DrinkingInvitation> generateRandomFor(long personid) {
        List<DrinkingInvitation> friendInvitations = new ArrayList<DrinkingInvitation>();
        for(int i =0; i< (int)(Math.random() *10); i++)
        {
            DrinkingInvitation invitation = new DrinkingInvitation();
            invitation.setEinladerId(personid);
            invitation.setEingeladenerId((long)Math.random() * Long.MAX_VALUE);
            invitation.setFreitext(text[((int)Math.random() * text.length)]);
        }
        return friendInvitations;
    }

    @Override
    public List<DrinkingInvitation> getAllFrom(long personid) {
        List<DrinkingInvitation> friendInvitations = mapEinlader.get(personid);
        if(friendInvitations == null || friendInvitations.isEmpty())
        {
            friendInvitations = generateRandomFrom(personid);

        }
        return friendInvitations;
    }

    @Override
    public void accept(DrinkingInvitation friendInvitation) throws BeerBuddyException {

    }

    private List<DrinkingInvitation> generateRandomFrom(long personid) {
        List<DrinkingInvitation> friendInvitations = new ArrayList<DrinkingInvitation>();
        for(int i =0; i< (int)(Math.random() *10); i++)
        {
            FriendInvitation invitation = new FriendInvitation();
            invitation.setEinladerId((long)Math.random() * Long.MAX_VALUE);
            invitation.setEingeladenerId(personid);
            invitation.setFreitext(text[((int)Math.random() * text.length)]);
        }
        return friendInvitations;
    }


}
