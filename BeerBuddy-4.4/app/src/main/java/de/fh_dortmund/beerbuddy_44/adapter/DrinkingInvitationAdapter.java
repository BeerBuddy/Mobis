package de.fh_dortmund.beerbuddy_44.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.IntentUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by grimm on 02.12.2015.
 */
public class DrinkingInvitationAdapter extends ArrayAdapter<DrinkingInvitation>{
    private final Context context;
    private final DrinkingInvitation[] objects;
    private static final String TAG = "DrinkingInvitationAd";
    public final static String UPDATE_DRINKING_INVITATIONS ="de.fh_dortmund.beerbuddy_44.UPDATE_DRINKING_INVITATIONS";

    public DrinkingInvitationAdapter(Context context, int resource, DrinkingInvitation[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.buddy_list_row_layout, parent, false);

        final DrinkingInvitation friendInvitation = objects[position];

        try {
            Person p = DAOFactory.getPersonDAO(context).getById(friendInvitation.getEingeladenerId());
            final DrinkingSpot drinkingSpot = DAOFactory.getDrinkingSpotDAO(context).getActiveByPersonId(friendInvitation.getEingeladenerId());
            if(p!= null)
            {
                if(p.getImage() != null && p.getImage().length > 0)
                {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(p.getImage(), 0, p.getImage().length);
                    ((ImageView)rowView.findViewById(R.id.buddy_list_row_icon)).setImageBitmap(bitmap);
                }
                ((TextView) rowView.findViewById(R.id.buddy_list_row_name)).setText(p.getUsername());
                rowView.findViewById(R.id.buddy_list_row_button_view).setOnClickListener(new IntentUtil.ShowDrinkingSpotListener(context, drinkingSpot.getId()));
                rowView.findViewById(R.id.buddy_list_row_button_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //accept a Friend Request
                        try {
                            DAOFactory.getDrinkingInvitationDAO(context).accept(friendInvitation);
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction(UPDATE_DRINKING_INVITATIONS);
                            context.sendBroadcast(broadcastIntent);
                            Toast.makeText(context, context.getString(R.string.request_accepted), Toast.LENGTH_SHORT).show();
                        } catch (BeerBuddyException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error accured during FreindRequest", e);
                        }
                    }
                });
            }
        } catch (BeerBuddyException e) {
            e.printStackTrace();
            Log.e(TAG, "Error accured during FreindRequest", e);
        }

        return rowView;
    }
}
