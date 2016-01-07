package de.fh_dortmund.beerbuddy_44.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.R;

/**
 * Created by grimm on 02.12.2015.
 */
public class BuddyPickerListAdapter  extends ArrayAdapter<Person> {
    private final Person[] objects;
    private final Context context;
    private static final String TAG = "BuddyPickerListAdapter";

    public BuddyPickerListAdapter(Context context, int resource, Person[] objects) {
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

            ((TextView)rowView.findViewById(R.id.buddy_list_row_name)).setText(p.getUsername());
            ((CheckBox)  rowView.findViewById(R.id.buddypicker_checkbox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //TODO this Person p is selected
                }
            });

        }

        return rowView;
    }

}
