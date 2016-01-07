package de.fh_dortmund.beerbuddy_44.adapter;

import android.content.Context;
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
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;

/**
 * Created by grimm on 01.12.2015.
 */
public class BuddyListAdapter extends ArrayAdapter<Person> {
    private final Person[] objects;
    private final BeerBuddyActivity context;
    private static final String TAG = "BuddyListAdapter";

    public BuddyListAdapter(BeerBuddyActivity context, int resource, Person[] objects) {
        super(context, resource, objects);

        this.context = context;
        this.objects = objects;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.buddy_list_row_layout, parent, false);

        final Person p = objects[position];
        if(p!= null)
        {
            if(p.getImage() != null && p.getImage().length > 0)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(p.getImage(), 0, p.getImage().length);
                ((ImageView)rowView.findViewById(R.id.buddy_list_row_icon)).setImageBitmap(bitmap);
            }
            ((TextView) rowView.findViewById(R.id.buddy_list_row_name)).setText(p.getUsername());
            rowView.findViewById(R.id.buddy_list_row_button_view).setOnClickListener(new IntentUtil.ShowProfilListener(context, p.getId()));
            rowView.findViewById(R.id.buddy_list_row_button_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send a Friend Request
                    try {
                        FriendInvitation i = new FriendInvitation();
                        i.setEingeladenerId(p.getId());
                        i.setEinladerId(DAOFactory.getCurrentPersonDAO(context).getCurrentPersonId());
                        DAOFactory.getFriendInvitationDAO(context).insertOrUpdate(i, new RequestListener<FriendInvitation>() {
                            @Override
                            public void onRequestFailure(SpiceException spiceException) {

                            }

                            @Override
                            public void onRequestSuccess(FriendInvitation friendInvitation) {
                                Toast.makeText(context, context.getString(R.string.request_send), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (BeerBuddyException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error accured during FreindRequest",e);
                    }
                }
            });
        }

        return rowView;
    }

}
