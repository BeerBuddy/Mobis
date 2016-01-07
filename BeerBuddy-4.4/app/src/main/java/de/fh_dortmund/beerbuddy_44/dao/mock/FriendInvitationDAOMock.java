package de.fh_dortmund.beerbuddy_44.dao.mock;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy_44.dao.interfaces.FriendInvitationDAO;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by David on 30.11.2015.
 */
public class FriendInvitationDAOMock extends FriendInvitationDAO {

    private String[] text = new String[]{"Add me plz", "We meet last week in Moe's", "Your Profil sounds intresting", "Nice Pic", "I want to be your freind", "lets go for a drink"};

    Map<Long, List<FriendInvitation>> mapEinlader = new HashMap<Long, List<FriendInvitation>>();
    Map<Long, List<FriendInvitation>> mapEingeladener = new HashMap<Long, List<FriendInvitation>>();

    public FriendInvitationDAOMock(Context context) {
        super(context);
    }

    @Override
    public FriendInvitation insertOrUpdate(FriendInvitation i) {
        if(mapEinlader.get(i.getEinladerId()) == null)
        {
            mapEinlader.put(i.getEinladerId(), new ArrayList<FriendInvitation>());
        }
        mapEinlader.get(i.getEinladerId()).add(i);

        if(mapEingeladener.get(i.getEingeladenerId()) == null)
        {
            mapEingeladener.put(i.getEingeladenerId(), new ArrayList<FriendInvitation>());
        }
        mapEingeladener.get(i.getEingeladenerId()).add(i);
        return i;
    }

    @Override
    public  List<FriendInvitation>getAllFor(long personid) {

        List<FriendInvitation> friendInvitations = mapEingeladener.get(personid);
        if(friendInvitations == null || friendInvitations.isEmpty())
        {
            friendInvitations = generateRandomFor(personid);

        }
        return friendInvitations;
    }

    private List<FriendInvitation> generateRandomFor(long personid) {
        List<FriendInvitation> friendInvitations = new ArrayList<FriendInvitation>();
        for(int i =0; i< (int)(Math.random() *10); i++)
        {
            FriendInvitation invitation = new FriendInvitation();
            invitation.setEinladerId(personid);
            invitation.setEingeladenerId((long)Math.random() * Long.MAX_VALUE);
            invitation.setFreitext(text[((int)Math.random() * text.length)]);
        }
        return friendInvitations;
    }

    @Override
    public List<FriendInvitation> getAllFrom(long personid) {
        List<FriendInvitation> friendInvitations = mapEinlader.get(personid);
        if(friendInvitations == null || friendInvitations.isEmpty())
        {
            friendInvitations = generateRandomFrom(personid);

        }
        return friendInvitations;
    }

    @Override
    public void accept(FriendInvitation friendInvitation) throws BeerBuddyException {

    }

    @Override
    public void decline(FriendInvitation invitation) throws BeerBuddyException {

    }

    private List<FriendInvitation> generateRandomFrom(long personid) {
        List<FriendInvitation> friendInvitations = new ArrayList<FriendInvitation>();
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
