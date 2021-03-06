package de.fh_dortmund.beerbuddy_44.adapter;

import android.view.View;
import android.view.ViewGroup;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;

/**
 * Created by grimm on 01.12.2015.
 */
public class FriendListAdapter extends BuddyListAdapter {
    private static final String TAG = "FriendListAdapter";

    public FriendListAdapter(BeerBuddyActivity context, int resource, Person[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = super.getView(position,convertView,parent);
        // remove unnecessary buttons
            rowView.findViewById(R.id.buddy_list_row_button_add).setVisibility(View.GONE);
        rowView.findViewById(R.id.buddy_list_row_button_decline).setVisibility(View.GONE);

        return rowView;
    }

}
