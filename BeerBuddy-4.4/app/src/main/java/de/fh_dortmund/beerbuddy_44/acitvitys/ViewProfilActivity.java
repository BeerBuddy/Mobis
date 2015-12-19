package de.fh_dortmund.beerbuddy_44.acitvitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.fh_dortmund.beerbuddy.Person;
import de.fh_dortmund.beerbuddy_44.ObjectMapperUtil;
import de.fh_dortmund.beerbuddy_44.R;
import de.fh_dortmund.beerbuddy_44.dao.DAOFactory;
import de.fh_dortmund.beerbuddy_44.exceptions.BeerBuddyException;
import de.fh_dortmund.beerbuddy_44.exceptions.MissingParameterExcetion;
import de.fh_dortmund.beerbuddy_44.listener.android.ViewProfilListener;

public class ViewProfilActivity extends BeerBuddyActivity {
    public ViewProfilActivity() {
        super(R.layout.view_profil_activity_main, true);
    }

    private static final String TAG = "ViewProfilActivity";

    @Override
    protected void onFurtherCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        long id = b.getLong("id");
        try {
            if (id != 0) {
                Person p = DAOFactory.getPersonDAO(this).getById(id);
                long currentPerson = DAOFactory.getCurrentPersonDAO(this).getCurrentPersonId();
                fillValues(p);

                //register ViewProfilListener
                if (currentPerson == p.getId() && DAOFactory.getFriendlistDAO(this).isFriendFromId(currentPerson, p.getId())) {
                    ViewProfilListener viewListener = new ViewProfilListener(this, p);
                    ((Button) findViewById(R.id.action_profil_send_request)).setOnClickListener(viewListener);
                } else {
                    //hide the Button
                    ((Button) findViewById(R.id.action_profil_send_request)).setVisibility(View.INVISIBLE);
                }

            } else {
                throw new MissingParameterExcetion("Expected a Parameter: long id when calling ViewProfil");
            }
        } catch (BeerBuddyException e) {
            e.printStackTrace();
        }


    }

    private void fillValues(Person p) {
        if (p.getImage() != null && p.getImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(p.getImage(), 0, p.getImage().length);
            ((ImageView) findViewById(R.id.profil_image)).setImageBitmap(bitmap);
        }
        ((TextView) findViewById(R.id.profil_username)).setText(p.getUsername());
        ((TextView) findViewById(R.id.profil_vorlieben)).setText(p.getPrefers());
        ((TextView) findViewById(R.id.profil_interessen)).setText(p.getInterests());
        switch (p.getGender()) {
            case Person.Gender.MALE:
                ((TextView) findViewById(R.id.profil_geschlecht)).setText(getString(R.string.profil_gender_male));
                break;
            case Person.Gender.FEMALE:
                ((TextView) findViewById(R.id.profil_geschlecht)).setText(getString(R.string.profil_gender_female));
                break;
            case Person.Gender.OTHER:
                ((TextView) findViewById(R.id.profil_geschlecht)).setText(getString(R.string.profil_gender_other));
                break;
            default:
                Log.e(TAG, "undefinde Gender for Person:" + p.getId());
                break;
        }
        int ageFromBirthday = ObjectMapperUtil.getAgeFromBirthday(p.getDateOfBirth());
        ((TextView) findViewById(R.id.profil_alter)).setText(ageFromBirthday + "");
    }

}
