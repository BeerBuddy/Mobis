package de.fh_dortmund.beerbuddy_44.picker;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Collection;
import java.util.List;

import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.DrinkingActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;

/**
 * Created by David on 02.12.2015.
 * <p/>
 * Redesigned by Marco on 10.12.2015.
 */
public class BuddyPicker {

    public static void show(final DrinkingActivity context) {
        try {

            //Using AlertDialog
            DAOFactory.getFriendlistDAO(context).getFriendList(DAOFactory.getCurrentPersonDAO(context).getCurrentPersonId(), new RequestListener<FriendList>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {

                }

                @Override
                public void onRequestSuccess(final FriendList friendList) {
                    final DrinkingSpot spot = context.getDrinkingSpot();
                    final AlertDialog.Builder b = new AlertDialog.Builder(context);
                    final CharSequence[] s = new CharSequence[friendList.getFriends().size()];
                    int i = 0;
                    for (Person p : friendList.getFriends()) {
                        s[i] = p.getUsername();
                        i++;
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
                                    Person invitedPerson = friendList.getFriends().toArray(new Person[]{})[i];
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
                                Person invitedPerson = friendList.getFriends().toArray(new Person[]{})[i];
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

                }
            });

        } catch (BeerBuddyException e) {
            e.printStackTrace();
        }
    }

    //check if you have to invite the Person
    //true --> invite, false --> is already invited
    private static boolean invite(Collection<Person> invitedPersons, Person person) {
        for (Person ip : invitedPersons) {
            if (person.getId() == ip.getId()) {
                return false;
            }
        }
        return true;
    }
}