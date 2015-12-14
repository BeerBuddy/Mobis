package de.fh_dortmund.beerbuddy_44.picker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import de.fh_dortmund.beerbuddy.FriendList;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.BuddyPickerListAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by grimm on 02.12.2015.
 *
 * Revised and Updated by Marco on 10.12.2015.
 */
public class BuddyPicker {

    public static void show(Context context)
    {
        // custom dialog
        final Dialog dialog;
        try {

            //Using AlertDialog --> working
            FriendList friendListId = DAOFactory.getFreindlistDAO(context).getFriendListId(DAOFactory.getCurrentPersonDAO(context).getCurrentPersonId());

            AlertDialog.Builder b = new AlertDialog.Builder(context);
            final CharSequence[] s = new CharSequence[friendListId.getFriends().size()];
            for(int i=0; i< friendListId.getFriends().size();i++)
            {
                s[i]=friendListId.getFriends().get(i).getUsername();
            }

            boolean[] checked = new boolean[friendListId.getFriends().size()];

            //Using Multiple Choice of Buddys --> Checkboxes
            b.setTitle("Choose your mates").setMultiChoiceItems(s, checked, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    Log.d("User-ID", which + "");
                }
            });
            b.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("Button", "ENTER");
                }
            });
            b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("Button", "CANCEL");
                }
            });
            b.setNeutralButton("Select All", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("Button", "SELECT ALL");
                }
            });
            AlertDialog ad = b.create();
            ad.show();


/*            //using CustomDialog --> not working yet
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.buddypicker_dialog);
            dialog.setTitle("Title...");

            //set the custom dialog components - text, image and button
            ListView list = (ListView) dialog.findViewById(R.id.buddy_picker_friends);
            list.setAdapter(new BuddyPickerListAdapter(context, R.layout.buddypicker_list_row_layout, friendListId.getFriends().toArray(new Person[]{})));

            CheckBox dialogButton = (CheckBox) dialog.findViewById(R.id.buddy_picker_checkall);
            // if button is clicked, close the custom dialog
            dialogButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //TODO toggle selection
                }
            });
            dialog.show();*/

        } catch (BeerBuddyException e) {
            e.printStackTrace();
        }
    }
}