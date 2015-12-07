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

import de.fh_dortmund.beerbuddy.FriendInvitation;
import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.acitvitys.ViewProfilActivity;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;

/**
 * Created by grimm on 01.12.2015.
 */
public class InvitedListAdapter extends ArrayAdapter<Person> {
    private final Person[] objects;
    private final Context context;
    private static final String TAG = "InvitedListAdapter";

    public InvitedListAdapter(Context context, int resource, Person[] objects) {
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
            rowView.findViewById(R.id.buddy_list_row_button_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //show the profil
                    Intent i = new Intent(context, ViewProfilActivity.class);
                    i.putExtra("id", p.getId());
                    context.startActivity(i);
                }
            });
            rowView.findViewById(R.id.buddy_list_row_button_add).setVisibility(View.GONE);
        }
        return rowView;
    }
}
