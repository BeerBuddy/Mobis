package de.fh_dortmund.beerbuddy_44.picker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import de.fh_dortmund.beerbuddy.FriendList;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.adapter.BuddyListAdapter;
import de.fh_dortmund.beerbuddy_44.adapter.BuddyPickerListAdapter;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by grimm on 02.12.2015.
 */
public class BuddyPicker {

    public static void show(Context context)
    {
        //TODO create a customDialog with a freindlist
        // custom dialog
        final Dialog dialog;
        try {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.buddypicker_dialog);
            dialog.setTitle("Title...");

            // set the custom dialog components - text, image and button
            ListView list = (ListView) dialog.findViewById(R.id.buddy_picker_friends);
            FriendList friendListId = DAOFactory.getFreindlistDAO(context).getFriendListId(DAOFactory.getCurrentPersonDAO(context).getCurrentPersonId());

            list.setAdapter(new BuddyPickerListAdapter(context, R.layout.buddypicker_list_row_layout, friendListId.getFriends().toArray(new Person[]{})));

            CheckBox dialogButton = (CheckBox) dialog.findViewById(R.id.buddy_picker_checkall);
            // if button is clicked, close the custom dialog
            dialogButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //TODO toggle selection
                }
            });
            dialog.show();
        } catch (BeerBuddyException e) {
            e.printStackTrace();
        }


    }
}
