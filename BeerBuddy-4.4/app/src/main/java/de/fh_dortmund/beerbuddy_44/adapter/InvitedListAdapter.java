package de.fh_dortmund.beerbuddy_44.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.IntentUtil;
import de.fh_dortmund.beerbuddy_44.R;

/**
 * Created by grimm on 01.12.2015.
 */
public class InvitedListAdapter extends ArrayAdapter<Person> {
    private static final String TAG = "InvitedListAdapter";
    private final Person[] objects;
    private final Context context;

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
            rowView.findViewById(R.id.buddy_list_row_button_view).setOnClickListener(new IntentUtil.ShowProfilListener(context, p.getId()));
            rowView.findViewById(R.id.buddy_list_row_button_add).setVisibility(View.GONE);
            rowView.findViewById(R.id.buddy_list_row_button_decline).setVisibility(View.GONE);
        }
        return rowView;
    }
}
