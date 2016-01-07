package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;

import de.fh_dortmund.beerbuddy.entities.Person;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.listener.android.EditProfilListener;
import de.fh_dortmund.beerbuddy_44.picker.ImagePicker;
import lombok.Getter;

public class EditProfilActivity extends BeerBuddyActivity {
    public EditProfilActivity() {
        super(R.layout.edit_profil_activity_main, true);
    }

    public static final int PICK_IMAGE_ID = 1;
    private static final String TAG = "EditProfilActivity";
    @Getter
    private Person person;


    @Override
    protected void onFurtherCreate(Bundle savedInstanceState) {
        //getProfil
        try {
            person = DAOFactory.getPersonDAO(this).getById(DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId());
            setValues();

           //register Listeners
            EditProfilListener profilSaveListener = new EditProfilListener(this);
            Button changePic = (Button) this.findViewById(R.id.action_profil_image);
            Button save = (Button) this.findViewById(R.id.profil_save);
            TextView dateofbirth = (TextView) this.findViewById(R.id.profil_dateofbirth);
            Button button = (Button) this.findViewById(R.id.profil_dateofbirth_picker);

            save.setOnClickListener(profilSaveListener);
            changePic.setOnClickListener(profilSaveListener);
            dateofbirth.setOnClickListener(profilSaveListener);
            button.setOnClickListener(profilSaveListener);
        } catch (BeerBuddyException e) {
            e.printStackTrace();
        }
    }



    public void setValues() {
        if(person.getImage() != null && person.getImage().length > 0)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(person.getImage(), 0, person.getImage().length);
            ((ImageView) findViewById(R.id.profil_image)).setImageBitmap(bitmap);
        }


        ((EditText) findViewById(R.id.profil_username)).setText(person.getUsername());

        ((EditText) findViewById(R.id.profil_email)).setText(person.getEmail());
        switch (person.getGender()) {
            case Person.Gender.MALE:
                ((RadioButton) findViewById(R.id.radioButton_male)).setChecked(true);
                break;
            case Person.Gender.FEMALE:
                ((RadioButton) findViewById(R.id.radioButton_female)).setChecked(true);
                break;

            case Person.Gender.OTHER:
                ((RadioButton) findViewById(R.id.radioButton_other)).setChecked(true);
                break;
            default:
                Log.e(TAG, "No Gender specidied with the int value " + person.getGender());
                break;
        }

        ((EditText) findViewById(R.id.profil_interessen)).setText(person.getInterests());
        ((EditText) findViewById(R.id.profil_vorlieben)).setText(person.getPrefers());
        ((TextView) findViewById(R.id.profil_dateofbirth)).setText(DateFormat.getDateInstance().format(person.getDateOfBirth()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                person.setImage( getByteArrayFromImage(bitmap));
                setValues();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    public Person getValues() {
        //Date of Birth is set Through Listener
        //Need to set all other fields
        person.setInterests(((EditText) findViewById(R.id.profil_interessen)).getText().toString());
        person.setPrefers(((EditText) findViewById(R.id.profil_vorlieben)).getText().toString());

        if (((RadioButton) findViewById(R.id.radioButton_male)).isChecked()) {
            person.setGender(Person.Gender.MALE);
        } else if (((RadioButton) findViewById(R.id.radioButton_female)).isChecked()) {
            person.setGender(Person.Gender.FEMALE);
        }else{
            person.setGender(Person.Gender.OTHER);
        }
        person.setEmail(((EditText) findViewById(R.id.profil_email)).getText().toString());
        person.setUsername(((EditText) findViewById(R.id.profil_username)).getText().toString());
        return person;
    }

    private byte[] getByteArrayFromImage(Bitmap map) {
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            map.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] image = stream.toByteArray();
            return image;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
