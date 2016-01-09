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

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import de.fh_dortmund.beerbuddy.entities.FriendInvitation;
import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.IntentUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.BeerBuddyActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;

/**
 * Created by grimm on 02.12.2015.
 */
public class FriendInvitationAdapter extends ArrayAdapter<FriendInvitation> {
    public final static String UPDATE_FRIENDLIST = "de.fh_dortmund.beerbuddy_44.UPDATE_FRIENDLIST";
    private static final String TAG = "FriendInvitationAdapter";
    private final BeerBuddyActivity context;
    private final FriendInvitation[] objects;

    public FriendInvitationAdapter(BeerBuddyActivity context, int resource, FriendInvitation[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.buddy_list_row_layout, parent, false);

        final FriendInvitation friendInvitation = objects[position];

        DAOFactory.getPersonDAO(context).getById(friendInvitation.getEinladerId(), new RequestListener<Person>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Error occurred while getting person from friendInvitation", spiceException);
            }

            @Override
            public void onRequestSuccess(Person p) {
                if (p != null) {
                    if (p.getImage() != null && p.getImage().length > 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(p.getImage(), 0, p.getImage().length);
                        ((ImageView) rowView.findViewById(R.id.buddy_list_row_icon)).setImageBitmap(bitmap);
                    }
                    ((TextView) rowView.findViewById(R.id.buddy_list_row_name)).setText(p.getUsername());
                    if (p.getUsername() == null) {
                        ((TextView) rowView.findViewById(R.id.buddy_list_row_name)).setText(p.getEmail());
                    }
                }
            }
        });


        rowView.findViewById(R.id.buddy_list_row_button_view).setOnClickListener(new IntentUtil.ShowProfilListener(context, friendInvitation.getEinladerId()));
        rowView.findViewById(R.id.buddy_list_row_button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //accept a Friend Request
                DAOFactory.getFriendInvitationDAO(context).accept(friendInvitation, new RequestListener<Void>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Log.e(TAG, "Error occurred while accepting friendInvitation", spiceException);
                    }

                    @Override
                    public void onRequestSuccess(Void aVoid) {
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(UPDATE_FRIENDLIST);
                        context.sendBroadcast(broadcastIntent);
                        Toast.makeText(context, context.getString(R.string.request_accepted), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        rowView.findViewById(R.id.buddy_list_row_button_decline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //decline a Friend Request
                DAOFactory.getFriendInvitationDAO(context).decline(friendInvitation, new RequestListener<Void>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Log.e(TAG, "Error occurred while declining friendInvitation", spiceException);
                    }

                    @Override
                    public void onRequestSuccess(Void aVoid) {
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(UPDATE_FRIENDLIST);
                        context.sendBroadcast(broadcastIntent);
                        Toast.makeText(context, context.getString(R.string.request_declined), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        return rowView;
    }
}
