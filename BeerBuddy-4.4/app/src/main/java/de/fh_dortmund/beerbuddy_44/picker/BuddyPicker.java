package de.fh_dortmund.beerbuddy_44.picker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import java.util.List;

import de.fh_dortmund.beerbuddy.DrinkingSpot;
import de.fh_dortmund.beerbuddy.FriendList;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.DrinkingActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by David on 02.12.2015.
 *
 * Redesigned by Marco on 10.12.2015.
 */
public class BuddyPicker {

    public static void show(final DrinkingActivity context)
    {
        try {

            //Using AlertDialog
            final FriendList friendList = DAOFactory.getFriendlistDAO(context).getFriendListId(DAOFactory.getCurrentPersonDAO(context).getCurrentPersonId());
            final DrinkingSpot spot = context.getDrinkingSpot();
            final AlertDialog.Builder b = new AlertDialog.Builder(context);
            final CharSequence[] s = new CharSequence[friendList.getFriends().size()];
            for(int i=0; i< friendList.getFriends().size();i++)
            {
                s[i]=friendList.getFriends().get(i).getUsername();
            }

            final boolean[] checked = new boolean[friendList.getFriends().size()];

            //Using Multiple Choice of Buddys --> Checkboxes
            b.setTitle(context.getString(R.string.drinkinginvitation_title));
            b.setMultiChoiceItems(s, checked, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    //do nothing
                }
            });

            //Invite selected
            b.setPositiveButton(context.getString(R.string.button_inviteselected), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < checked.length; i++) {
                        if (checked[i]) {
                            Person invitedPerson = friendList.getFriends().get(i);
                            if (invite(spot.getPersons(), invitedPerson)) {
                                spot.getPersons().add(invitedPerson);
                            }
                        }
                    }
                    context.setValue(spot);
                }
            });

            //Invite all
            b.setNeutralButton(context.getString(R.string.button_inviteall), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Select all Buddys
                    for (int i = 0; i < checked.length; i++) {
                        checked[i] = true;
                        Person invitedPerson = friendList.getFriends().get(i);
                        if (invite(spot.getPersons(), invitedPerson)) {
                            spot.getPersons().add(invitedPerson);
                        }
                    }
                    context.setValue(spot);
                }
            });

            //Cancel
            b.setNegativeButton(context.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Deselect all Buddys
                    for (int i = 0; i < checked.length; i++) {
                        checked[i] = false;
                    }
                }
            });

            //Show the Dialog
            AlertDialog ad = b.create();
            ad.show();

        } catch (BeerBuddyException e) {
            e.printStackTrace();
        }
    }

    //check if you have to invite the Person
    //true --> invite, false --> is already invited
    private static boolean invite(List<Person> invitedPersons, Person person){
        for (Person ip : invitedPersons){
            if(person.getId() == ip.getId()) {
                Log.d("NOOOOOOO", person.getId() + " vs. " + ip.getId());
                return false;
            }
        }
        Log.d("YEEEEEEES", person.getId() + "");
        return true;
    }
}