package de.fh_dortmund.beerbuddy_44.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.DrinkingInvitation;
import de.fh_dortmund.beerbuddy.entities.DrinkingSpot;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.IntentUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;

/**
 * Created by grimm on 02.12.2015.
 */
public class DrinkingInvitationAdapter extends ArrayAdapter<DrinkingInvitation> {
    public final static String UPDATE_DRINKING_INVITATIONS = "de.fh_dortmund.beerbuddy_44.UPDATE_DRINKING_INVITATIONS";
    private static final String TAG = "DrinkingInvitationAd";
    private final BeerBuddyActivity context;
    private final DrinkingInvitation[] objects;

    public DrinkingInvitationAdapter(BeerBuddyActivity context, int resource, DrinkingInvitation[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.buddy_list_row_layout, parent, false);

        final DrinkingInvitation drinkingInvitation = objects[position];

        // Get the drinkingInvitation einlader to display his profile picture and his username/email
        DAOFactory.getPersonDAO(context).getById(drinkingInvitation.getEinladerId(), new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Error occurred while getting creator drinkingInvitations", spiceException);
            }

            @Override
            public void onRequestSuccess(Person person) {
                if (person != null) {
                    if (person.getImage() != null && person.getImage().length > 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(person.getImage(), 0, person.getImage().length);
                        ((ImageView) rowView.findViewById(R.id.buddy_list_row_icon)).setImageBitmap(bitmap);
                    }

                    ((TextView) rowView.findViewById(R.id.buddy_list_row_name)).setText(person.getUsername());
                    if (person.getUsername() == null) {
                        ((TextView) rowView.findViewById(R.id.buddy_list_row_name)).setText(person.getEmail());
                    }
                }
            }
        });

        // Get the drinkingSpot from the drinkingInvitation
        DAOFactory.getDrinkingSpotDAO(context).getById(drinkingInvitation.getDrinkingSpotId(), new RequestListener<DrinkingSpot>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Error occurred while getting the drinkingSpot from a drinkingInvitation", spiceException);
            }

            @Override
            public void onRequestSuccess(DrinkingSpot drinkingSpot) {
                Drawable multiple = context.getResources().getDrawable(R.drawable.ic_account_multiple);
                ((Button) rowView.findViewById(R.id.buddy_list_row_button_view)).setCompoundDrawablesWithIntrinsicBounds(null, multiple, null, null);
                rowView.findViewById(R.id.buddy_list_row_button_view).setOnClickListener(new IntentUtil.ShowDrinkingSpotListener(context, drinkingSpot.getId(), false));

                rowView.findViewById(R.id.buddy_list_row_button_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //accept a drinking Request
                        DAOFactory.getDrinkingInvitationDAO(context).accept(drinkingInvitation, new RequestListener<Void>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                Log.e(TAG, "Error occurred while accepting a drinkingInvitation", spiceException);
                            }

                            @Override
                            public void onRequestSuccess(Void aVoid) {
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(UPDATE_DRINKING_INVITATIONS);
                                context.sendBroadcast(broadcastIntent);
                                Toast.makeText(context, context.getString(R.string.request_accepted), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                rowView.findViewById(R.id.buddy_list_row_button_decline).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //decline a drinking Request
                        DAOFactory.getDrinkingInvitationDAO(context).decline(drinkingInvitation, new RequestListener<Void>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                Log.e(TAG, "Error occurred while declining a drinkingInvitation", spiceException);
                            }

                            @Override
                            public void onRequestSuccess(Void aVoid) {
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(UPDATE_DRINKING_INVITATIONS);
                                context.sendBroadcast(broadcastIntent);
                                Toast.makeText(context, context.getString(R.string.request_declined), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });

        return rowView;
    }
}
