package de.fh_dortmund.beerbuddy_44.picker;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

import de.fh_dortmund.beerbuddy.entities.FriendList;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.DrinkingActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;

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
                    final AlertDialog.Builder b = new AlertDialog.Builder(context);
                    final CharSequence[] s = new CharSequence[friendList.getFriends().size()];
                    final boolean[] checked = new boolean[friendList.getFriends().size()];
                    final long[] invitedPersons = new long[friendList.getFriends().size()];

                    int anz = 0;
                    for (Person p : friendList.getFriends()) {
                        if (!alreadyJoined(p.getId(), context)) {
                            s[anz] = p.getUsername();
                            anz++;
                        }
                    }

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
                            int zaehler = 0;
                            for (int i = 0; i < checked.length; i++) {
                                if (checked[i]) {
                                    long invitedPerson = friendList.getFriends().toArray(new Person[]{})[i].getId();
                                    invitedPersons[zaehler] = invitedPerson;
                                    zaehler++;
                                }
                            }
                            context.setInvitedPersons(invitedPersons);
                        }
                    });

                    //Invite all
                    b.setNeutralButton(context.getString(R.string.button_inviteall), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Select all Buddys
                            int zaehler = 0;
                            for (int i = 0; i < checked.length; i++) {
                                checked[i] = true;
                                long invitedPerson = friendList.getFriends().toArray(new Person[]{})[i].getId();
                                invitedPersons[zaehler] = invitedPerson;
                                zaehler++;
                            }
                            context.setInvitedPersons(invitedPersons);
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
                            context.setInvitedPersons(invitedPersons);
                        }
                    });

                    //Show the Dialog
                    if (anz > 0) {
                        AlertDialog ad = b.create();
                        ad.show();
                    }
                }
            });

        } catch (BeerBuddyException e) {
            e.printStackTrace();
        }
    }

    private static boolean alreadyJoined(long person, DrinkingActivity context){
        boolean alreadyJoined = false;
        if (context.getDrinkingSpot() != null) {
            List<Person> joined = context.getDrinkingSpot().getPersons();
            for (Person p : joined) {
                if (p.getId() == person) {
                    alreadyJoined = true;
                }
            }
        }
        return alreadyJoined;
    }
}